package com.a2z.app.ui.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.Slider
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.resource.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AppRepository,
    val appPreference: AppPreference
) : BaseViewModel() {

    val exitDialogState = mutableStateOf(false)

    val isSliderVisible = mutableStateOf(false)
    val sliders = mutableListOf<Slider>()

    val singInDialogState = mutableStateOf(false)
    val onLaunchEffect = mutableStateOf(false)

    val homeScreenState = MutableSharedFlow<HomeScreenState>()

    val balanceFlow = MutableStateFlow<ResultType<BalanceResponse>>(
        ResultType.Loading()
    )

    private val _logoutSharedFlow = MutableSharedFlow<ResultType<AppResponse>>()

    init {
        fetchWalletBalance()
        fetchBanners()
        fetchNews()
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
        callApiForShareFlow (
            flow =balanceFlow,
            showExceptionDialog = false)
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
        }, showExceptionDialog = false) { repository.fetchBanner() }
    }

    val newsResponseState = mutableStateOf<NewsResponse?>(null)

    fun fetchNews() {
        callApiForShareFlow(beforeEmit = {
            if (it is ResultType.Success)
                if (it.data.status == 1)
                    newsResponseState.value = it.data
                else newsResponseState.value = null
        }, showExceptionDialog = false) { repository.fetchNews() }
    }

    fun logout() {

        viewModelScope.launch {
            _logoutSharedFlow.emit(ResultType.Loading())
            delay(1000)
            _logoutSharedFlow.emit(ResultType.Success(AppResponse(1,"Logout successfully")))
        }

    }


}

sealed class HomeScreenState{
    object OnLogoutComplete : HomeScreenState()
}
