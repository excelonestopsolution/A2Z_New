package com.a2z.app.ui.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.Slider
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.AppUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appRepository: AppRepository,
    val appPreference: AppPreference
) : BaseViewModel() {

    val exitDialogState = mutableStateOf(false)

    val isSliderVisible = mutableStateOf(false)
    val sliders = mutableListOf<Slider>()

    val singInDialogState = mutableStateOf(false)
    val onLaunchEffect = mutableStateOf(false)

    val homeScreenState = MutableSharedFlow<HomeScreenState>()

    private val _logoutSharedFlow = MutableSharedFlow<ResultType<AppResponse>>()

    init {
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



    fun setOnLaunchEffect() {
        onLaunchEffect.value = !onLaunchEffect.value
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
        }, useExceptionNavigator = false) { appRepository.fetchBanner() }
    }

    val newsResponseState = mutableStateOf<NewsResponse?>(null)

    private fun fetchNews() {
        callApiForShareFlow(beforeEmit = {
            if (it is ResultType.Success)
                if (it.data.status == 1)
                    newsResponseState.value = it.data
                else newsResponseState.value = null
        }, useExceptionNavigator = false) { appRepository.fetchNews() }
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
