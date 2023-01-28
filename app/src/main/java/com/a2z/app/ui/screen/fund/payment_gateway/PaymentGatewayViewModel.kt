package com.a2z.app.ui.screen.fund.payment_gateway

import android.util.Base64
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
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
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.Credentials
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PaymentGatewayViewModel @Inject constructor(
    private val repository: FundRepository,
    private val appPreference: AppPreference
) : BaseViewModel() {


    val input = FormInput()

    private val _initiateRequestResponse = resultShareFlow<PaymentGatewayInitiateResponse>()
    var initiateData: PaymentGatewayInitiateData? = null

    val initiatePaymentFlow = MutableSharedFlow<JSONObject>()


    init {

        _initiateRequestResponse.getLatest {
            if (it.status == 1) {
                initiateData = it.data


                val options = JSONObject()
                options.put("name", "A2ZSuvidhaa")
                options.put("theme.color", "#3399cc");
                options.put("currency", "INR");
                options.put("receipt", it.data?.ackNo.toString());
                options.put("amount", input.amount.getValue())

               /*  val retryObj = JSONObject();
                 retryObj.put("enabled", true);
                 retryObj.put("max_count", 4);
                 options.put("retry", retryObj);*/

                 val prefill = JSONObject()
                 prefill.put("email", appPreference.user?.email.toString())
                 prefill.put("contact", input.mobile.getValue())

                options.put("prefill",prefill)

                initiatePaymentFlow.emit(options)
            } else alertDialog(it.message)
        }

    }



    fun initiateTransaction() {

        val param = hashMapOf(
            "amount" to input.amount.getValue(), "customer_mobile" to input.mobile.getValue()
        )
        callApiForShareFlow(_initiateRequestResponse) {
            repository.initiatePaymentGatewayRequest(
                param
            )
        }

    }

    data class FormInput(
        val amount: InputWrapper = InputWrapper { AppValidator.amountValidation(it) },
        val mobile: InputWrapper = InputWrapper { AppValidator.mobile(it) }
    ) : BaseInput(amount, mobile)
}