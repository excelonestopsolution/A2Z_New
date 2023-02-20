package com.a2z.app.ui.screen.indonepal.register_sender

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.indonepal.INCommonOtpResponse
import com.a2z.app.data.model.indonepal.INDistrict
import com.a2z.app.data.model.indonepal.INDistrictResponse
import com.a2z.app.data.model.indonepal.INStaticData
import com.a2z.app.data.repository.IndoNepalRepository
import com.a2z.app.ui.screen.indonepal.INUtil
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.extension.insertDateSeparator
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class INRegisterSenderViewModel @Inject constructor(
    private val repository: IndoNepalRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    val mobileNumber = savedStateHandle.get<String>("mobileNumber")!!

    var staticData: INStaticData = INUtil.staticData()

    val genderState = mutableStateOf("")
    val senderProofTypeState = mutableStateOf("")
    val senderIncomeSourceState = mutableStateOf("")
    val countryStateState = mutableStateOf("")
    val districtState = mutableStateOf("")

    val validateOtp = mutableStateOf(false)

    val input = InputForm(validateOtp)

    private var processId: String = ""

    private val _sendOtpResultFlow = resultShareFlow<INCommonOtpResponse>()
    private val _districtListResultFlow = resultShareFlow<INDistrictResponse>()
    private val _registerResultFlow = resultShareFlow<AppResponse>()

    val districtList = mutableStateListOf<INDistrict>()

    init {
        _sendOtpResultFlow.getLatest {
            if (it.status == 1) {
                validateOtp.value = true
                processId = it.data?.ProcessId ?: ""
            } else alertDialog(it.message)
        }


        _districtListResultFlow.getLatest {
            districtList.addAll(it.data)
            districtState.value = ""
        }

        _registerResultFlow.getLatest {
            if(it.status ==1) successDialog(it.message){
                navigateUpWithResult("isRegistered" to true)
            }
            else alertDialog(it.message)
        }
    }


    fun onProceed() {

        if (!validateDropDownInput()) return

        if (processId.isEmpty()) {
            alertBanner("OTP Error", "Otp didn't fetched successfully!")
            return
        }

        val senderIdType = staticData.senderProofType.find { it.name == senderProofTypeState.value }
        val countryState = staticData.stateLists.find { it.name == countryStateState.value }
        val disctrict = districtList.find { it.distic == districtState.value }
        val incomeSource = staticData.incomeSource.find { it.name == senderIncomeSourceState.value }

        val param = hashMapOf(
            "senderName" to input.fullName.getValue(),
            "gender" to genderState.value,
            "dob" to "04-05-1998",
            "senderAddress" to input.senderAddress.getValue(),
            "senderMobileNumber" to mobileNumber,
            "employer" to input.senderWorkPlace.getValue(),
            "senderCountry" to "India",
            "senderTypeId" to senderIdType?.id.toString(),
            "senderIdNumber" to input.senderProofType.getValue(),
            "senderState" to countryState?.id.toString(),
            "senderDistic" to disctrict?.distic.toString(),
            "senderCity" to input.senderCity.getValue(),
            "incomeSource" to incomeSource?.id.toString(),
            "nationality" to "Nepalese",
            "senderOtp" to input.otp.getValue(),
            "ProcessId" to processId,
            "senderEmail" to input.senderEmailId.getValue(),
        )

        callApiForShareFlow(_registerResultFlow){
            repository.senderRegistrationOtpVerify(param)
        }

    }

    fun onSendOtp() {

        if (!validateDropDownInput()) return

        val senderProofType =
            staticData.senderProofType.find { it.name == senderProofTypeState.value }!!

        callApiForShareFlow(_sendOtpResultFlow) {
            repository.senderRegistrationOtp(
                hashMapOf(
                    "number" to mobileNumber,
                    "senderName" to input.fullName.getValue(),
                    "dob" to input.senderDob.getValue(),
                    "senderIdNumber" to senderProofType.id,
                    "type" to "CreateCustomer",
                )
            )
        }
    }

    private fun validateDropDownInput(): Boolean {

        if (genderState.value.isEmpty()) {
            alertBanner("Sender Gender", "Field can't be empty")
            return false
        }
        if (senderProofTypeState.value.isEmpty()) {
            alertBanner("Sender Proof Type", "Field can't be empty")
            return false
        }
        if (senderIncomeSourceState.value.isEmpty()) {
            alertBanner("Sender Income Source", "Field can't be empty")
            return false
        }
        if (countryStateState.value.isEmpty()) {
            alertBanner("Country State", "Field can't be empty")
            return false
        }
        return true

    }

    fun onCountryStateChanged(value: String) {
        val stateId = staticData.stateLists.find {
            it.name == value
        }!!.id
        callApiForShareFlow(_districtListResultFlow) {
            repository.fetchDistrict(stateId)
        }
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
        fullName, senderWorkPlace, senderEmailId, senderProofType,
        senderCity, senderAddress, otp
    )

}