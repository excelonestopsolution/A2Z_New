package com.a2z.app.ui.screen.auth.change.pin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.repository.AuthRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.security.AppSecurity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePinViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val appPreference: AppPreference
) : BaseViewModel() {


    private val otpValidation = mutableStateOf(false)
    val input = ChangePinInput(otpValidation)
    val actionType = mutableStateOf(ChangePinActionType.REQUEST_OTP)
    val timerState = mutableStateOf(true)

    val buttonText: String
        get() = if (actionType.value == ChangePinActionType.REQUEST_OTP)
            "Request Otp" else "Change Pin"

    val isInputReadOnly: Boolean
        get() = actionType.value == ChangePinActionType.CHANGE_PIN

    private val _resendOtpResultFlow = resultShareFlow<AppResponse>()
    private val _requestOtpResultFlow = resultShareFlow<AppResponse>()
    private val _generateMPINResultFlow = resultShareFlow<AppResponse>()

    init {
        viewModelScope.launch {
            _resendOtpResultFlow.getLatest {
                if (it.status == 1) successBanner("Opt Resent", it.message)
                else failureBanner("Opt Resent", it.message)
            }
        }
        viewModelScope.launch {
            _requestOtpResultFlow.getLatest {
                if (it.status == 1) {
                    otpValidation.value = true
                    actionType.value = ChangePinActionType.CHANGE_PIN
                    successBanner("Generate New Pin", it.message)
                } else failureBanner("Failure", it.message)
            }
        }

        viewModelScope.launch {
            _generateMPINResultFlow.getLatest {
                if (it.status == 1) {
                    successDialog(it.message) {
                        navigateTo(NavScreen.DashboardScreen.route)
                    }
                } else failureDialog(it.message)
            }
        }
    }


    fun onProceed() {
        when (actionType.value) {
            ChangePinActionType.REQUEST_OTP -> requestChangePinOtp()
            ChangePinActionType.CHANGE_PIN -> changeMpin()
        }

    }

    private fun changeMpin() {
        val param = hashMapOf(
            "txn_pin" to input.txnPin.getValue(),
            "confirm_txn_pin" to input.confirmTxnPin.getValue(),
            "otp" to input.otp.getValue(),
        )
        callApiForShareFlow(
            flow = _generateMPINResultFlow,
            call = { repository.generateMPIN(param) }
        )


    }

    private fun requestChangePinOtp() {

        val param = hashMapOf("change-pin" to "change-pin")
        callApiForShareFlow(
            flow = _requestOtpResultFlow,
            call = { repository.requestChangeMPINOtp(param) }
        )

    }

    fun onResendOtp() {
        val mobile = AppSecurity.encrypt(appPreference.user?.mobile ?: "")
        val type = AppSecurity.encrypt("CHANGE_PIN")
        val param = hashMapOf(
            "mobileNumber" to mobile.toString(),
            "type" to type.toString(),
        )
        callApiForShareFlow(
            flow = _resendOtpResultFlow,
            call = { repository.resendMPINOtp(param) }
        )

    }


}

data class ChangePinInput(
    val otpValidation: MutableState<Boolean>,
    val txnPin: InputWrapper = InputWrapper { AppValidator.mpinValidation(it, length = 4) },
    val confirmTxnPin: InputWrapper = InputWrapper {
        AppValidator.confirmMpin(
            it,
            txnPin.getValue(),
            length = 4
        )
    },
    val otp: InputWrapper = InputWrapper (otpValidation){ AppValidator.otp(it, length = 4) }
) : BaseInput(txnPin, confirmTxnPin, otp)

enum class ChangePinActionType {
    REQUEST_OTP, CHANGE_PIN
}