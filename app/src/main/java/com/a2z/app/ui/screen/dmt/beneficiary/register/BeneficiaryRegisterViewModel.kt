package com.a2z.app.ui.screen.dmt.beneficiary.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.AccountVerify
import com.a2z.app.data.model.dmt.Bank
import com.a2z.app.data.model.dmt.BankListResponse
import com.a2z.app.data.model.dmt.MoneySender
import com.a2z.app.data.repository.DMTRepository
import com.a2z.app.ui.screen.dmt.util.DMTType
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.*
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.AppUtil
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeneficiaryRegisterViewModel @Inject constructor(
    private val repository: DMTRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    val moneySender: MoneySender = savedStateHandle.safeParcelable("moneySender")!!

    val input = BeneficiaryRegisterInput()
    val spinnerDialogState = mutableStateOf(false)

    private val _addBeneficiaryResultFlow = resultShareFlow<AppResponse>()
    private val _validationResultFlow = resultShareFlow<AccountVerify>()
    private val _bankListResultFlow = resultStateFlow<BankListResponse>()
    val bankListResultFlow = _bankListResultFlow.asStateFlow()

    var bankList: ArrayList<Bank> = arrayListOf()
    val selectedBank = mutableStateOf<Bank?>(null)

    init {


        fetchBankList()

        viewModelScope.launch {
            _validationResultFlow.getLatest {
                if (it.status == 1) successDialog(it.message + "\n" + it.beneName) {
                    input.name.setValue(it.beneName)
                }
                else alertDialog(it.message)
            }
        }

        viewModelScope.launch {
            _addBeneficiaryResultFlow.getLatest {
                if (it.status == 500 || it.status == 5514) successDialog(it.message){
                    navigateUpWithResult("isRegistered" to true)
                }
                else alertDialog(it.message)
            }
        }
    }

    private fun fetchBankList() {
        callApiForStateFlow(
            flow = _bankListResultFlow,
            call = { repository.bankList(hashMapOf()) },
            beforeEmit = {

                if (it is ResultType.Success
                    && it.data.status == 1
                    && it.data.data != null
                ) bankList = it.data.data!!
            }
        )
    }

    fun onVerifyClick() {

        val param = hashMapOf(
            "mobile_number" to moneySender.mobileNumber.orEmpty(),
            "bank_account" to input.accountNumber.getValue(),
            "ifsc" to input.ifscCode.getValue(),
            "bank_name" to selectedBank.value!!.bankName.orEmpty()
        )
        callApiForShareFlow(
            flow = _validationResultFlow,
            call = { repository.accountValidation(param) }
        )

    }

    fun onBankSelect(bankName: String) {
        selectedBank.value = bankList.find { it.bankName == bankName }
    }

    fun customValidation(): Boolean {
        val result = BaseInput(input.ifscCode, input.accountNumber).isValidObs.value
        return result && selectedBank.value != null
    }

    fun registerBeneficiary() {
        val param = hashMapOf(
            "sender_number" to moneySender.mobileNumber.orEmpty(),
            "bene_name" to input.name.getValue(),
            "account_number" to input.accountNumber.getValue(),
            "bank_id" to "",
            "ifsc" to input.ifscCode.getValue(),
            "bank_name" to selectedBank.value?.bankName.orEmpty()
        )
        callApiForShareFlow(
            flow = _addBeneficiaryResultFlow,
            call = { repository.addBeneficiary(param) }
        )


    }


}

data class BeneficiaryRegisterInput(
    val ifscCode: InputWrapper = InputWrapper {
        Pair(
            it.length == 11,
            "Enter 11 Character Ifsc Code"
        )
    },
    val accountNumber: InputWrapper = InputWrapper { AppValidator.accountNumberValidation(it) },
    val name: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
) : BaseInput(ifscCode, accountNumber, name)