package com.a2z.app.ui.screen.indonepal.transfer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.model.indonepal.*
import com.a2z.app.data.repository.IndoNepalRepository
import com.a2z.app.ui.screen.indonepal.INUtil
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.safeParcelable
import com.a2z.app.util.extension.nullOrEmptyToDouble
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class INTransferViewModel @Inject constructor(
    private val repository: IndoNepalRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    val beneficiary = savedStateHandle.safeParcelable<INBeneficiary>("beneficiary")!!
    val sender = savedStateHandle.safeParcelable<INSender>("sender")!!

    val isChargeFetched = mutableStateOf(false)
    val input = InputForm(isChargeFetched)
    val selectedReasonOfTransfer = mutableStateOf(INUtil.reasonOfTransferList.first())
    private val _serviceChargeResultFlow = resultShareFlow<INServiceChargeResponse>()

    var serviceCharge: INServiceCharge? = null

    private val _txnOtpResultFlow = resultShareFlow<INTxnOtpResponse>()
    private var txnProcessId  = ""

    init {
        _serviceChargeResultFlow.getLatest {
            if (it.status == 1) {
                successBanner("Service Charge", it.message)
                serviceCharge = it.data
                isChargeFetched.value = true
            } else alertDialog(it.message)
        }

        _txnOtpResultFlow.getLatest {
            if(it.status == 1){
                txnProcessId = it.data?.txnProcessId ?: ""
                successDialog(it.message)
            }
            else alertDialog(it.message)
        }
    }

    fun onTransfer() {
        if(txnProcessId.isEmpty()) {
            failureBanner("OTP Error","Otp didn't received successfully!")
            return
        }

    }

    fun fetchCharge() {

        callApiForShareFlow(_serviceChargeResultFlow) {
            repository.serviceCharge(
                hashMapOf(
                    "paymentMode" to beneficiary.paymentMode.toString(),
                    "sendAmount" to input.amount.getValue(),
                    "branchId" to beneficiary.bank_branch_id.toString()
                )
            )
        }

    }

    fun onAmountChange(it: String) {
        input.amount.onChange(it)
        if (serviceCharge != null) {
            isChargeFetched.value =
                serviceCharge!!.transferAmount.nullOrEmptyToDouble() == it.nullOrEmptyToDouble()
        }

    }

    fun sendOtp() {
        callApiForShareFlow (_txnOtpResultFlow){
            repository.txnOtp(
                hashMapOf(
                    "senderMobileNumber" to sender.mobile.toString(),
                    "type" to "SendTransaction",
                    "txnPaymentMode" to beneficiary.paymentMode.toString(),
                    "customerId" to sender.customerId.toString(),
                    "ReceiverId" to beneficiary.receiver_id.toString(),
                    "sendAmount" to input.amount.getValue(),
                )
            )
        }
    }


    data class InputForm(
        val otpValidate: MutableState<Boolean>,
        val amount: InputWrapper = InputWrapper {
            AppValidator.amountValidation(
                it,
                minAmount = 200.0,
                maxAmount = 49800.0
            )
        },
        val otp: InputWrapper = InputWrapper(otpValidate) { AppValidator.otp(it) }
    ) : BaseInput(amount, otp)
}