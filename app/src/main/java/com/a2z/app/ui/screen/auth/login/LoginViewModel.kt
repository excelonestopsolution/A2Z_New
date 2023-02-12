package com.a2z.app.ui.screen.auth.login

import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.auth.User
import com.a2z.app.data.repository.AuthRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
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
) : BaseViewModel() {

    var input = LoginInput()
    var loginCheckState = mutableStateOf(false)

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
            progress = { progressDialog("Login") },
            success = { onLoginSuccess(it) }
        )
    }

    private suspend fun onLoginSuccess(user: User) {
        val status = user.status
        val message = user.message
        when (status) {
            1 -> {
                setBanner(BannerType.Success(title = "User Login", message = message))
                saveData(user)
                delay(1000)

                navigateTo(
                    route = NavScreen.DashboardScreen.passArgs(true),
                    popUpAll = true
                )


            }
            700 -> {
                val mobile = input.userIdWrapper.getValue()
                navigateTo(NavScreen.LoginOtpScreen.passArgs(mobile))
            }
            else -> {
                alertDialog(message)
            }
        }
    }

    fun login() {
        if (bannerState.value is BannerType.Success) return
        val password = AppSecurity.encrypt(input.passwordWrapper.input.value) ?: ""
        val mobile = AppSecurity.encrypt(input.userIdWrapper.input.value) ?: ""
        val latitude = appPreference.latitude
        val longitude = appPreference.longitude
        callApiForShareFlow(loginFlow) {
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
    val passwordWrapper: InputWrapper = InputWrapper { AppValidator.password(it) },

    ) : BaseInput(userIdWrapper, passwordWrapper)

