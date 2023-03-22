package com.a2z.app.ui.screen.aeps

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.aeps.*
import com.a2z.app.data.repository.AepsRepository
import com.a2z.app.data.repository.TransactionRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.service.firebase.FBAppLog
import com.a2z.app.service.firebase.FirebaseDatabase
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
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

    val aepsType = mutableStateOf(AepsType.ICICI)

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
    private val _aepsLimitResultFlow = resultShareFlow<AepsLimitResponse>()

    var aepsLimit by mutableStateOf<AepsLimit?>(null)

    private var checkStatusCountTotal = 0
    private var checkStatusCount = 0

    val title: String
        get() = when (aepsType.value) {
            AepsType.ICICI -> "AEPS - ICICI"
            AepsType.FINO -> "AEPS - FINO"
            AepsType.PAYTM -> "AEPS - PAYTM"
        }

    init {
        fetchBankList()
        viewModelScope.launch {
            _transactionResultFlow.collectLatest {
                when (it) {
                    is ResultType.Failure -> {
                        if (transactionType.value == AepsTransactionType.MINI_STATEMENT
                            || transactionType.value == AepsTransactionType.BALANCE_ENQUIRY
                        ) {
                            failureDialog("Something went wrong, please try again")
                        } else
                            pendingDialog("Transaction in pending, please check report for more details") {
                                navigateTo(NavScreen.DashboardScreen.route, true)
                            }
                    }
                    is ResultType.Loading -> transactionProgressDialog()
                    is ResultType.Success -> transactionResult(it.data)

                }
            }
        }

        viewModelScope.launch {
            _tableCheckStatusResultFlow.collectLatest {
                when (it) {
                    is ResultType.Failure -> navigateToResultScreen()
                    is ResultType.Loading -> {}
                    is ResultType.Success -> {
                        when (it.data.status) {
                            1, 2 -> navigateToResultScreen(it.data)
                            11, 33 -> {
                                if (checkStatusCount < 6) {
                                    if (checkStatusCountTotal < 22) checkStatusForTransaction()
                                    else navigateToResultScreen()
                                } else checkStatusForTransactionFromBank()
                            }
                            else -> navigateToResultScreen()
                        }
                    }
                }
            }
        }
        viewModelScope.launch {
            _bankCheckStatusResultFlow.collectLatest {
                when (it) {
                    is ResultType.Failure -> navigateToResultScreen()
                    is ResultType.Loading -> {}
                    is ResultType.Success -> {
                        if (it.data.status == 503) navigateToResultScreen()
                        else checkStatusForTransaction()
                    }
                }
            }
        }

        _aepsLimitResultFlow.getLatest {
            aepsLimit = it.data
        }
    }

    private fun transactionResult(data: AepsTransaction) {
        transactionResponse = data
        val status = transactionResponse.status
        val payId = transactionResponse.pay_type

        if (status == 3 && payId == "58") checkStatusForTransaction()
        else if (status == 3 || status == 2 || status == 1) navigateToResultScreen()
        else {
            dismissDialog()
            alertDialog(transactionResponse.message)
        }
    }

    private fun fetchAepsLimit(iin : String) {

        callApiForShareFlow(_aepsLimitResultFlow) {
            repository.aepsLimit(
                hashMapOf(
                    "iin" to iin
                )
            )
        }
    }

    private fun checkStatusForTransaction() {

        checkStatusCount += 1
        checkStatusCountTotal += 1
        val txnId = transactionResponse.record_id.toString()
        val param = hashMapOf("recordId" to txnId)

        callApiForShareFlow(_tableCheckStatusResultFlow,
            beforeEmit = {

                val apiName = "aeps/three/table-check-status"

                val db = FirebaseDatabase()
                if (it is ResultType.Success) {
                    db.insertLog(
                        FBAppLog(
                            logs = it.data.toString(),
                            apiName = apiName,
                            isSuccess = true
                        )
                    )
                } else if (it is ResultType.Failure) {
                    db.insertLog(
                        FBAppLog(
                            logs = it.exception.message.toString(),
                            apiName = apiName,
                            isSuccess = false
                        )
                    )
                }


            }) {
            delay(4000)
            repository.tableCheckStatus(param)
        }

    }

    private fun checkStatusForTransactionFromBank() {

        checkStatusCount = 0
        checkStatusCountTotal += 1

        val txnId = transactionResponse.record_id.toString()
        val param = hashMapOf("record_id" to txnId)

        callApiForShareFlow(_bankCheckStatusResultFlow,
            beforeEmit = {

                val apiName = "aeps/three/checkstatus"

                val db = FirebaseDatabase()
                if (it is ResultType.Success) {
                    db.insertLog(
                        FBAppLog(
                            logs = it.data.toString(),
                            apiName = apiName,
                            isSuccess = true
                        )
                    )
                } else if (it is ResultType.Failure) {
                    db.insertLog(
                        FBAppLog(
                            logs = it.exception.message.toString(),
                            apiName = apiName,
                            isSuccess = false
                        )
                    )
                }


            }) { repository.bankCheckStatus(param) }

    }

    private fun navigateToResultScreen(data: AepsTransaction? = null) {
        dismissDialog()
        val mData = data ?: transactionResponse

        val isWithdrawal =  transactionType.value == AepsTransactionType.CASH_WITHDRAWAL
                || transactionType.value == AepsTransactionType.AADHAAR_PAY


        navigateTo(
            NavScreen.AEPSTxnScreen.passArgs(
                response = mData.apply {
                    isTransaction = isWithdrawal

                },
            )
        )
    }

    private fun setFormValid() {

        val amountValidation =
            transactionType.value == AepsTransactionType.CASH_WITHDRAWAL || transactionType.value == AepsTransactionType.AADHAAR_PAY
        useMobileValidation.value = amountValidation

        isFormValid.value = input.isValidObs.value && selectedBank.value != null

    }

    fun setTransactionType(type: AepsTransactionType) {
        transactionType.value = type

        if (transactionType.value == AepsTransactionType.MINI_STATEMENT ||
            transactionType.value == AepsTransactionType.BALANCE_ENQUIRY
        ) {
            input.amountInputWrapper.setValue("0")
        }
        setFormValid()
    }

    private fun fetchBankList() {
        callApiForShareFlow(flow = _bankListResponseFlow) {
            repository.fetchBankList()
        }
    }

    fun getAepsStringBankList(): ArrayList<String> {
        return (bankList?.map { it.bankName ?: "" }?.toList() ?: arrayListOf()) as ArrayList<String>
    }

    private fun bankNameToAepsBank(bankName: String): AepsBank? {
        return bankList?.first { it.bankName == bankName }

    }

    fun onBankChange(value: String) {
        selectedBank.value = bankNameToAepsBank(value)
        setFormValid()
        fetchAepsLimit(selectedBank.value?.bankInnNumber.toString())
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

    private val isCashTransaction: Boolean
        get() = when (transactionType.value) {
            AepsTransactionType.CASH_WITHDRAWAL -> true
            AepsTransactionType.BALANCE_ENQUIRY -> false
            AepsTransactionType.MINI_STATEMENT -> false
            AepsTransactionType.AADHAAR_PAY -> true
        }

    fun onConfirmTransaction() {


        val amount = if (isCashTransaction) input.amountInputWrapper.getValue()
        else "0"
        checkStatusCountTotal = 0
        checkStatusCount = 0

        val param = hashMapOf(
            "customerNumber" to input.mobileInputWrapper.getValue(),
            "bankName" to selectedBank.value!!.bankInnNumber,
            "selectedBankName" to selectedBank.value!!.bankName.toString(),
            "transactionType" to getTransactionTypeParam(),
            "txtPidData" to pidData,
            "amount" to amount,
            "deviceName" to biometricDevice.deviceName,
            "aadhaarNumber" to input.aadhaarInputWrapper.getValue(),
            "latitude" to appPreference.latitude,
            "longitude" to appPreference.longitude,
        )

        val call = suspend {
            when (aepsType.value) {
                AepsType.ICICI -> transactionRepository.aeps1Transaction(param)
                AepsType.FINO -> transactionRepository.aeps2Transaction(param)
                AepsType.PAYTM -> transactionRepository.aeps3Transaction(param)
            }
        }
        callApiForShareFlow(
            flow = _transactionResultFlow, call = call,
        )
    }

    fun onAepsSelect(it: AepsType) {
        aepsType.value = it
    }
}

enum class AepsTransactionType {
    CASH_WITHDRAWAL, BALANCE_ENQUIRY, MINI_STATEMENT, AADHAAR_PAY
}

enum class AepsType {
    ICICI, FINO, PAYTM
}