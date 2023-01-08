package com.a2z.app.ui.screen.dmt.sender.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.dmt.MoneySender
import com.a2z.app.data.model.dmt.MoneySenderResponse
import com.a2z.app.data.model.dmt.SenderAccountDetail
import com.a2z.app.data.model.dmt.SenderAccountDetailResponse
import com.a2z.app.data.repository.DMT3Repository
import com.a2z.app.data.repository.DMTRepository
import com.a2z.app.data.repository.UpiRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.screen.dmt.sender.register.SenderRegistrationArgs
import com.a2z.app.ui.screen.dmt.sender.register.SenderRegistrationType
import com.a2z.app.ui.screen.dmt.util.DMTType
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.safeSerializable
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchSenderViewModel @Inject constructor(
    private val repository: DMTRepository,
    private val dmt3Repository: DMT3Repository,
    private val upiRepository: UpiRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    var dmtType: DMTType = savedStateHandle.safeSerializable("dmtType")!!

    val searchType = mutableStateOf(SenderSearchType.MOBILE)

    val numberValidationLength = mutableStateOf(10)
    val input = SenderSearchInput(numberValidationLength)

    val senderBeneficiaries = mutableStateOf<List<SenderAccountDetail>>(listOf())

    private val senderMobileSearchFlow = resultShareFlow<MoneySenderResponse>()
    private val beneficiaryAccountSearchFlow = resultShareFlow<SenderAccountDetailResponse>()

    val senderAccountDetail = mutableStateOf<SenderAccountDetail?>(null)

    init {

        viewModelScope.launch {
            senderMobileSearchFlow.getLatest { onSenderMobileSearchResult(it) }
        }
        viewModelScope.launch {
            beneficiaryAccountSearchFlow.getLatest { onBeneficiaryAccountSearchResult(it) }
        }
    }

    private fun onBeneficiaryAccountSearchResult(it: SenderAccountDetailResponse) {
        if (it.status != 1) failureDialog(it.message)
        else it.senderAccountDetail?.let { senderBeneficiaries.value = it }
    }

    private fun onSenderMobileSearchResult(it: MoneySenderResponse) {
        it.moneySender?.mobileNumber = input.number.getValue()
        val moneySender = it.moneySender ?: MoneySender(mobileNumber = input.number.getValue())
        when (it.status) {
            13 -> {
                navigateTo(
                    NavScreen.DmtBeneficiaryListInfoScreen.passArgs(
                        it.moneySender!!,
                        dmtType,
                        senderAccountDetail.value ?: SenderAccountDetail(
                            "", "", "", "",
                            "", "", "", "",
                            "",
                        )
                    )
                )
                senderAccountDetail.value = null
            }
            11, 111 -> {
                navigateTo(
                    NavScreen.DmtSenderRegisterScreen.passArgs(
                        args = SenderRegistrationArgs(
                            state = it.state,
                            registrationType = SenderRegistrationType.NEW_REGISTER,
                            moneySender = moneySender,
                            dmtType = dmtType
                        )
                    )
                )
            }
            12 -> {
                navigateTo(
                    NavScreen.DmtSenderRegisterScreen.passArgs(
                        args = SenderRegistrationArgs(
                            state = it.state,
                            registrationType = SenderRegistrationType.VERIFY_AND_UPDATE,
                            moneySender = moneySender,
                            dmtType = dmtType
                        )
                    )
                )
            }
            else -> {
                failureDialog(it.message)
            }
        }
    }

    fun onSearchTypeClick(value: SenderSearchType) {
        this.searchType.value = value

        if (this.searchType.value == SenderSearchType.MOBILE)
            senderBeneficiaries.value = listOf()

        numberValidationLength.value =
            if (searchType.value == SenderSearchType.MOBILE) 10 else 20
        if (input.number.getValue().isNotEmpty()) input.number.setValue("")
    }


    fun onSearchClick() {
        if (searchType.value == SenderSearchType.MOBILE)
            senderMobileSearch()
        else beneficiaryAccountSearch()

    }

    val inputLabel: String
        get() = if (searchType.value == SenderSearchType.MOBILE)
            "Mobile Number" else "Account Number"

    private fun senderMobileSearch() {


        val mobileNumber = input.number.getValue()
        val param = hashMapOf(
            "mobile_number" to mobileNumber,
            "mobile" to mobileNumber
        )
        callApiForShareFlow(
            flow = senderMobileSearchFlow,
            call = {
                when (dmtType) {
                    DMTType.WALLET_1 -> repository.searchMobileNumberWalletOne(param)
                    DMTType.WALLET_2 -> repository.searchMobileNumberWalletTwo(param)
                    DMTType.WALLET_3 -> repository.searchMobileNumberWalletThree(param)
                    DMTType.DMT_3 -> dmt3Repository.searchMobileNumberDmtThree(param)
                    DMTType.UPI ->  upiRepository.searchSender(param).body()!!
                }
            }
        )
    }

    private fun beneficiaryAccountSearch() {
        senderBeneficiaries.value = listOf()
        val param = hashMapOf("account_number" to input.number.getValue())
        callApiForShareFlow(
            call = { repository.searchAccountNumber(param) },
            flow = beneficiaryAccountSearchFlow
        )
    }

    fun getAvailableBalance(it: SenderAccountDetail): String? {
        return when (dmtType) {
            DMTType.WALLET_1 -> it.remainingLimit
            DMTType.WALLET_2 -> it.remainingLimit2
            DMTType.WALLET_3 -> it.remainingLimit3
            else -> null
        }
    }

    fun onWalletSelect(dmtType: DMTType?= null) {
        dmtType?.let { this.dmtType = it }
        searchType.value = SenderSearchType.MOBILE
        input.number.setValue(senderAccountDetail.value?.mobileNumber.toString())
        senderBeneficiaries.value = listOf()
        onSearchClick()
    }


}


data class SenderSearchInput(
    val numberValidationLength: MutableState<Int>,
    val number: InputWrapper = InputWrapper {
        if (numberValidationLength.value == 10)
            AppValidator.mobileValidation(it)
        else AppValidator.accountNumberValidation(it)
    }
) : BaseInput(number)

enum class SenderSearchType {
    MOBILE, ACCOUNT
}