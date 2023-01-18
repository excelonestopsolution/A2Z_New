package com.a2z.app.ui.screen.auth.forget.password

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.auth.ForgotPassword
import com.a2z.app.data.repository.AuthRepository
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
class ForgotPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
) : BaseViewModel() {


    private val actionType = mutableStateOf(ActionType.REQUEST_OTP)
    val input = FormInput()

    val buttonText: String
        get() = when (actionType.value) {
            ActionType.REQUEST_OTP -> "Request OTP"
            ActionType.RESET_PASSWORD -> "Reset Password"
        }

    val buttonValidation: Boolean
        get() = when (actionType.value) {
            ActionType.REQUEST_OTP -> {
                input.otp.useValidation.value = false
                input.password.useValidation.value = false
                input.confirmPassword.useValidation.value = false
                input.mobile.useValidation.value = true
                input.isValidObs.value
            }
            ActionType.RESET_PASSWORD -> {
                input.otp.useValidation.value = true
                input.password.useValidation.value = true
                input.confirmPassword.useValidation.value = true
                input.mobile.useValidation.value = false
                input.isValidObs.value
            }
        }

    val mobileFormCard: Boolean
        get() = actionType.value == ActionType.REQUEST_OTP
    val resetFormCard: Boolean
        get() = actionType.value == ActionType.RESET_PASSWORD


    private val _requestOTPResultFlow = resultShareFlow<ForgotPassword>()
    private val _resendOTPResultFlow = resultShareFlow<AppResponse>()
    private val _resetPasswordResultFlow = resultShareFlow<AppResponse>()

    val timerState = mutableStateOf(true)
    var token = ""

    init {

        viewModelScope.launch {
            _requestOTPResultFlow.getLatest {
                if (it.status == 1) {
                    successBanner("Reset Password OTP", it.message)
                    token = it.token.orEmpty()
                    actionType.value = ActionType.RESET_PASSWORD
                } else failureDialog(it.message)
            }
        }

        viewModelScope.launch {
            _resendOTPResultFlow.getLatest {
                if (it.status == 1) {
                    successBanner("Resend OTP", it.message)
                    timerState.value = true
                } else failureBanner("Resend OTP",it.message)
            }
        }

        viewModelScope.launch {
            _resetPasswordResultFlow.getLatest {
                if (it.status == 1) {
                   successDialog(it.message){
                       navigateUpWithResult()
                   }
                } else failureDialog(it.message)
            }
        }

    }

    fun onProceed() {
        when (actionType.value) {
            ActionType.REQUEST_OTP -> requestOTP()
            ActionType.RESET_PASSWORD -> resetPassword()
        }
    }

    private fun resetPassword() {
        val param = hashMapOf(
            "otp" to input.otp.getValue(),
            "password" to input.password.getValue(),
            "password_confirmation" to input.confirmPassword.getValue(),
            "token" to token,

        )

        callApiForShareFlow(
            flow = _resetPasswordResultFlow,
            call = {
                repository.resetForgotPassword(param)
            }
        )
    }

    private fun requestOTP() {

        callApiForShareFlow(
            flow = _requestOTPResultFlow,
            call = {
                repository.forgotPasswordRequestOTP(
                    hashMapOf("mobile" to input.mobile.getValue())
                )
            }
        )

    }

    fun onResendOtp() {

        val mobile = AppSecurity.encrypt(input.mobile.getValue())
        val type = AppSecurity.encrypt("FORGET_PASSWORD")

        callApiForShareFlow(
            flow = _resendOTPResultFlow,
            call = {
                repository.forgotPasswordResendOTP(
                    hashMapOf(
                        "mobileNumber" to mobile.orEmpty(),
                        "type" to type.orEmpty(),
                        "token" to  token,
                    )
                )
            }
        )

    }


    data class FormInput(
        val mobile: InputWrapper = InputWrapper { AppValidator.mobile(it) },
        val otp: InputWrapper = InputWrapper { AppValidator.otp(it) },
        val password: InputWrapper = InputWrapper { AppValidator.password(it) },
        val confirmPassword: InputWrapper = InputWrapper {
            AppValidator.confirmPassword(
                it,
                password.getValue()
            )
        },
    ) : BaseInput(mobile, otp, password, confirmPassword)


    enum class ActionType {
        REQUEST_OTP, RESET_PASSWORD
    }

}