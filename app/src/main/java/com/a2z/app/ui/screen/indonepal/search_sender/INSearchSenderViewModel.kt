package com.a2z.app.ui.screen.indonepal.search_sender

import androidx.compose.runtime.mutableStateOf
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.indonepal.INKycRedirect
import com.a2z.app.data.model.indonepal.INMobileVerificationResponse
import com.a2z.app.data.model.indonepal.INSender
import com.a2z.app.data.repository.IndoNepalRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class INSearchSenderViewModel @Inject constructor(
    private val repository: IndoNepalRepository,
) : BaseViewModel() {

    val input = FormInput()

    private val _searchSenderResponse = resultShareFlow<INMobileVerificationResponse>()

    val kycDialogState = mutableStateOf(false)
    val onboardDialogState = mutableStateOf(false)
    val kycType = mutableStateOf(INKycType.KYC)

    var senderInfo: INSender? = null

    private val _kycRedirectUrlResultFlow = resultShareFlow<INKycRedirect>()
    private val _onboardUserResultFlow = resultShareFlow<AppResponse>()

    val redirectUrl = MutableSharedFlow<String>()

    init {
        _searchSenderResponse.getLatest {
            when (it.status) {
                1 -> {
                    senderInfo = it.data
                    if (it.data?.ekyc_status == "1" && it.data.onboarding_status == "1")
                        navigateTo(NavScreen.INDetailSenderScreen.passArgs(it.data))
                    else {
                        if (it.data?.ekyc_status == "0")
                            kycType.value = INKycType.KYC
                        else if (it.data?.onboarding_status == "0")
                            kycType.value = INKycType.ON_BOARDING
                        kycDialogState.value = true
                    }
                }
                11 -> {
                    navigateTo(NavScreen.INRegistrationScreen.passArgs(input.mobileNumber.getValue()))
                }
                else -> alertDialog(it.message)
            }
        }

        _kycRedirectUrlResultFlow.getLatest {
            if (it.status == 1) {
                redirectUrl.emit(it.url!!)
            } else alertDialog(it.message)
        }

        _onboardUserResultFlow.getLatest {
            if(it.status == 1){
                successDialog(it.message){
                    onSearchClick()
                }
            }else alertDialog(it.message)
        }
    }


    fun onSearchClick() {
        callApiForShareFlow(_searchSenderResponse) {
            repository.mobileVerification(
                hashMapOf("number" to input.mobileNumber.getValue())
            )
        }
    }

    fun kycRedirectUrl() {

        callApiForShareFlow(_kycRedirectUrlResultFlow) {
            repository.kycRedirectUrl(
                hashMapOf(
                    "customerId" to senderInfo?.customerId.toString()
                )
            )
        }
    }


    fun onboardUser(customerId: String, sourceIncomeId: String, annualIncomeId: String) {

        callApiForShareFlow(_onboardUserResultFlow) {
            repository.onboardSender(
                hashMapOf(
                    "customerType" to customerId,
                    "sourceIncomeType" to sourceIncomeId,
                    "annualIncome" to annualIncomeId,
                    "customerId" to senderInfo?.customerId.toString()
                )
            )
        }
    }



    data class FormInput(
        val mobileNumber: InputWrapper = InputWrapper { AppValidator.mobile(it) }
    ) : BaseInput(mobileNumber)
}