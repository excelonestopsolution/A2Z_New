package com.a2z.app.ui.screen.kyc.aadhaar

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.kyc.AadhaarKycResponse
import com.a2z.app.data.repository.KycRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AadhaarKycViewModel @Inject constructor(
    private val repository: KycRepository,
    val appPreference: AppPreference,
) : BaseViewModel() {

    val input = FormInput()
    private val _requestOtpResultFlow = resultShareFlow<AadhaarKycResponse>()
    private val _verifyOtpResultFlow = resultShareFlow<AadhaarKycResponse>()
    val showOtpDialogState = mutableStateOf(false)

    var requestId = ""

    init {
        viewModelScope.launch {
            _requestOtpResultFlow.getLatest {
                if (it.status == 3) {
                    requestId = it.requestId.orEmpty()
                    showOtpDialogState.value = true
                } else failureDialog(it.message)
            }
        }

        viewModelScope.launch {
            _verifyOtpResultFlow.getLatest {
                if (it.status == 1) {
                    appPreference.user = appPreference.user?.copy(isAadhaarKyc = 1)
                    successDialog(it.message) {
                        navigateTo(NavScreen.DashboardScreen.route, true)
                    }
                } else failureDialog(it.message)
            }
        }
    }


    fun onSubmit() {
        val param = hashMapOf(
            "aadhaar_number" to input.aadhaar.getValue(),
            "mobile" to input.mobile.getValue(),
            "latitude" to appPreference.latitude,
            "longitude" to appPreference.longitude,
        )
        callApiForShareFlow(
            flow = _requestOtpResultFlow,
            call = { repository.aadhaarKycRequestOtp(param) }
        )
    }

    fun onOtpSubmit(otp: String) {
        val param = hashMapOf(
            "request_id" to requestId,
            "otp" to otp,
            "latitude" to appPreference.latitude,
            "longitude" to appPreference.longitude,
        )
        callApiForShareFlow(
            flow = _verifyOtpResultFlow,
            call = { repository.aahdaarKycVerifyOtp(param) }
        )

    }


    data class FormInput(
        val aadhaar: InputWrapper = InputWrapper { AppValidator.aadhaarValidation(it) },
        val mobile: InputWrapper = InputWrapper { AppValidator.mobile(it) },
    ) : BaseInput(aadhaar, mobile)

}