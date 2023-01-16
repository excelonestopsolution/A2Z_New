package com.a2z.app.ui.screen.dmt.sender.register

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.MoneySender
import com.a2z.app.data.model.dmt.SenderRegistrationResponse
import com.a2z.app.data.repository.DMT3Repository
import com.a2z.app.data.repository.DMTRepository
import com.a2z.app.ui.screen.dmt.util.DMTType
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.safeParcelable
import com.a2z.app.util.extension.notNullOrEmpty
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SenderRegisterViewModel @Inject constructor(
    private val repository: DMTRepository,
    private val dmt3repository: DMT3Repository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {


    private val args: SenderRegistrationArgs =
        savedStateHandle.safeParcelable("senderRegistrationArgs")!!

    private val dmtDMType = args.dmtType

    private val moneySender: MoneySender = args.moneySender!!
    private var senderState = args.state
    private val senderRegistrationType = mutableStateOf(args.registrationType)

    private val otpValidation = mutableStateOf(false)

    val input = SenderRegisterInput(otpValidation)

    val timerState = mutableStateOf(true)

    private val _newRegisterResultFlow = resultShareFlow<SenderRegistrationResponse>()
    private val _verifyNewSenderResultFlow = resultShareFlow<AppResponse>()
    private val _resendOtpResultFlow = resultShareFlow<SenderRegistrationResponse>()

    init {
        input.mobileNumber.setValue(args.moneySender?.mobileNumber)
        if (senderRegistrationType.value == SenderRegistrationType.VERIFY_AND_UPDATE) {
            otpValidation.value = true
            if (moneySender.firstName.notNullOrEmpty())
                input.firstName.setValue(moneySender.firstName)
            if (moneySender.lastName.notNullOrEmpty())
                input.lastName.setValue(moneySender.lastName)
        }

        viewModelScope.launch {
            _newRegisterResultFlow.getLatest {
                if (it.status == 12) {
                    senderState = it.state.orEmpty()
                    successDialog(it.message) {
                        otpValidation.value = true
                        senderRegistrationType.value = SenderRegistrationType.VERIFY_SENDER
                    }
                } else {
                    alertDialog(it.message)
                }
            }
        }

        viewModelScope.launch {
            _resendOtpResultFlow.getLatest {
                if (it.status == 12) {
                    successBanner("Success", it.message)
                    if (it.state.notNullOrEmpty() && dmtDMType == DMTType.DMT_3)
                        senderState = it.state
                    timerState.value = true
                } else {
                    alertBanner("Failed", it.message)
                }
            }
        }


        viewModelScope.launch {
            _verifyNewSenderResultFlow.getLatest {
                if (it.status == 17 || it.status == 15) {
                    successDialog(it.message) {
                        navigateUpWithResult("registrationState" to true)
                    }
                } else {
                    failureDialog(it.message)
                }
            }
        }


    }

    fun onRegisterAndVerifyButton() {

        when (senderRegistrationType.value) {
            SenderRegistrationType.NEW_REGISTER -> registerNewSender()
            SenderRegistrationType.VERIFY_SENDER -> verifyNewSender()
            SenderRegistrationType.VERIFY_AND_UPDATE -> verifyAndUpdateSender()
        }
    }


    private fun verifyAndUpdateSender() {

        val param = hashMapOf(
            "mobile" to moneySender.mobileNumber.orEmpty(),
            "firstName" to input.firstName.getValue(),
            "lastName" to input.lastName.getValue(),
            "pincode" to input.pinCode.getValue(),
            "address" to input.address.getValue(),
            "otp" to input.otp.getValue(),
            "state" to senderState.orEmpty(),
        )

        callApiForShareFlow(
            flow = _verifyNewSenderResultFlow,
            call = {
                repository.verifyAndUpdateSender(param)
            }
        )
    }

    private fun verifyNewSender() {

        val param = when (dmtDMType) {

            DMTType.DMT_3 -> hashMapOf(
                "mobile" to moneySender.mobileNumber.orEmpty(),
                "registrationOtp" to input.otp.getValue(),
                "stateId" to senderState.orEmpty(),
                "firstName" to input.firstName.getValue(),
                "lastName" to input.lastName.getValue(),
                "address" to input.address.getValue(),
                "pincode" to input.pinCode.getValue(),
            )
            else -> hashMapOf(
                "mobile" to moneySender.mobileNumber.orEmpty(),
                "otp" to input.otp.getValue(),
                "state" to senderState.orEmpty(),
            )

        }


        callApiForShareFlow(
            flow = _verifyNewSenderResultFlow,
            call = {
                if (dmtDMType == DMTType.DMT_3)
                    dmt3repository.verifySender(param)
                else repository.verifySender(param)
            }
        )
    }

    private fun registerNewSender() {

        val param = when (dmtDMType) {
            DMTType.DMT_3 -> hashMapOf(
                "mobile" to moneySender.mobileNumber.orEmpty(),
                "otpType" to "registrationOtp",
            )
            else -> hashMapOf(
                "mobile" to moneySender.mobileNumber.orEmpty(),
                "firstName" to input.firstName.getValue(),
                "lastName" to input.lastName.getValue(),
                "address" to input.address.getValue(),
                "pincode" to input.pinCode.getValue(),
            )
        }

        callApiForShareFlow(
            flow = _newRegisterResultFlow,
            call = {
                if (dmtDMType == DMTType.DMT_3)
                    dmt3repository.registerSender(param)
                else repository.registerSender(param)
            }
        )
    }

    fun onResendOtp() {

        val param = when (dmtDMType) {
            DMTType.DMT_3 -> hashMapOf(
                "mobile" to moneySender.mobileNumber.orEmpty(),
                "otpType" to "registrationOtp",
            )
            else -> hashMapOf(
                "mobile" to moneySender.mobileNumber.orEmpty()
            )
        }


        callApiForShareFlow(
            flow = _resendOtpResultFlow,
            call = {
                if (dmtDMType == DMTType.DMT_3)
                    dmt3repository.resendSenderRegistrationOtp(param)
                else repository.resendSenderRegistrationOtp(param)
            }
        )
    }


    val counterOtpVisibility: Boolean
        get() = senderRegistrationType.value != SenderRegistrationType.NEW_REGISTER

    val registerButtonText: String
        get() = when (senderRegistrationType.value) {
            SenderRegistrationType.NEW_REGISTER -> "Register New Sender"
            SenderRegistrationType.VERIFY_SENDER -> "Verify Sender"
            SenderRegistrationType.VERIFY_AND_UPDATE -> "Verify And Update"
        }


}

enum class SenderRegistrationType {
    NEW_REGISTER, VERIFY_SENDER, VERIFY_AND_UPDATE
}

data class SenderRegisterInput(
    val otpValidation: MutableState<Boolean>,
    val mobileNumber: InputWrapper = InputWrapper { AppValidator.mobile(it) },
    val firstName: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
    val lastName: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
    val pinCode: InputWrapper = InputWrapper { AppValidator.pinCodeValidation(it) },
    val address: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
    val otp: InputWrapper = InputWrapper(otpValidation) { AppValidator.otp(it) },
) : BaseInput(mobileNumber, firstName, lastName, pinCode, address, otp)