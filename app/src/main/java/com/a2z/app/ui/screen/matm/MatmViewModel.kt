package com.a2z.app.ui.screen.matm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.matm.MaPosAmountLimitResponse
import com.a2z.app.data.repository.MatmRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.resource.ResultType
import com.mosambee.lib.Currency
import com.mosambee.lib.MosCallback
import com.mosambee.lib.ResultData
import com.mosambee.lib.TransactionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatmViewModel @Inject constructor(
    private val repository: MatmRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    private val transactionResult = object : TransactionResult{
        override fun onResult(p0: ResultData?) {

        }

        override fun onCommand(p0: String?) {

        }

    }
    val isMPos: Boolean = savedStateHandle.get<String>("isMPos").toBoolean()
    val transactionType = mutableStateOf(
        if (isMPos) MatmTransactionType.M_POS else MatmTransactionType.CASH_WITHDRAWAL
    )
    private val useMobileValidation = mutableStateOf(true)
    val formInput = MatmInput(useMobileValidation)
    private val isFormValid = mutableStateOf(false)

    private val mPosAmountLimitResponse
    = MutableSharedFlow<ResultType<MaPosAmountLimitResponse>>()

    val mPosLimit = mutableStateOf<MaPosAmountLimitResponse?>(null)

    init {
        if (isMPos) fetchMPosAmountLimit()

        viewModelScope.launch {
            mPosAmountLimitResponse.getLatest {
                mPosLimit.value = it
            }
        }
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


    fun getTransactionTypeParam() = when (transactionType.value) {
        MatmTransactionType.BALANCE_ENQUIRY -> "BE"
        MatmTransactionType.M_POS -> "MS"
        MatmTransactionType.CASH_WITHDRAWAL -> "CW"
    }

    fun getTitle(): String {
        return if (isMPos) "M-Pos Transaction" else "M-Atm Transaction"
    }

    fun transaction(mosCallback : MosCallback) {

        mosCallback.initialise(
            "usename",
            "password",
            transactionResult
        )
        mosCallback.initialiseFields(
            "transactionType",
            //viewModel.mobileNumber,
            "na",
            "appKey",
            false,
            "",
            "",
            "bt",
            "",
            "0"
        )
        mosCallback.processTransaction(
            "txnId",
            "txnId",
            0.0,
            0.0,
            "",
            Currency.INR
        )
    }
}

enum class MatmTransactionType {
    CASH_WITHDRAWAL, BALANCE_ENQUIRY, M_POS
}