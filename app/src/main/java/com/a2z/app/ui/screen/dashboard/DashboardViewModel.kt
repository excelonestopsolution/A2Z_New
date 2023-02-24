package com.a2z.app.ui.screen.dashboard

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.screen.home.component.useLocalAuth
import com.a2z.app.ui.screen.home.retailer.HomeScreenState
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    saveStateHandle: SavedStateHandle,
    val appPreference: AppPreference,
    private val repository: AppRepository,
) : BaseViewModel() {

    var scaffoldState: ScaffoldState? = null
    var fromLogin = saveStateHandle.get<String?>("fromLogin").toBoolean()

    var exitFromApp = false

    val exitDialogState = mutableStateOf(false)

    val onLaunchEffect = mutableStateOf(false)

    val homeScreenState = MutableSharedFlow<HomeScreenState>()

    val balanceResponseFlow = resultStateFlow<BalanceResponse>()

    val singInDialogState = mutableStateOf(false)

    val newsResponseState = mutableStateOf<NewsResponse?>(null)

    private val _logoutSharedFlow = MutableSharedFlow<ResultType<AppResponse>>()

    init {
        fetchNews()
        observeLogoutShareFlow()
    }
    fun setOnLaunchEffect() {
        onLaunchEffect.value = !onLaunchEffect.value
    }

    fun fetchWalletBalance() {
        callApiForShareFlow(
            flow = balanceResponseFlow,
            handleException = false,
            beforeEmit = {
                if(it is ResultType.Success){
                    if(it.data.status != 1){
                        navigateTo(NavScreen.LoginScreen.route,true)
                    }
                }
                if (it is ResultType.Failure) {
                    homeScreenState.emit(HomeScreenState.OnHomeApiFailure(it.exception))
                }
            }

        )
        { repository.fetchWalletBalance() }
    }

    fun fetchNews() {
        callApiForShareFlow(beforeEmit = {
            if (it is ResultType.Success)
                if (it.data.status == 1)
                    newsResponseState.value = it.data
                else newsResponseState.value = null
        }, handleException = false) { repository.fetchNews() }
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

    fun logout() {

        viewModelScope.launch {
            _logoutSharedFlow.emit(ResultType.Loading())
            delay(1000)
            _logoutSharedFlow.emit(ResultType.Success(AppResponse(1, "Logout successfully")))
        }
    }

}