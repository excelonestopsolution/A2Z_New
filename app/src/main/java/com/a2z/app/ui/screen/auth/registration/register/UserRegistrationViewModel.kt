package com.a2z.app.ui.screen.auth.registration.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.auth.RegistrationResponse
import com.a2z.app.data.model.auth.RegistrationRole
import com.a2z.app.data.model.auth.RegistrationRoleUser
import com.a2z.app.data.repository.RegistrationRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.safeParcelable
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserRegistrationViewModel @Inject constructor(
    private val repository: RegistrationRepository, private val saveState: SavedStateHandle
) : BaseViewModel() {


    private val registrationRole: RegistrationRole? = saveState.safeParcelable("role")
    private val mappedUnder: RegistrationRoleUser? = saveState.safeParcelable("mapRole")
    private val selfRegister = saveState.get<String>("selfRegister").toBoolean()
    private val mobileNumber = saveState.get<String>("mobileNumber")

    val totalStepperCount = 4
    val selectedStepperIndex = mutableStateOf(1)

    val input = MobileEmailPanFormInput()
    val finalInput = FinalRegistrationFormInput()

    val confirmDialogState = mutableStateOf(UserRegistrationConfirmDialogState())

    val otpDialogState = mutableStateOf(false)
    val otpDialogMessage = mutableStateOf("")

    private val _postMobileResultFlow = resultShareFlow<RegistrationResponse>()
    private val _verifyMobileResultFlow = resultShareFlow<RegistrationResponse>()
    private val _postEmailResultFlow = resultShareFlow<RegistrationResponse>()
    private val _verifyEmailResultFlow = resultShareFlow<RegistrationResponse>()
    private val _postPanNumberResultFlow = resultShareFlow<RegistrationResponse>()
    private val _registrationResultFlow = resultShareFlow<RegistrationResponse>()

    private var requestId = ""
    val finalResponseState = mutableStateOf<RegistrationResponse?>(null)


    val title : String
    get() = if(selfRegister) "User Registration" else "Create ${registrationRole?.title}"

    val subTitle : String?
    get() = if(mappedUnder == null) null else "Under - "+ mappedUnder.userDetails

    init {

        if(mobileNumber != null && mobileNumber.isNotEmpty())
        {
            input.mobile.setValue(mobileNumber)
            onProceed()

        }

        validateMobileEmailPanInput()

        viewModelScope.launch {
            _postMobileResultFlow.getLatest {
                when (it.status) {
                    1, 8 -> {
                        requestId = it.requestId
                        otpDialogMessage.value =
                            "Otp has sent to your mobile number, please enter below"
                        otpDialogState.value = true
                    }
                    9 -> {
                        requestId = it.requestId
                        input.mobile.setValue(it.mobile)
                        successDialog(it.message) {
                            selectedStepperIndex.value = 2
                            validateMobileEmailPanInput()

                        }
                    }
                    10 -> {
                        requestId = it.requestId
                        input.mobile.setValue(it.mobile)
                        input.email.setValue(it.email_id)

                        successDialog(it.message) {
                            selectedStepperIndex.value = 2
                            otpDialogMessage.value =
                                "Otp has sent to you email Id, please check Inbox and Spam mail too. Please enter below"
                            otpDialogState.value = true
                        }

                    }
                    11 -> {

                        requestId = it.requestId
                        input.mobile.setValue(it.mobile)
                        successDialog(it.message) {
                            selectedStepperIndex.value = 3
                            validateMobileEmailPanInput()
                        }
                    }
                    17 -> {
                        requestId = it.requestId
                        input.mobile.setValue(it.details.mobile)
                        input.email.setValue(it.details.email)
                        input.pan.setValue(it.details.pan_card)
                        finalResponseState.value = it
                        selectedStepperIndex.value = 4
                    }
                    else -> failureDialog(it.message)
                }
            }
        }

        viewModelScope.launch {
            _verifyMobileResultFlow.getLatest {
                when (it.status) {
                    5, 9 -> {
                        requestId = it.requestId
                        successDialog(it.message) {
                            selectedStepperIndex.value = 2
                            validateMobileEmailPanInput()
                        }
                    }
                    6 -> alertDialog(it.message) {
                        otpDialogState.value = true
                    }
                    else -> failureDialog(it.message)
                }
            }
        }

        viewModelScope.launch {
            _postEmailResultFlow.getLatest {
                when (it.status) {
                    3 -> {
                        requestId = it.requestId
                        successDialog(it.message) {
                            selectedStepperIndex.value = 3
                            validateMobileEmailPanInput()
                        }
                    }
                    20 -> {
                        requestId = it.requestId
                        otpDialogMessage.value =
                            "Otp has sent to you email Id, please check Inbox and Spam mail too. Please enter below"
                        otpDialogState.value = true
                    }
                    else -> failureDialog(it.message)
                }
            }
        }

        viewModelScope.launch {
            _verifyEmailResultFlow.getLatest {
                when (it.status) {
                    3 -> {
                        requestId = it.requestId
                        successDialog(it.message) {
                            selectedStepperIndex.value = 3
                            validateMobileEmailPanInput()
                        }
                    }
                    else -> failureDialog(it.message) {
                        if (it.status == 18) otpDialogState.value = true
                    }
                }
            }
        }

        viewModelScope.launch {
            _postPanNumberResultFlow.getLatest {
                if (it.status == 17) {

                    successDialog(it.message) {
                        requestId = it.requestId
                        input.mobile.setValue(it.details.mobile)
                        input.email.setValue(it.details.email)
                        input.pan.setValue(it.details.pan_card)
                        finalResponseState.value = it
                        selectedStepperIndex.value = 4

                    }
                } else failureDialog(it.message)
            }
        }

        viewModelScope.launch {
            _registrationResultFlow.getLatest {
                if (it.status == 1) successDialog(it.message) {
                    if (selfRegister) navigateUpWithResult()
                    else navigateTo(NavScreen.DashboardScreen.route, true)
                }
                else failureDialog(it.message)
            }
        }
    }


    private fun validateMobileEmailPanInput() {
        when (selectedStepperIndex.value) {
            1 -> {
                input.mobile.useValidation.value = true
                input.email.useValidation.value = false
                input.pan.useValidation.value = false
            }
            2 -> {
                input.mobile.useValidation.value = false
                input.email.useValidation.value = true
                input.pan.useValidation.value = false
            }
            3 -> {
                input.mobile.useValidation.value = false
                input.email.useValidation.value = false
                input.pan.useValidation.value = true
            }
        }
    }

    fun onProceed() {
        when (selectedStepperIndex.value) {
            1 -> {
                confirmDialogState.value = UserRegistrationConfirmDialogState(
                    showDialog = true,
                    title = "Please confirm provided Mobile Number.",
                    title2 = "Mobile Number once submitted can not be change.",
                    data = input.mobile.getValue()
                )
            }
            2 -> {
                confirmDialogState.value = UserRegistrationConfirmDialogState(
                    showDialog = true,
                    title = "Please confirm provided Email ID.",
                    title2 = "Email ID once submitted can not be change.",
                    data = input.email.getValue()
                )
            }
            3 -> {
                confirmDialogState.value = UserRegistrationConfirmDialogState(
                    showDialog = true,
                    title = "Please confirm provided Pan Number.",
                    title2 = "Pan Number once submitted can not be change.",
                    data = input.pan.getValue()
                )
            }
            else -> {
                if (selfRegister) register() else registerFromUser()
            }
        }
    }

    fun onConfirm() {
        when (selectedStepperIndex.value) {
            1 -> submitMobileNumber()
            2 -> submitEmailId()
            3 -> submitPanNumber()
            else -> {}
        }
    }

    private fun submitMobileNumber() {
        callApiForShareFlow(flow = _postMobileResultFlow,
            call = {
                repository.postMobileNumber(
                    input.mobile.getValue(),
                    selfRegister.toString()
                )
            })
    }

    private fun verifyMobileNumber(otp: String) {
        callApiForShareFlow(flow = _verifyMobileResultFlow,
            call = { repository.mobileNumberVerify(requestId, otp, selfRegister.toString()) })
    }

    private fun submitEmailId() {
        callApiForShareFlow(flow = _postEmailResultFlow,
            call = {
                repository.postEmailId(
                    input.email.getValue(),
                    requestId,
                    selfRegister.toString()
                )
            })
    }

    private fun verifyEmailId(otp: String) {
        callApiForShareFlow(flow = _verifyEmailResultFlow,
            call = { repository.verifyEmailId(otp, requestId, selfRegister.toString()) })
    }

    private fun submitPanNumber() {
        callApiForShareFlow(flow = _postPanNumberResultFlow,
            call = {
                repository.postPanNumber(
                    requestId,
                    input.pan.getValue(),
                    selfRegister.toString()
                )
            })
    }

    fun onOtpSubmit(it: String) {
        when (selectedStepperIndex.value) {
            1 -> verifyMobileNumber(it)
            2 -> verifyEmailId(it)
        }
    }

    fun onResetPanVerification() {
        input.pan.setValue(finalResponseState.value?.details?.pan_card)
        selectedStepperIndex.value = 3
        validateMobileEmailPanInput()
    }

    private fun register() {
        callApiForShareFlow(flow = _registrationResultFlow, call = {
            repository.selfRegister(
                requestId = requestId,
                password = finalInput.password.getValue(),
                confirmPassword = finalInput.confirmPassword.getValue(),
                outletName = finalInput.shopName.getValue(),
                outletAddress = finalInput.shopAddress.getValue(),
            )

        })
    }

    private fun registerFromUser() {

        val param = if (mappedUnder !=null) hashMapOf(
            "role_id" to registrationRole!!.roleId.toString(),
            "requestId" to requestId,
            "password" to finalInput.password.getValue(),
            "confirm_password" to finalInput.confirmPassword.getValue(),
            "outlet_name" to finalInput.shopName.getValue(),
            "outlet_address" to finalInput.shopAddress.getValue(),
            "relationId" to mappedUnder.relationId.toString(),
            "parentId" to mappedUnder.id.toString(),
        )
        else hashMapOf(
            "role_id" to registrationRole!!.roleId.toString(),
            "requestId" to requestId,
            "password" to finalInput.password.getValue(),
            "confirm_password" to finalInput.confirmPassword.getValue(),
            "outlet_name" to finalInput.shopName.getValue(),
            "outlet_address" to finalInput.shopAddress.getValue(),

            )

        callApiForShareFlow(_registrationResultFlow) { repository.registerFromDistributor(param) }
    }

    data class MobileEmailPanFormInput(
        val mobile: InputWrapper = InputWrapper { AppValidator.mobile(it) },
        val email: InputWrapper = InputWrapper { AppValidator.email(it) },
        val pan: InputWrapper = InputWrapper { AppValidator.pan(it) },
    ) : BaseInput(mobile, email, pan)


    data class FinalRegistrationFormInput(
        val shopName: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
        val shopAddress: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
        val password: InputWrapper = InputWrapper { AppValidator.password(it) },
        val confirmPassword: InputWrapper = InputWrapper {
            AppValidator.confirmPassword(
                it, password.getValue()
            )
        },
    ) : BaseInput(shopName, shopAddress, password, confirmPassword)

}

