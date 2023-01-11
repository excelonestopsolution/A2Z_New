package com.a2z.app.ui.screen.auth.change.password

import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.repository.AuthRepository
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
class ChangePasswordViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val appPreference: AppPreference
) : BaseViewModel() {

    val input = ChangePasswordInput()

    private val _changePasswordResultFlow = resultShareFlow<AppResponse>()

    init {
        viewModelScope.launch {
            _changePasswordResultFlow.getLatest {
                if (it.status == 1) successDialog(it.message) {
                    appPreference.user = null
                    navigateTo(NavScreen.LoginScreen.route, true)
                }
                else alertDialog(it.message)
            }
        }
    }


    fun changePassword() {
        val param = hashMapOf(
            "old_password" to input.currentPassword.getValue(),
            "password" to input.newPassword.getValue()
        )
        callApiForShareFlow(
            flow = _changePasswordResultFlow,
            call = { repository.changePassword(param) }
        )
    }


}

data class ChangePasswordInput(
    val currentPassword: InputWrapper = InputWrapper { AppValidator.password(it) },
    val newPassword: InputWrapper = InputWrapper { AppValidator.password(it) },
    val confirmPassword: InputWrapper = InputWrapper {
        AppValidator.confirmPassword(
            it,
            newPassword.getValue()
        )
    }
) : BaseInput(currentPassword, newPassword, confirmPassword)