package com.a2z.app.ui.screen.aeps

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.aeps.AepsBank
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.aeps.RDService
import com.a2z.app.data.repository.AepsRepository
import com.a2z.app.data.repository.TransactionRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AepsViewModel @Inject constructor(
    private val repository: AepsRepository,
    private val appPreference: AppPreference,
    private val transactionRepository : TransactionRepository
) : BaseViewModel() {

    lateinit var biometricDevice: RDService
    val transactionType = mutableStateOf(AepsTransactionType.CASH_WITHDRAWAL)
    var spinnerDialogState = mutableStateOf(false)
    var selectedBank = mutableStateOf<AepsBank?>(null)

    private val useMobileValidation = mutableStateOf(true)
    val input = AepsInput(useMobileValidation)
    private val isFormValid = mutableStateOf(false)


    private val _bankListResponseFlow = resultStateFlow<AepsBankListResponse>()
    val bankListResponseFlow = _bankListResponseFlow.asStateFlow()

    var bankList: List<AepsBank>? = null
    var pidData = ""
    val showConfirmDialogState = mutableStateOf(false)

    private val _transactionResultFlow = resultShareFlow<AepsTransaction>()

    init {
        fetchBankList()
        viewModelScope.launch {
            _transactionResultFlow.getLatest {

            }
        }
    }

    private fun setFormValid() {

        val amountValidation = transactionType.value == AepsTransactionType.CASH_WITHDRAWAL ||
                transactionType.value == AepsTransactionType.AADHAAR_PAY
        useMobileValidation.value = amountValidation

        isFormValid.value = input.isValidObs.value && selectedBank.value != null

    }

    fun setTransactionType(type: AepsTransactionType) {
        transactionType.value = type
        setFormValid()
    }


    private fun fetchBankList() {
        callApiForShareFlow(flow = _bankListResponseFlow) {
            repository.fetchBankList()
        }
    }

    fun getAepsStringBankList(): ArrayList<String> {
        return (bankList?.map { it.bankName ?: "" }?.toList()
            ?: arrayListOf()) as ArrayList<String>
    }

    fun bankNameToAepsBank(bankName: String): AepsBank? {
        val aepsBank = bankList?.first { it.bankName == bankName }
        return aepsBank

    }

    fun onBankChange(value: String) {
        selectedBank.value = bankNameToAepsBank(value)
        setFormValid()
    }

    val transactionTypeText: String
        get() = when (transactionType.value) {
            AepsTransactionType.CASH_WITHDRAWAL -> "Cash Withdrawal"
            AepsTransactionType.BALANCE_ENQUIRY -> "Balance Enquiry"
            AepsTransactionType.MINI_STATEMENT -> "Mini Statement"
            AepsTransactionType.AADHAAR_PAY -> "Aadhaar Pay"
        }

    private fun getTransactionTypeParam() = when (transactionType.value) {
        AepsTransactionType.BALANCE_ENQUIRY -> "BE"
        AepsTransactionType.MINI_STATEMENT -> "MS"
        AepsTransactionType.CASH_WITHDRAWAL -> "CW"
        AepsTransactionType.AADHAAR_PAY -> "AADHAAR_PAY"
    }

    fun onBiometricResult(pidData: String) {
        this.pidData = pidData
        showConfirmDialogState.value = true
    }

    fun onConfirmTransaction() {

        val param = hashMapOf(
            "customerNumber" to input.mobileInputWrapper.getValue(),
            "bankName" to selectedBank.value!!.bankInnNumber.toString(),
            "selectedBankName" to selectedBank.value!!.bankName.toString(),
            "transactionType" to getTransactionTypeParam(),
            "txtPidData" to pidData,
            "amount" to input.amountInputWrapper.getValue(),
            "deviceName" to biometricDevice.deviceName,
            "aadhaarNumber" to input.aadhaarInputWrapper.getValue(),
            "latitude" to appPreference.latitude,
            "longitude" to appPreference.longitude,
        )

        callApiForShareFlow(
            flow = _transactionResultFlow,
            call = {transactionRepository.aepsTransaction(param)}
        )
    }
}

enum class AepsTransactionType {
    CASH_WITHDRAWAL, BALANCE_ENQUIRY, MINI_STATEMENT, AADHAAR_PAY
}