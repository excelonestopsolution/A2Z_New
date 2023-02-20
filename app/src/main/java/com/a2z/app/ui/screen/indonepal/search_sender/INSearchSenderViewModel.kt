package com.a2z.app.ui.screen.indonepal.search_sender

import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.indonepal.INMobileVerificationResponse
import com.a2z.app.data.repository.IndoNepalRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.ApiUtil
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class INSearchSenderViewModel @Inject constructor(
    private val repository: IndoNepalRepository,
    private val apiUtil: ApiUtil
) : BaseViewModel() {

    val input = FormInput()

    private val _searchSenderResponse = resultShareFlow<INMobileVerificationResponse>()


    init {
        _searchSenderResponse.getLatest {
            when (it.status) {
                1 -> {
                    navigateTo(NavScreen.INDetailSenderScreen.passArgs(it.data!!))
                }
                11 -> {
                    navigateTo(NavScreen.INRegistrationScreen.passArgs(input.mobileNumber.getValue()))
                }
                else -> alertDialog(it.message)
            }
        }
    }


    fun onSearchClick() {
        callApiForShareFlow(_searchSenderResponse) {
            repository.mobileVerification(
                hashMapOf("number" to input.mobileNumber.getValue())
            )
        }
    }


    data class FormInput(
        val mobileNumber: InputWrapper = InputWrapper { AppValidator.mobile(it) }
    ) : BaseInput(mobileNumber)
}