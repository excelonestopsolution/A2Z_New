package com.a2z.app.ui.screen.auth.verification

import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.repository.AuthRepository
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.resource.BannerType
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.AppUtilDI
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.security.AppSecurity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginOtpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appUtilDI: AppUtilDI
) : BaseViewModel() {

    val formData = LoginOtpInput()
    var mobileNumber: String = ""

    val verifyFlow = resultShareFlow<AppResponse>()

    init {
        viewModelScope.launch { subscribers() }
    }

    private suspend fun subscribers() {
        verifyFlow.getLatest {
            val message = it.message
            val isSuccess = it.status == 701
            if (isSuccess) {
                setBanner(BannerType.Success("Device Verification", message))
                delay(1000)
                navigateUpWithResult("callLogin" to true)
            } else
                alertDialog(message)

        }
    }

    fun verifyOtp() = callApiForShareFlow(verifyFlow) {
        authRepository.verifyLoginOtp(
            "mobileNumber" to (AppSecurity.encrypt(mobileNumber) ?: ""),
            "otp" to (AppSecurity.encrypt(formData.otpWrapper.input.value) ?: ""),
            "imei" to appUtilDI.appUniqueIdentifier()
        )
    }

}

data class LoginOtpInput(
    val otpWrapper: InputWrapper = InputWrapper { AppValidator.otp(it) }

) : BaseInput(otpWrapper)