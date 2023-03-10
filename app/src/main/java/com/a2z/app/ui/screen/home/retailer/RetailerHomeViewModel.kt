package com.a2z.app.ui.screen.home.retailer

import androidx.compose.runtime.mutableStateOf
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.FlightHotelRedirectUrlResponse
import com.a2z.app.data.model.PanAutoLoginResponse
import com.a2z.app.data.model.app.Slider
import com.a2z.app.data.model.dmt.BankDownResponse
import com.a2z.app.data.model.indonepal.INRequestStatus
import com.a2z.app.data.model.indonepal.INRequestStatusResponse
import com.a2z.app.data.model.kyc.KycInfoResponse
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.data.repository.UpiRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class RetailerHomeViewModel @Inject constructor(
    private val repository: AppRepository,
    private val upiRepository: UpiRepository,
    val appPreference: AppPreference
) : BaseViewModel() {


    val isSliderVisible = mutableStateOf(false)
    val sliders = mutableListOf<Slider>()


    val hotelFlightDirectUrlFlow = resultShareFlow<FlightHotelRedirectUrlResponse>()
    val panAutoLogFlow = resultShareFlow<PanAutoLoginResponse>()


    private val _checkKycInfoFlow = MutableSharedFlow<ResultType<KycInfoResponse>>()

    val dmtKycPendingState = mutableStateOf(checkDMTKycPending())
    val dmtAndAEPSKycPendingState = mutableStateOf(checkDMTAndAEPSKycPending())

    val kycDialogState = mutableStateOf(false)
    val bankDownResponseState = mutableStateOf<BankDownResponse?>(null)


    init {
        fetchUpiVerifyStaticMessage()
        fetchBanners()
        fetchBankDown()
        _checkKycInfoFlow.getLatest {

            if (it.status == 1) {

                appPreference.user = appPreference.user?.copy(
                    isAadhaarKyc = it.data.is_aadhaar_kyc,
                    isVideoKyc = it.data.is_video_kyc,
                    isUserHasActiveSettlementAccount = it.data.is_user_has_active_settlemnet_account,
                    aepsKyc = it.data.aeps_kyc
                )

                dmtKycPendingState.value = checkDMTKycPending()
                dmtAndAEPSKycPendingState.value = checkDMTAndAEPSKycPending()
                if (dmtAndAEPSKycPendingState.value) {
                    kycDialogState.value = true
                } else successDialog("You kyc process has done, Now you can access all Aeps and Dmt services. Thankyou")
            } else alertDialog(it.message)
        }
    }


    private fun fetchBanners() {
        callApiForShareFlow(beforeEmit = {
            if (it is ResultType.Success) {
                if (it.data.status == 1) {
                    sliders.clear()
                    sliders.addAll(it.data.sliders!!)
                    isSliderVisible.value = true
                }
            }
        }, handleException = false) { repository.fetchBanner() }
    }


    private fun fetchBankDown() {
        callApiForShareFlow(beforeEmit = {
            if (it is ResultType.Success)
                if (it.data.status == 1) {
                    bankDownResponseState.value = it.data
                } else bankDownResponseState.value = null
        }, handleException = false) { repository.fetchBankDown() }
    }


    private fun checkDMTAndAEPSKycPending(): Boolean {
        return appPreference.user?.isAadhaarKyc == 0
                || appPreference.user?.aepsKyc == 0
                || appPreference.user?.isUserHasActiveSettlementAccount == 0
                || appPreference.user?.isVideoKyc == 0
    }

    private fun checkDMTKycPending(): Boolean {
        return appPreference.user?.isAadhaarKyc == 0
                || appPreference.user?.isVideoKyc == 0
    }

    fun kycInfo(): Triple<String, String, String> {
        when {
            appPreference.user?.isAadhaarKyc == 0 -> {
                return Triple(
                    "Aadhaar Kyc Required!",
                    "Aadhaar kyc is required, to enable DMT and AEPS Services",
                    "AADHAAR_KYC"

                )
            }
            appPreference.user?.isVideoKyc == 0 -> {
                return Triple(
                    "Upload documents First",
                    "Please upload all required documents, to enable DMT and AEPS Services",
                    "VIDEO_KYC"

                )

            }
            appPreference.user?.isUserHasActiveSettlementAccount == 0 -> {
                return Triple(
                    "Add Bank First!",
                    "Please add aeps settlement bank first, to enable AEPS",
                    "SETTLEMENT"

                )
            }
            appPreference.user?.aepsKyc == 0 -> {
                return Triple(
                    "Aeps Kyc Required!",
                    "Aadhaar e-kyc is required, to enable AEPS Services",
                    "AEPS_KYC"

                )
            }
            else -> return Triple(
                "Kyc Required!",
                "Kyc is required for AEPS and DMT Transactions",
                ""

            )
        }
    }

    fun flightHotelRedirectUrl() {

        val param = hashMapOf("na" to "na")
        callApiForShareFlow(hotelFlightDirectUrlFlow) { repository.flightHotelRedirectUrl(param) }
    }

    fun panAutoLogin() {
        val param = hashMapOf("na" to "na")
        callApiForShareFlow(panAutoLogFlow) { repository.panAutoLogin(param) }

    }

    private fun fetchUpiVerifyStaticMessage() {
        if (appPreference.upiStateMessage == null) callApiForShareFlow(
            call = { upiRepository.upiVerifyStaticMessage() },
            handleException = false,
            beforeEmit = {
                if (it is ResultType.Success) {
                    appPreference.upiStateMessage = it.data
                }
            }
        )

    }

    fun checkKycInfo() {
        callApiForShareFlow(_checkKycInfoFlow) { repository.kycCheck() }
    }

    fun navigateKycScreen(it: String) {
        when (it) {
            "AADHAAR_KYC" -> {
                navigateTo(NavScreen.AadhaarKycScreen.passArgs())
            }
            "VIDEO_KYC" -> {
                navigateTo(NavScreen.DocumentKycScreen.passArgs())
            }
            "SETTLEMENT" -> {
                navigateTo(NavScreen.SettlementBankAddScreen.route)
            }
            "AEPS_KYC" -> {
                navigateTo(NavScreen.AEPSKycScreen.route)
            }
        }

    }
}

sealed class HomeScreenState {
    object OnLogoutComplete : HomeScreenState()
    data class OnHomeApiFailure(val exception: Exception) : HomeScreenState()
}

