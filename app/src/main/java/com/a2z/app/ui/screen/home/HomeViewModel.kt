package com.a2z.app.ui.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.FlightHotelRedirectUrlResponse
import com.a2z.app.data.model.PanAutoLoginResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.Slider
import com.a2z.app.data.model.dmt.BankDownResponse
import com.a2z.app.data.model.kyc.KycInfoResponse
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.data.repository.UpiRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AppRepository,
    private val upiRepository: UpiRepository,
    val appPreference: AppPreference
) : BaseViewModel() {

    var exitFromApp = false

    val exitDialogState = mutableStateOf(false)

    val isSliderVisible = mutableStateOf(false)
    val sliders = mutableListOf<Slider>()

    val singInDialogState = mutableStateOf(false)
    val onLaunchEffect = mutableStateOf(false)

    val homeScreenState = MutableSharedFlow<HomeScreenState>()

    val balanceResponseFlow = resultStateFlow<BalanceResponse>()
    val hotelFlightDirectUrlFlow = resultShareFlow<FlightHotelRedirectUrlResponse>()
    val panAutoLogFlow = resultShareFlow<PanAutoLoginResponse>()


    private val _logoutSharedFlow = MutableSharedFlow<ResultType<AppResponse>>()
    private val _checkKycInfoFlow = MutableSharedFlow<ResultType<KycInfoResponse>>()

    val dmtKycPendingState = mutableStateOf(checkDMTKycPending())
    val dmtAndAEPSKycPendingState = mutableStateOf(checkDMTAndAEPSKycPending())

    val kycDialogState = mutableStateOf(false)

    init {
        fetchUpiVerifyStaticMessage()
        fetchWalletBalance()
        fetchBanners()
        fetchNews()
        fetchBankDown()
        observeLogoutShareFlow()

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

    private fun observeLogoutShareFlow() = viewModelScope.launch {
        _logoutSharedFlow.getLatest {
            appPreference.user = null
            appPreference.loginCheck = false
            appPreference.loginId = ""
            appPreference.password = ""
            useLocalAuth = true
            homeScreenState.emit(HomeScreenState.OnLogoutComplete)
        }
    }

    fun fetchWalletBalance() {
        callApiForShareFlow(
            flow = balanceResponseFlow,
            handleException = false,
            beforeEmit = {
                if (it is ResultType.Failure) {
                    homeScreenState.emit(HomeScreenState.OnHomeApiFailure(it.exception))
                }
            }

        )
        { repository.fetchWalletBalance() }
    }


    fun setOnLaunchEffect() {
        onLaunchEffect.value = !onLaunchEffect.value
    }


    fun fetchBanners() {
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

    val newsResponseState = mutableStateOf<NewsResponse?>(null)
    val bankDownResponseState = mutableStateOf<BankDownResponse?>(null)

    fun fetchNews() {
        callApiForShareFlow(beforeEmit = {
            if (it is ResultType.Success)
                if (it.data.status == 1)
                    newsResponseState.value = it.data
                else newsResponseState.value = null
        }, handleException = false) { repository.fetchNews() }
    }

    private fun fetchBankDown() {
        callApiForShareFlow(beforeEmit = {
            if (it is ResultType.Success)
                if (it.data.status == 1) {
                    bankDownResponseState.value = it.data
                } else newsResponseState.value = null
        }, handleException = false) { repository.fetchBankDown() }
    }

    fun logout() {

        viewModelScope.launch {
            _logoutSharedFlow.emit(ResultType.Loading())
            delay(1000)
            _logoutSharedFlow.emit(ResultType.Success(AppResponse(1, "Logout successfully")))
        }
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
            "AADHAAR_KYC" -> {navigateTo(NavScreen.AadhaarKycScreen.route)}
            "VIDEO_KYC" -> {navigateTo(NavScreen.DocumentKycScreen.route)}
            "SETTLEMENT" -> {navigateTo(NavScreen.SettlementBankAddScreen.route)}
            "AEPS_KYC" -> {navigateTo(NavScreen.AEPSKycScreen.route)}
        }

    }

}

sealed class HomeScreenState {
    object OnLogoutComplete : HomeScreenState()
    data class OnHomeApiFailure(val exception: Exception) : HomeScreenState()
}

