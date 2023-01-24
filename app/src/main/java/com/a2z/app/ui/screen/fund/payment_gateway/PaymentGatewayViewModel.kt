package com.a2z.app.ui.screen.fund.payment_gateway

import com.a2z.app.data.model.fund.PaymentGatewayInitiateData
import com.a2z.app.data.model.fund.PaymentGatewayInitiateResponse
import com.a2z.app.data.repository.FundRepository
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultShareFlow
import com.razorpay.Checkout
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentGatewayViewModel @Inject constructor(
    private val repository: FundRepository
) : BaseViewModel() {


    val input = FormInput()

    private val _initiateRequestResponse = resultShareFlow<PaymentGatewayInitiateResponse>()
    var initiateData : PaymentGatewayInitiateData? = null

    init {
        
        _initiateRequestResponse.getLatest {
            if(it.status ==1){
                initiateData = it.data
            }else alertDialog(it.message)
        }
    }


    fun initiateTransaction() {

        val param = hashMapOf(
            "amount" to input.amount.getValue(),
            "customer_mobile" to input.mobile.getValue()
        )
        callApiForShareFlow (_initiateRequestResponse)
        { repository.initiatePaymentGatewayRequest(param) }

    }

    data class FormInput(
        val amount : InputWrapper = InputWrapper{AppValidator.amountValidation(it)},
        val mobile : InputWrapper = InputWrapper{AppValidator.mobile(it)}
    ) : BaseInput(amount,mobile)
}