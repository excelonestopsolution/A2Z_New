package com.a2z.app.ui.screen.fund.upi_payment

import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.fund.UpiPaymentInitiateResponse
import com.a2z.app.data.repository.FundRepository
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class UpiPaymentViewModel @Inject constructor(
    private val repository: FundRepository,
    val appPreference: AppPreference
) : BaseViewModel() {

    val input =FormInput()
    private val _initiateResultFlow = resultShareFlow<UpiPaymentInitiateResponse>()
    val onInitiateSuccessResult = MutableSharedFlow<String>()

    init {
        _initiateResultFlow.getLatest {
            if(it.status == 1){
                onInitiateSuccessResult.emit(it.refId.toString())
            }else failureDialog(it.message)
        }
    }

    fun fetchData() {
        val param = hashMapOf("amount" to input.amount.getValue())
        callApiForShareFlow(_initiateResultFlow) {
            repository.initiateUpiPayment(param)
        }
    }

    data class FormInput(
        val amount: InputWrapper = InputWrapper {
            AppValidator.amountValidation(
                it,
                minAmount = 1.0
            )
        }
    ) : BaseInput(amount)
}