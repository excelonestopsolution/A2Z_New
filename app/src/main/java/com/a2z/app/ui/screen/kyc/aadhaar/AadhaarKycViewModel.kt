package com.a2z.app.ui.screen.kyc.aadhaar

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
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
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val input = FormInput()
    private val _requestOtpResultFlow = resultShareFlow<AadhaarKycResponse>()
    private val _verifyOtpResultFlow = resultShareFlow<AadhaarKycResponse>()
    val showOtpDialogState = mutableStateOf(false)

    val parentUserId = savedStateHandle.get<String>("parentUserId") ?: ""

    private var requestId = ""

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
                    if (parentUserId.isEmpty())
                        appPreference.user = appPreference.user?.copy(isAadhaarKyc = 1)
                    successDialog(it.message) {
                        navigateTo(NavScreen.DashboardScreen.route, true)
                    }
                } else failureDialog(it.message)
            }
        }
    }


    fun onSubmit() {
        val param = if (parentUserId.isNotEmpty())
            hashMapOf(
                "aadhaar_number" to input.aadhaar.getValue(),
                "mobile" to input.mobile.getValue(),
                "userId" to parentUserId,
                "latitude" to appPreference.latitude,
                "longitude" to appPreference.longitude,
            )
        else hashMapOf(
            "aadhaar_number" to input.aadhaar.getValue(),
            "mobile" to input.mobile.getValue(),
            "latitude" to appPreference.latitude,
            "longitude" to appPreference.longitude,
        )

        val call = suspend {
            if (parentUserId.isNotEmpty())
                repository.registerUserPostAadharKyc(param)
            else repository.aadhaarKycRequestOtp(param)
        }

        callApiForShareFlow(
            flow = _requestOtpResultFlow,
            call = { call.invoke() }
        )
    }

    fun onOtpSubmit(otp: String) {
        val param = if (parentUserId.isNotEmpty())
            hashMapOf(
                "request_id" to requestId,
                "otp" to otp,
                "userId" to parentUserId,
                "latitude" to appPreference.longitude,
                "longitude" to appPreference.longitude,
            )
        else hashMapOf(
            "request_id" to requestId,
            "otp" to otp,
            "latitude" to appPreference.latitude,
            "longitude" to appPreference.longitude,
        )


        val call = suspend {
            if (parentUserId.isNotEmpty())
                repository.registerUserPostAadharKycVerify(param)
            else repository.aahdaarKycVerifyOtp(param)
        }

        callApiForShareFlow(
            flow = _verifyOtpResultFlow,
            call = { call.invoke() }
        )

    }


    data class FormInput(
        val aadhaar: InputWrapper = InputWrapper { AppValidator.aadhaarValidation(it) },
        val mobile: InputWrapper = InputWrapper { AppValidator.mobile(it) },
    ) : BaseInput(aadhaar, mobile)

}