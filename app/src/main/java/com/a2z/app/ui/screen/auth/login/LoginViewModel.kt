package com.a2z.app.ui.screen.auth.login

import android.content.Context
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.auth.AepsDriver
import com.a2z.app.data.model.auth.User
import com.a2z.app.data.repository.AuthRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.service.LocalAuth
import com.a2z.app.ui.theme.LocalUserRole
import com.a2z.app.ui.util.*
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.resource.BannerType
import com.a2z.app.util.AppUtil.Empty
import com.a2z.app.util.AppUtilDI
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.security.AppSecurity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appUtilDI: AppUtilDI,
    val appPreference: AppPreference,
) : BaseViewModel()
{

    var input = LoginInput()
    var loginCheckState = mutableStateOf(false)
    var autoLogin by mutableStateOf(false)
    var error = mutableStateOf<java.lang.Exception?>(null)


    private val loginFlow = resultShareFlow<User>()

    init {

        val prefLoginId = appPreference.loginId
        val prefPassword = appPreference.password
        val prefLoginCheck = appPreference.loginCheck

        if (prefLoginCheck && prefLoginId.isNotEmpty() && prefPassword.isNotEmpty()) {
            input.userIdWrapper.input.value = prefLoginId
            input.passwordWrapper.input.value = prefPassword
            loginCheckState.value = appPreference.loginCheck
            input.validate()
        }
        viewModelScope.launch { subscribers() }
    }


    private suspend fun subscribers() {
        loginFlow.getLatest(
            progress = {
                if (autoLogin)
                    progressFullScreenDialog("Login")
                else progressDialog("Login")
            },
            failure = {
                if (autoLogin)
                    error.value = it
            },
            success = { onLoginSuccess(it) }
        )
    }

    private suspend fun onLoginSuccess(user: User) {
        val status = user.status
        val message = user.message
        when (status) {
            1 -> {
                saveData(user)
                if (!autoLogin) {
                    setBanner(BannerType.Success(title = "User Login", message = message))
                    delay(1000)
                }
                navigateTo(
                    route = NavScreen.DashboardScreen.passArgs(!autoLogin),
                    popUpAll = true
                )
            }
            700 -> {

                if (autoLogin) {
                    autoLogin = false
                }

                val mobile = input.userIdWrapper.getValue()
                navigateTo(NavScreen.LoginOtpScreen.passArgs(mobile))
            }
            else -> {
                appPreference.user = null
                appPreference.locationFetched = false
                if (autoLogin) {
                    autoLogin = false
                } else alertDialog(message)
            }
        }
    }

    fun login() {
        if (bannerState.value is BannerType.Success && !autoLogin) return
        error.value = null
        val password =
            if (autoLogin) AppSecurity.encrypt(appPreference.password) ?: ""
            else AppSecurity.encrypt(input.passwordWrapper.input.value) ?: ""
        val mobile = if (autoLogin) AppSecurity.encrypt(appPreference.loginId) ?: ""
        else AppSecurity.encrypt(input.userIdWrapper.input.value) ?: ""
        val latitude = appPreference.latitude
        val longitude = appPreference.longitude
        callApiForShareFlow(loginFlow, handleException = !autoLogin) {
            authRepository.login(
                "case" to "FIRST",
                "password" to password,
                "mobileNumber" to mobile,
                "device_token" to Empty,
                "hardwareSerialNumber" to "unknown",
                "deviceName" to Build.BRAND,
                "imei" to appUtilDI.appUniqueIdentifier(),
                "latitude" to latitude,
                "longitude" to longitude,
            )
        }
    }

    private fun saveData(it: User) {

        appPreference.user = it

        aepsDrivers = it.aepsDrivers as MutableList<AepsDriver>

        if (loginCheckState.value) {
            appPreference.loginCheck = true
            appPreference.loginId = input.userIdWrapper.input.value
            appPreference.password = input.passwordWrapper.input.value
        } else {
            appPreference.loginCheck = false
            appPreference.loginId = ""
            appPreference.password = ""
        }
    }
}

data class LoginInput(

    val userIdWrapper: InputWrapper = InputWrapper { AppValidator.userId(it) },
    val passwordWrapper: InputWrapper = InputWrapper {
        if (it.length < 6) Pair(false, "Enter valid password")
        else Pair(true, "")
    },

    ) : BaseInput(userIdWrapper, passwordWrapper)

