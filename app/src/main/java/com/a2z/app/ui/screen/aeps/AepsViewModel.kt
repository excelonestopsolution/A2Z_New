package com.a2z.app.ui.screen.aeps

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.aeps.AepsBank
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.aeps.RDService
import com.a2z.app.data.repository.AepsRepository
import com.a2z.app.data.repository.TransactionRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.safeSerializable
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AepsViewModel @Inject constructor(
    private val repository: AepsRepository,
    private val appPreference: AppPreference,
    private val transactionRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val aepsType: AepsType = savedStateHandle.safeSerializable("aeps_type")!!

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

    lateinit var transactionResponse: AepsTransaction

    private val _transactionResultFlow = resultShareFlow<AepsTransaction>()
    private val _tableCheckStatusResultFlow = resultShareFlow<AepsTransaction>()
    private val _bankCheckStatusResultFlow = resultShareFlow<AepsTransaction>()


    private var checkStatusCountTotal = 0
    private var checkStatusCount = 0

    val title = when (aepsType) {
        AepsType.AEPS_1 -> "AEPS - 1"
        AepsType.AEPS_3 -> "AEPS - 3"
    }

    init {
        fetchBankList()
        viewModelScope.launch {
            _transactionResultFlow.collectLatest {
                when (it) {
                    is ResultType.Failure -> {
                        if (transactionType.value == AepsTransactionType.MINI_STATEMENT ||
                            transactionType.value == AepsTransactionType.BALANCE_ENQUIRY
                        ) {
                            failureDialog("Something went wrong! please try again.")
                        } else pendingDialog("Transaction in pending! please check aeps report") {
                            dismissDialog()
                            navigateTo(NavScreen.DashboardScreen.route, true)
                        }
                    }
                    is ResultType.Loading -> transactionProgressDialog()
                    is ResultType.Success -> transactionResult(it.data)

                }
            }

            _tableCheckStatusResultFlow.collectLatest {
                when (it) {
                    is ResultType.Failure -> navigateToResultScreen()
                    is ResultType.Loading -> {}
                    is ResultType.Success -> {
                        when (it.data.status) {
                            1, 2 -> navigateToResultScreen(it.data)
                            11, 33 -> {
                                if (checkStatusCount < 6) {
                                    if (checkStatusCountTotal < 22)
                                        checkStatusForTransaction()
                                    else navigateToResultScreen()
                                } else checkStatusForTransactionFromBank()
                            }
                            else -> navigateToResultScreen()
                        }
                    }
                }
            }

            _bankCheckStatusResultFlow.collectLatest {
                when (it) {
                    is ResultType.Failure -> navigateToResultScreen()
                    is ResultType.Loading -> {}
                    is ResultType.Success -> {
                        if (it.data.status == 503)
                            navigateToResultScreen()
                        else checkStatusForTransaction()
                    }
                }
            }
        }
    }

    private fun transactionResult(data: AepsTransaction) {
        transactionResponse = data
        val status = transactionResponse.status
        val payId = transactionResponse.pay_type

        if (status == 3 && payId == "58") checkStatusForTransaction()
        else if (status == 3 || status == 2 || status == 1)
            navigateToResultScreen()
        else {
            dismissDialog()
            alertDialog(transactionResponse.message)
        }
    }


    private fun checkStatusForTransaction() {

        checkStatusCount += 1
        checkStatusCountTotal += 1
        val txnId = transactionResponse.txn_id.toString()
        val param = hashMapOf("recordId" to txnId)

        callApiForShareFlow(_tableCheckStatusResultFlow) {
            delay(4000)
            repository.tableCheckStatus(param)
        }

    }

    private fun checkStatusForTransactionFromBank() {

        checkStatusCount = 0
        checkStatusCountTotal += 1

        val txnId = transactionResponse.record_id.toString()
        val param = hashMapOf("record_id" to txnId)

        callApiForShareFlow(_bankCheckStatusResultFlow) { repository.bankCheckStatus(param) }

    }

    private fun navigateToResultScreen(data: AepsTransaction? = null) {
        dismissDialog()
        val mData = data ?: transactionResponse
        navigateTo(
            NavScreen.AEPSTxnScreen.passArgs(
                response = mData.apply {
                    isTransaction = transactionType.value == AepsTransactionType.CASH_WITHDRAWAL ||
                            transactionType.value == AepsTransactionType.AADHAAR_PAY
                },
            )
        )

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

    private fun bankNameToAepsBank(bankName: String): AepsBank? {
        return bankList?.first { it.bankName == bankName }

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

        checkStatusCountTotal = 0
        checkStatusCount = 0

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

        val call = suspend {
            when (aepsType) {
                AepsType.AEPS_1 -> transactionRepository.aepsTransaction(param)
                AepsType.AEPS_3 -> transactionRepository.aeps3Transaction(param)
            }
        }

        callApiForShareFlow(
            flow = _transactionResultFlow,
            call = call
        )
    }
}

enum class AepsTransactionType {
    CASH_WITHDRAWAL, BALANCE_ENQUIRY, MINI_STATEMENT, AADHAAR_PAY
}

enum class AepsType {
    AEPS_1, AEPS_3
}