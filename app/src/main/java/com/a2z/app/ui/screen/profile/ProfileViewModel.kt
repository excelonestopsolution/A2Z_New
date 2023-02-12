package com.a2z.app.ui.screen.profile

import androidx.compose.runtime.mutableStateOf
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.ui.dialog.StatusDialog
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AppRepository,
    private val appPreference: AppPreference,
) : BaseViewModel() {


    val user = appPreference.user
    val showOtpDialogState = mutableStateOf(false)
    private val dialogType = mutableStateOf(OtDialogType.VERIFY_MOBILE)

    private val _mobileOptRequestFlow = resultShareFlow<AppResponse>()
    private val _emailOptRequestFlow = resultShareFlow<AppResponse>()
    private val _mobileOptVerifyFlow = resultShareFlow<AppResponse>()
    private val _emailOptVerifyFlow = resultShareFlow<AppResponse>()

    init {
        _mobileOptRequestFlow.getLatest {
            if (it.status == 1) {
                dialogType.value = OtDialogType.VERIFY_MOBILE
                showOtpDialogState.value = true
            } else alertDialog(it.message)
        }

        _emailOptRequestFlow.getLatest {
            if (it.status == 1) {
                dialogType.value = OtDialogType.VERIFY_EMAIL
                showOtpDialogState.value = true
            } else alertDialog(it.message)
        }

        _mobileOptVerifyFlow.getLatest {
            if (it.status == 1) {
                appPreference.user = appPreference.user?.copy(isMobileVerified = 1)
                successDialog(it.message)
            } else alertDialog(it.message)
        }

        _emailOptVerifyFlow.getLatest {
            if (it.status == 1) {
                appPreference.user = appPreference.user?.copy(isEmailVerified = 1)
                successDialog(it.message)
            } else alertDialog(it.message)
        }
    }

    fun requestMobileVerifyOtp() {
        val param = hashMapOf("type" to "MOBILE")
        callApiForShareFlow(_mobileOptRequestFlow) { repository.mobileEmailVerifyRequestOtp(param) }

    }

    fun requestEmailVerifyOtp() {
        val param = hashMapOf("type" to "EMAIL")
        callApiForShareFlow(_emailOptRequestFlow) { repository.mobileEmailVerifyRequestOtp(param) }

    }


    fun onOtpSubmit(otp: String) {

        when (dialogType.value) {
            OtDialogType.VERIFY_MOBILE -> verifyMobile(otp)
            OtDialogType.VERIFY_EMAIL -> verifyEmail(otp)
        }

    }

    private fun verifyMobile(otp: String) {

        val param = hashMapOf(
            "type" to "MOBILE",
            "otp" to otp
        )
        callApiForShareFlow(_mobileOptVerifyFlow) {
            repository.mobileEmailVerifyOtp(param)
        }

    }

    private fun verifyEmail(otp: String) {
        val param = hashMapOf(
            "type" to "EMAIL",
            "otp" to otp
        )
        callApiForShareFlow(_emailOptVerifyFlow) {
            repository.mobileEmailVerifyOtp(param)
        }

    }


    enum class OtDialogType {
        VERIFY_MOBILE, VERIFY_EMAIL
    }
}