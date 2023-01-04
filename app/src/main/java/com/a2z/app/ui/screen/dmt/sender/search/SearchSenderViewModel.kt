package com.a2z.app.ui.screen.dmt.sender.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.dmt.MoneySenderResponse
import com.a2z.app.data.model.dmt.SenderAccountDetail
import com.a2z.app.data.model.dmt.SenderAccountDetailResponse
import com.a2z.app.data.repository.DMTRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchSenderViewModel @Inject constructor(
    private val repository: DMTRepository
) : BaseViewModel() {


    val searchType = mutableStateOf(SenderSearchType.MOBILE)

    val numberValidationLength = mutableStateOf(10)
    val input = SenderSearchInput(numberValidationLength)

    val senderBeneficiaries = mutableStateOf<List<SenderAccountDetail>>(listOf())

    private val senderMobileSearchFlow = resultShareFlow<MoneySenderResponse>()
    private val beneficiaryAccountSearchFlow = resultShareFlow<SenderAccountDetailResponse>()


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
        when (it.status) {
            13 -> {
                navigateTo(NavScreen.DmtBeneficiaryListInfoScreen.passArgs(it.moneySender!!))
            }
            11, 111 -> {

            }
            12 -> {

            }
            else -> {
                failureDialog(it.message)
            }
        }
    }

    fun onSearchTypeClick(searchType: SenderSearchType) {

        if (searchType == SenderSearchType.MOBILE) senderBeneficiaries.value = listOf()

        numberValidationLength.value = if (searchType == SenderSearchType.MOBILE) 10 else 20
        if (input.number.getValue().isNotEmpty()) input.number.setValue("")
        this.searchType.value = searchType
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
            call = { repository.searchMobileNumberWalletOne(param) }
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