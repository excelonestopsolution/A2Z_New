package com.a2z.app.ui.screen.indonepal.register_sender

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.model.indonepal.INCommonOtpResponse
import com.a2z.app.data.repository.IndoNepalRepository
import com.a2z.app.ui.screen.indonepal.INUtil
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class INRegisterSenderViewModel @Inject constructor(
    private val repository: IndoNepalRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val mobileNumber = savedStateHandle.get<String>("mobileNumber")!!

    val genderState = mutableStateOf(INUtil.genderList.first())
    val senderProofTypeState = mutableStateOf(INUtil.senderProofTypeList.first())
    val senderIncomeSourceState = mutableStateOf(INUtil.senderIncomeSourceList.first())

    val validateOtp = mutableStateOf(false)

    val input = InputForm(validateOtp)

    var processId : String = ""

    private val _sendOtpResultFlow = resultShareFlow<INCommonOtpResponse>()

    init {
        _sendOtpResultFlow.getLatest {
            if (it.status == 1) {
                validateOtp.value = true
                processId = it.data?.ProcessId ?: ""
            } else alertDialog(it.message)
        }

        fetchStaticData()
    }

    private fun fetchStaticData(){
        callApiForShareFlow { repository.staticData() }
    }

    fun onProceed() {
        if(processId.isEmpty()){
            alertBanner("OTP Error","Otp didn't fetched successfully!")
            return
        }


    }

    fun onSendOtp() {

        callApiForShareFlow(_sendOtpResultFlow) { repository.senderRegistrationOtp(hashMapOf(
            "number" to mobileNumber,
            "senderName" to input.fullName.getValue(),
            "dob" to input.senderDob.getValue(),
            "senderIdNumber" to senderProofTypeState.value,
            "type" to "CreateCustomer",
        )) }
    }


    data class InputForm(
        val validateOtp: MutableState<Boolean>,
        val fullName: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
        val senderWorkPlace: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
        val senderEmailId: InputWrapper = InputWrapper { AppValidator.email(it) },
        val senderProofType: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
        val senderCity: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
        val senderAddress: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
        val senderDob: InputWrapper = InputWrapper { AppValidator.dobValidation(it) },
        val otp: InputWrapper = InputWrapper(validateOtp) { AppValidator.otp(it) },
    ) : BaseInput(
        fullName, senderWorkPlace, senderEmailId, senderProofType, senderCity, senderAddress, otp
    )

}