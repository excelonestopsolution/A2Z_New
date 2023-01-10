package com.a2z.app.ui.screen.matm

import androidx.compose.runtime.mutableStateOf
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.matm.*
import com.a2z.app.data.repository.MatmRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultShareFlow
import com.mosambee.lib.Currency
import com.mosambee.lib.MosCallback
import com.mosambee.lib.ResultData
import com.mosambee.lib.TransactionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MatmViewModel @Inject constructor(
    private val repository: MatmRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    private lateinit var initiateMatm: MatmInitiate
    lateinit var mosCallback: MosCallback
    private val transactionResult = object : TransactionResult {
        override fun onResult(data: ResultData?) {
            if (data != null) {
                if (data.result) postResultData(data.transactionData)
                else {
                    val reasonMessage = data.reason.orEmpty()
                    val mData = """ {
                         "orderId" : "${initiateMatm.transactionId}",
                         "transactionStatus" : "$reasonMessage",
                         "statusCode" : "5500",
                         "errorCode" : "${data.reasonCode}",
                         "result": "failure"} """.trimIndent()
                    postResultData(JSONObject(mData).toString())
                }
            } else {
                val mData = """ {
                        "orderId" : "${initiateMatm.transactionId}",
                        "statusCode" : "5501"} """.trimIndent()
                postResultData(mData)
            }
        }

        override fun onCommand(p0: String?) {
            progressDialog(p0.toString())
        }
    }

    private fun postResultData(transactionData: String) {
        callApiForShareFlow(
            flow = _postMatmResultFlow,
            call = { repository.postResultData(transactionData) }
        )
    }

    val isMPos: Boolean = savedStateHandle.get<String>("isMPos").toBoolean()
    val transactionType = mutableStateOf(
        if (isMPos) MatmTransactionType.M_POS else MatmTransactionType.CASH_WITHDRAWAL
    )
    private val useMobileValidation = mutableStateOf(true)
    val formInput = MatmInput(useMobileValidation)
    private val isFormValid = mutableStateOf(false)

    private val mPosAmountLimitResponse = resultShareFlow<MaPosAmountLimitResponse>()
    private val _postMatmResultFlow = resultShareFlow<MatmPostResponse>()
    private val _checkStatusResultFlow = resultShareFlow<MatmTransactionResponse>()
    val mPosLimit = mutableStateOf<MaPosAmountLimitResponse?>(null)

    private val mamtInitiateResultFlow = resultShareFlow<MatmInitiateResponse>()
    private var matmTransactionResponse: MatmTransactionResponse? = null

    init {
        if (isMPos) fetchMPosAmountLimit()

        viewModelScope.launch {
            mPosAmountLimitResponse.getLatest {
                mPosLimit.value = it
            }
        }

        viewModelScope.launch {
            mamtInitiateResultFlow.getLatest {
                if (it.status == 1) {
                    initiateMatm = it.data!!
                    transaction()
                } else alertDialog(it.message)
            }
        }

        viewModelScope.launch {
            _postMatmResultFlow.getLatest {
                if (it.status == 1) {
                    when (it.data!!.status) {
                        1, 2 -> {
                            navigateTo(
                                NavScreen.MATMTxnScreen.passArgs(
                                    response = it.data
                                )
                            )
                        }
                        3, 34 -> {
                            matmTransactionResponse = it.data
                            checkStatus()
                        }
                        else -> pendingDialog("Transaction in progress! please check report") {
                            navigateTo(NavScreen.DashboardScreen.route, true)
                        }
                    }
                } else {
                    pendingDialog("Transaction in progress! please check report") {
                        navigateTo(NavScreen.DashboardScreen.route, true)
                    }
                }
            }
        }

        viewModelScope.launch {
            _checkStatusResultFlow.getLatest {
                when (it.status) {
                    1, 2 -> {
                        navigateTo(
                            NavScreen.MATMTxnScreen.passArgs(
                                response = it
                            )
                        )
                    }
                    11, 3, 34 -> {
                        checkStatus()
                    }
                    else -> {
                        pendingDialog("Transaction in pending! please check report") {
                            navigateTo(NavScreen.DashboardScreen.route)
                        }
                    }
                }
            }
        }
    }

    var checkStatusCount: Int = 0
    private fun checkStatus() {
        if (checkStatusCount < 3) {
            doCheckStatus()
        } else {
            navigateTo(
                NavScreen.MATMTxnScreen.passArgs(
                    response = matmTransactionResponse!!
                )
            )
        }

    }

    private fun doCheckStatus() {
        checkStatusCount++
        callApiForShareFlow(
            flow = _checkStatusResultFlow,
            call = {
                delay(5000)
                repository.checkStatus(matmTransactionResponse!!.recordId!!)
            }
        )
    }


    private fun fetchMPosAmountLimit() {
        callApiForShareFlow(mPosAmountLimitResponse) { repository.salemAmountLimit() }
    }


    private fun setFormValid() {

        val amountValidation = transactionType.value == MatmTransactionType.CASH_WITHDRAWAL ||
                transactionType.value == MatmTransactionType.M_POS
        useMobileValidation.value = amountValidation

        isFormValid.value = formInput.isValidObs.value

    }

    fun setTransactionType(type: MatmTransactionType) {
        transactionType.value = type
        setFormValid()
    }


    private fun getTransactionTypeParam() = when (transactionType.value) {
        MatmTransactionType.BALANCE_ENQUIRY -> "BALANCE_ENQUIRY"
        MatmTransactionType.M_POS -> "SALE"
        MatmTransactionType.CASH_WITHDRAWAL -> "CASH_WITHDRAWAL"
    }

    private fun getTransactionTypeSDK(): String {
        return when (transactionType.value) {
            MatmTransactionType.CASH_WITHDRAWAL -> "CASH WITHDRAWAL"
            MatmTransactionType.BALANCE_ENQUIRY -> "BALANCE ENQUIRY"
            MatmTransactionType.M_POS -> "SALE"
        }
    }

    fun getTitle(): String {
        return if (isMPos) "M-POS Transaction" else "M-ATM Transaction"
    }

    fun initTransaction() {
        val amount = if (transactionType.value == MatmTransactionType.BALANCE_ENQUIRY)
            "0" else formInput.amountInputWrapper.getValue()
        val param = hashMapOf(
            "customerMobile" to formInput.mobileInputWrapper.getValue(),
            "transactionType" to getTransactionTypeParam(),
            "amount" to amount
        )

        callApiForShareFlow(
            flow = mamtInitiateResultFlow,
            call = {
                if (isMPos) repository.initiateMPosTransaction(param)
                else repository.initiateMatmTransaction(param)
            }
        )
    }

    fun transaction() {

        val amount = if (transactionType.value == MatmTransactionType.BALANCE_ENQUIRY)
            "0" else formInput.amountInputWrapper.getValue()

        mosCallback.initialise(
            initiateMatm.username,
            initiateMatm.password,
            transactionResult
        )
        mosCallback.initialiseFields(
            getTransactionTypeSDK(),
            "na", initiateMatm.appKey, false, "",
            "", "bt", "", "0"
        )
        mosCallback.processTransaction(
            initiateMatm.transactionId,
            initiateMatm.transactionId,
            amount.toDouble(),
            0.0, "",
            Currency.INR
        )
    }
}

enum class MatmTransactionType {
    CASH_WITHDRAWAL, BALANCE_ENQUIRY, M_POS
}