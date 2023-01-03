package com.a2z.app.ui.screen.aeps

import androidx.compose.runtime.mutableStateOf
import com.a2z.app.data.model.aeps.AepsBank
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.repository.AepsRepository
import com.a2z.app.ui.screen.matm.MatmTransactionType
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.resource.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AepsViewModel @Inject constructor(
    private val repository: AepsRepository
) : BaseViewModel() {

    val transactionType = mutableStateOf(AepsTransactionType.CASH_WITHDRAWAL)
    var spinnerDialogState = mutableStateOf(false)
    var selectedBank = mutableStateOf<AepsBank?>(null)
    private val useMobileValidation = mutableStateOf(true)
    val aepsInput = AepsInput(useMobileValidation)
    val isFormValid = mutableStateOf(false)


    val aepsBankListResponse = MutableStateFlow<ResultType<AepsBankListResponse>>(
        ResultType.Loading()
    )

    var aepsBankList: List<AepsBank>? = null



    init {
        fetchBankList()
        aepsInput.isValidObs
    }

    private fun setFormValid(){

        val amountValidation = transactionType.value == AepsTransactionType.CASH_WITHDRAWAL ||
                transactionType.value == AepsTransactionType.AADHAAR_PAY
        useMobileValidation.value = amountValidation

        isFormValid.value = aepsInput.isValidObs.value  && selectedBank.value != null

    }

    fun setTransactionType(type : AepsTransactionType){
        transactionType.value = type
        setFormValid()
    }


    private fun fetchBankList() {
        callApiForShareFlow(flow = aepsBankListResponse) {
            repository.fetchBankList()
        }
    }

    fun getAepsStringBankList(): ArrayList<String> {
        return (aepsBankList?.map { it.bankName ?: "" }?.toList()
            ?: arrayListOf()) as ArrayList<String>
    }

    fun bankNameToAepsBank(bankName : String): AepsBank? {
        val aepsBank = aepsBankList?.first { it.bankName == bankName }
        return aepsBank

    }

    fun onBankChange(value: String) {
        selectedBank.value = bankNameToAepsBank(value)
        setFormValid()
    }

    fun getTransactionTypeParam() = when (transactionType.value) {
        AepsTransactionType.BALANCE_ENQUIRY -> "BE"
        AepsTransactionType.MINI_STATEMENT -> "MS"
        AepsTransactionType.CASH_WITHDRAWAL -> "CW"
        AepsTransactionType.AADHAAR_PAY -> "AADHAAR_PAY"
    }
}

enum class AepsTransactionType {
    CASH_WITHDRAWAL, BALANCE_ENQUIRY, MINI_STATEMENT, AADHAAR_PAY
}