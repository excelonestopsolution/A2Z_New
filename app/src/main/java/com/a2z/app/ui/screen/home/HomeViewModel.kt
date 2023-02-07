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
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.data.repository.UpiRepository
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

    init {
        fetchUpiVerifyStaticMessage()
        fetchWalletBalance()
        fetchBanners()
        fetchNews()
        fetchBankDown()
        observeLogoutShareFlow()
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

    fun checkDMTAndAEPSKycPending(): Boolean {
        return appPreference.user?.isAadhaarKyc == 0
                || appPreference.user?.aepsKyc == 0
                || appPreference.user?.isUserHasActiveSettlementAccount == 0
                || appPreference.user?.isVideoKyc == 0
    }

    private fun checkDMTKycPending(): Boolean {
        return appPreference.user?.isAadhaarKyc == 0
                || appPreference.user?.isVideoKyc == 0
    }

    fun kycInfo(): Triple<String, String, String>? {
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
            else -> return null
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

    fun fetchUpiVerifyStaticMessage() {
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


}

sealed class HomeScreenState {
    object OnLogoutComplete : HomeScreenState()
    data class OnHomeApiFailure(val exception: Exception) : HomeScreenState()
}

