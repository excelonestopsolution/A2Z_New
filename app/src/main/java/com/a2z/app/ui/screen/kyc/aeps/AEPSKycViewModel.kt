package com.a2z.app.ui.screen.kyc.aeps

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.kyc.AepsKycDetailResponse
import com.a2z.app.data.repository.KycRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AEPSKycViewModel @Inject constructor(
    private val repository: KycRepository,
    private val appPreference: AppPreference
) : BaseViewModel() {

    val input = InputForm()
    lateinit var pidData: String

    val kycDetailResultFlow = resultStateFlow<AepsKycDetailResponse>()
    private val _requestOtpResultFlow = resultShareFlow<AppResponse>()
    private val _verifyOtpResultFlow = resultShareFlow<AppResponse>()
    private val _kycResultFlow = resultShareFlow<AppResponse>()
    val showOtpDialogState = mutableStateOf(false)


    init {
        fetchDetails()

        viewModelScope.launch {
            _requestOtpResultFlow.getLatest {
                if (it.status == 1) {
                    successDialog(it.message) {
                        showOtpDialogState.value = true
                    }
                } else failureDialog(it.message)
            }
        }

        viewModelScope.launch {
            _verifyOtpResultFlow.getLatest {
                if (it.status == 1) {
                    proceedForKyc()
                } else failureDialog(it.message) {
                    showOtpDialogState.value = true
                }
            }
        }

        viewModelScope.launch {
            _kycResultFlow.getLatest {
                if (it.status == 1) {
                    navigateTo(NavScreen.DashboardScreen.route, true)
                } else failureDialog(it.message)
            }
        }

    }


    private fun fetchDetails() {
        callApiForStateFlow(
            flow = kycDetailResultFlow,
            call = { repository.aepsKycDetail() },
            beforeEmit = {
                if (it is ResultType.Success) {
                    it.data.details?.let {
                        input.name.setValue(it.agent_name)
                        input.pan.setValue(it.pan_number)
                        input.aadhaar.setValue(it.aadhaar_number)
                        input.merchantId.setValue(it.merchant_login_id)
                    }

                }
            }
        )
    }

    fun onBiometricResult(pidData: String) {
        this.pidData = pidData
        requestKycOtp()
    }

    private fun requestKycOtp() {

        val param = hashMapOf(
            "latitude" to appPreference.latitude,
            "longitude" to appPreference.longitude
        )
        callApiForShareFlow(
            flow = _requestOtpResultFlow,
            call = { repository.aepsKycRequestOtp(param) }
        )
    }

    fun verifyKycOtp(otp: String) {

        val param = hashMapOf(
            "latitude" to appPreference.latitude,
            "longitude" to appPreference.longitude,
            "otp" to otp
        )

        callApiForShareFlow(
            flow = _verifyOtpResultFlow,
            call = { repository.aepsKycVerifyOtp(param) }
        )
    }

    private fun proceedForKyc() {

        val param = hashMapOf(
            "latitude" to appPreference.latitude,
            "longitude" to appPreference.longitude,
            "biometricData" to pidData
        )

        callApiForShareFlow(
            flow = _kycResultFlow,
            call = { repository.aepsKyc(param) }
        )

    }


    data class InputForm(
        val name: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
        val pan: InputWrapper = InputWrapper { AppValidator.pan(it) },
        val aadhaar: InputWrapper = InputWrapper { AppValidator.aadhaarValidation(it) },
        val merchantId: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
    ) : BaseInput(name, pan, aadhaar, merchantId)
}