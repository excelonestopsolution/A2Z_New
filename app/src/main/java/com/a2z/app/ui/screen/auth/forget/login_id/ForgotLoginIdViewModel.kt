package com.a2z.app.ui.screen.auth.forget.login_id

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
class ForgotLoginIdViewModel @Inject constructor(
    private val repository: AuthRepository
) : BaseViewModel() {


    private val actionType = mutableStateOf(ActionType.REQUEST_OTP)
    val input = FormInput()

    val buttonText: String
        get() = when (actionType.value) {
            ActionType.REQUEST_OTP -> "Request OTP"
            ActionType.RESET_LOGIN_ID -> "Reset Login Id"
        }

    val buttonValidation: Boolean
        get() = when (actionType.value) {
            ActionType.REQUEST_OTP -> {
                input.otp.useValidation.value = false
                input.isValidObs.value
            }
            ActionType.RESET_LOGIN_ID -> {
                input.otp.useValidation.value = true
                input.isValidObs.value
            }
        }

    val isOtpVisible : Boolean
    get() = actionType.value == ActionType.RESET_LOGIN_ID


    private val _requestOTPResultFlow = resultShareFlow<AppResponse>()
    private val _resetLoginIdResultFlow = resultShareFlow<AppResponse>()


    init {

        viewModelScope.launch {
            _requestOTPResultFlow.getLatest {
                if (it.status == 1) {
                    successBanner("Reset Login Id OTP", it.message)
                    actionType.value = ActionType.RESET_LOGIN_ID
                } else failureDialog(it.message)
            }
        }
        viewModelScope.launch {
            _resetLoginIdResultFlow.getLatest {
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
            ActionType.RESET_LOGIN_ID -> resetLoginId()
        }
    }

    private fun resetLoginId() {
        val param = hashMapOf(
            "mobile" to input.mobile.getValue(),
            "aadhaar_number" to input.aadhaar.getValue(),
            "otp" to input.otp.getValue()
        )
        callApiForShareFlow(
            flow = _resetLoginIdResultFlow,
            call = {
                repository.forgotLoginIdVerify(param)
            }
        )
    }

    private fun requestOTP() {

        val param =  hashMapOf(
            "mobile" to input.mobile.getValue(),
            "aadhaar_number" to input.aadhaar.getValue(),
        )
        callApiForShareFlow(
            flow = _requestOTPResultFlow,
            call = {
                repository.forgotLoginId(param)
            }
        )

    }



    data class FormInput(
        val mobile: InputWrapper = InputWrapper { AppValidator.mobile(it) },
        val aadhaar: InputWrapper = InputWrapper { AppValidator.aadhaarValidation(it) },
        val otp: InputWrapper = InputWrapper { AppValidator.otp(it) },

        ) : BaseInput(mobile, aadhaar, otp)


    enum class ActionType {
        REQUEST_OTP, RESET_LOGIN_ID
    }

}