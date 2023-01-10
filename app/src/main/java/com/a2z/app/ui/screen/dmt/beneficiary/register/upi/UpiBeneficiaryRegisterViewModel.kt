package com.a2z.app.ui.screen.dmt.beneficiary.register.upi

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.*
import com.a2z.app.data.repository.UpiRepository
import com.a2z.app.ui.theme.*
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.ui.util.extension.safeParcelable
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpiBeneficiaryRegisterViewModel
@Inject constructor(
    private val upiRepository: UpiRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val moneySender: MoneySender = savedStateHandle.safeParcelable("moneySender")!!


    val vpaListResultFlow = resultStateFlow<VpaBankExtensionResponse>()

    val selectedBankState = mutableStateOf<UpiBank?>(null)

    var verificationDoneByApi = false

    val upiExtensions = mutableStateListOf<UpiExtension>()
    private val upiBanks = mutableStateListOf<UpiBank>()

    val nameValidation = mutableStateOf(false)

    val input = UpiBeneficiaryRegisterInput(nameValidation)


    val actionType = mutableStateOf(ActionType.VERIFY)

    val buttonText: String
        get() = if (actionType.value == ActionType.VERIFY) "Verify" else "Register"

    val confirmVerificationDialog = mutableStateOf(false)
    val confirmResetDialog = mutableStateOf(false)

    val inputEnable: Boolean
        get() = actionType.value == ActionType.VERIFY


    private val _verificationChargeResultFlow = resultShareFlow<VpaVerificationChargeResponse>()
    private val _verifyResultFlow = resultShareFlow<AccountVerify>()
    private val _registerResultFlow = resultShareFlow<AppResponse>()
    var chargeAmount = mutableStateOf<String?>(null)

    init {
        fetchVpaAccounts()
        viewModelScope.launch {
            _verificationChargeResultFlow.getLatest {
                if (it.status == 1) {
                    chargeAmount.value = it.data?.chargeAmount
                    confirmVerificationDialog.value = true
                } else failureBanner(
                    title = "Verification failed",
                    message = it.message
                )
            }
        }

        viewModelScope.launch {
            _verifyResultFlow.getLatest {
                if (it.status == 1 || it.status ==7001) {
                    verificationDoneByApi = true
                    input.name.setValue(it.upiBeneName)
                    nameValidation.value = true
                    actionType.value = ActionType.REGISTER
                } else {
                    alertDialog(it.message)
                }
            }
        }

        viewModelScope.launch {
            _registerResultFlow.getLatest {
                if (it.status == 1) successDialog(it.message){
                    navigateUpWithResult("isRegistered" to true)
                }
                else alertDialog(it.message)
            }
        }
    }

    private fun initialSelectedBank() {
        if (upiBanks.isEmpty()) return
        selectedBankState.value = upiBanks.find { it.name == "Bank Upi" }
        upiExtensions.clear()
        upiExtensions.addAll(selectedBankState.value?.upiextensions!!)
    }

    private fun fetchVpaAccounts() {
        callApiForStateFlow(
            flow = vpaListResultFlow,
            call = { upiRepository.vpaList() },
            beforeEmit = {
                if (it is ResultType.Success) {
                    it.data.upiBank?.let {
                        upiBanks.clear()
                        upiBanks.addAll(it)
                        initialSelectedBank()
                    }
                }
            }
        )
    }

    private fun fetchVerificationCharge() {
        callApiForShareFlow(
            flow = _verificationChargeResultFlow,
            call = {
                upiRepository.verificationCharge(
                    hashMapOf(
                        "type" to "VPA",
                    )
                )
            },
        )
    }

    fun onSelectBank(it: UpiBank) {
        if (!inputEnable) return
        selectedBankState.value = it
        upiExtensions.clear()
        upiExtensions.addAll(selectedBankState.value?.upiextensions!!)

    }

    fun onSelectExtension(it: UpiExtension) {
        if (!inputEnable) return

        val upiNumber = input.upiId

        if (upiNumber.getValue().contains("@")) {
            val indexOf = upiNumber.getValue().indexOf("@")
            val newInput = upiNumber.getValue().substring(0, indexOf)
            upiNumber.setValue("")
            upiNumber.setValue("$newInput@${it.name}")
        } else {
            upiNumber.setValue("${input.upiId.getValue()}@${it.name}")
        }

    }

    fun getExtensionColor(): Color {

        return when (selectedBankState.value?.name?.toLowerCase()) {
            "paytm" -> PrimaryColor
            "amazon" -> YellowColor
            "phone pay" -> PurpleColor
            "google pay" -> GreenColor
            else -> PrimaryColorDark
        }
    }

    fun onReset() {
        input.upiId.setValue("")
        nameValidation.value = false
        input.name.setValue("")
        verificationDoneByApi = false
        actionType.value = ActionType.VERIFY
        initialSelectedBank()
    }

    fun onProceedButtonClick() {
        when (actionType.value) {
            ActionType.VERIFY -> fetchVerificationCharge()
            ActionType.REGISTER -> registerUpiId()
        }
    }

    private fun registerUpiId() {

        val param = hashMapOf(
            "sender_number" to moneySender.mobileNumber.orEmpty(),
            "beneficiary_vpa" to input.upiId.getValue(),
            "wallet_type" to selectedBankState.value?.name.toString(),
            "beneficiary_name" to input.name.getValue(),
            "is_bank_verified" to "1",
        )
        callApiForShareFlow(
            flow =_registerResultFlow,
            call = { upiRepository.addBeneficiary(param) }
        )
    }

    fun verifyUpiId() {
        callApiForShareFlow(
            flow = _verifyResultFlow,
            call = {
                upiRepository.accountValidation(
                    hashMapOf(
                        "number" to input.upiId.getValue(),
                        "type" to "VPA"
                    )
                )
            },
        )

    }

    enum class ActionType {
        VERIFY, REGISTER
    }

}

data class UpiBeneficiaryRegisterInput(
    val nameValidation: MutableState<Boolean>,
    val upiId: InputWrapper = InputWrapper { AppValidator.upiId(it) },
    val name: InputWrapper = InputWrapper(nameValidation) { AppValidator.minThreeChar(it) },
) : BaseInput(upiId, name)