package com.a2z.app.ui.screen.util.device_order

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.matm.MatmServiceInformation
import com.a2z.app.data.model.matm.MatmServiceInformationfoResponse
import com.a2z.app.data.repository.MatmRepository
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.ApiUtil
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MATMOderViewModel @Inject constructor(
    private val repository: MatmRepository,
    private val appPreference: AppPreference,
    private val apiUtil: ApiUtil
) : BaseViewModel() {

    private lateinit var matmInfoData: MatmServiceInformation
    val isMatmReceivedState = mutableStateOf(false)
    val orderConfirmDialogState = mutableStateOf(false)
    private val _orderDeviceResultFlow = resultShareFlow<AppResponse>()
    val deviceInfoResultFlow = resultStateFlow<MatmServiceInformationfoResponse>()

    val courierAddressCheckBoxState = mutableStateOf(false)

    val courierAddressValidation = mutableStateOf(true)
    val input = MATMDeviceOrderInput(courierAddressValidation)

    val selectedImageUrl = mutableStateOf("")
    val showImageDialog = mutableStateOf(false)

    var selectedPanFile: File? = null
    var selectedAddressProofFile: File? = null

    private val _requestOtpResultFlow = resultShareFlow<AppResponse>()
    private val _verifyOtpResultFlow = resultShareFlow<AppResponse>()
    private val _onUploadResultFlow = resultShareFlow<AppResponse>()

    val otpSentState = mutableStateOf(false)

    val otpInput = MATMDeviceOtpInput()

    init {

        fetchDeviceOrderInfo()

        viewModelScope.launch {
            _orderDeviceResultFlow.getLatest {
                if (it.status == 1) {
                    successDialog(it.message) {
                        fetchDeviceOrderInfo()
                    }
                } else failureDialog(it.message)
            }
        }

        viewModelScope.launch {
            _requestOtpResultFlow.getLatest {
                if (it.status == 1) {
                    otpSentState.value = true
                    successBanner(
                        "Verity Otp",
                        it.message
                    )
                } else failureDialog(it.message)
            }
        }

        viewModelScope.launch {
            _verifyOtpResultFlow.getLatest {
                if (it.status == 1) {
                    successDialog(it.message) {
                        fetchDeviceOrderInfo()
                    }
                } else failureDialog(it.message)
            }
        }

        viewModelScope.launch {
            _onUploadResultFlow.getLatest {
                if (it.status == 1) {
                    successDialog(it.message) {
                        fetchDeviceOrderInfo()
                    }
                } else failureDialog(it.message)
            }
        }
    }

    private fun fetchDeviceOrderInfo() {

        callApiForStateFlow(
            flow = deviceInfoResultFlow,
            call = { repository.fetchOrderInfo() },
            beforeEmit = {
                if (it is ResultType.Success) setupInformation(it.data)
            }
        )
    }

    private fun setupInformation(it: MatmServiceInformationfoResponse) {

        if (it.data == null) return
        matmInfoData = it.data
        when (it.status) {
            1 -> if (it.data.service_status != "4") {

                val user = appPreference.user

                val name = user!!.name.trim()
                if (name.isNotEmpty()) {
                    input.name.setValue(name)
                    input.name.readOnly.value = true
                }

                val mobile = user.mobile.trim()
                if (mobile.isNotEmpty()) {
                    input.mobile.setValue(mobile)
                    input.mobile.readOnly.value = true
                }

                val email = user.email.trim()
                if (email.isNotEmpty()) {
                    input.email.setValue(email)
                    input.email.readOnly.value = true
                }

                val aadhaarNumber = user.aadhaarNumber.trim()
                if (aadhaarNumber.isNotEmpty()) {
                    input.aadhaarNumber.setValue(aadhaarNumber)
                    input.aadhaarNumber.readOnly.value = true
                }

                val shopName = user.shopName.toString()
                if (shopName.isNotEmpty()) {
                    input.shopName.setValue(shopName)
                    input.shopName.readOnly.value = true
                }

                val shopAddress = user.shopAddress.toString()
                if (shopAddress.isNotEmpty()) {
                    input.shopAddress.setValue(shopAddress)
                    input.shopAddress.readOnly.value = true
                }
                val pinCode = user.pinCode.trim()
                if (pinCode.isNotEmpty()) {
                    input.pinCode.setValue(pinCode)
                    input.pinCode.readOnly.value = true
                }
            }
            else -> failureDialog(it.message) { navigateUpWithResult() }
        }
    }

    fun onConfirmOrderDevice() {
        callApiForShareFlow(
            flow = _orderDeviceResultFlow,
            call = { repository.orderNow() }
        )
    }

    fun onFormSubmit() {

        if (selectedPanFile == null || selectedAddressProofFile == null) {
            failureBanner(
                "File required",
                "Select Pan or Address Proof image file"
            )
            return
        }

        fun getBodyPart(param: String): RequestBody {
            return param.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }

        val matmReceiveStatus = if (isMatmReceivedState.value) "1" else "0"

        val mGstNumber = matmInfoData.gstNumber ?: input.gstNumber.getValue()
        val mShopAddress = matmInfoData.shopAddress ?: input.shopAddress.getValue()
        val mMobileNumber = matmInfoData.mobile ?: input.mobile.getValue()
        val mEmailId = matmInfoData.email ?: input.email.getValue()
        val mShopName = matmInfoData.shopName ?: input.shopName.getValue()
        val mLandmark = matmInfoData.landmark ?: input.landmark.getValue()
        val mPinCode = matmInfoData.pinCode ?: input.pinCode.getValue()
        val mName = matmInfoData.name ?: input.name.getValue()
        val mCity = matmInfoData.city ?: input.city.getValue()
        val mAadhaarNumber = matmInfoData.aadhaarNumber ?: input.aadhaarNumber.getValue()
        val mPanNumber = matmInfoData.panNumber ?: input.panNumber.getValue()

        val mCourierAddress = matmInfoData.courierAddress ?: if(courierAddressCheckBoxState.value)
            input.shopAddress.getValue() else input.courierAddress.getValue()

        callApiForShareFlow(
            flow = _onUploadResultFlow,
            call = {
                repository.uploadDetail(
                    panCardFilePart = getFilePart("pan_image", selectedPanFile),
                    addressProofFilePart = getFilePart(
                        "address_proof_image",
                        selectedAddressProofFile
                    ),
                    gstNumberBodyPart = getBodyPart(mGstNumber),
                    shopAddressBodyPart = getBodyPart(mShopAddress),
                    mobileBodyPart = getBodyPart(mMobileNumber),
                    emailBodyPart = getBodyPart(mEmailId),
                    shopNameBodyPart = getBodyPart(mShopName),
                    courierAddressBodyPart = getBodyPart(mCourierAddress),
                    landmarkBodyPart = getBodyPart(mLandmark),
                    pinCodeBodyPart = getBodyPart(mPinCode),
                    nameBodyPart = getBodyPart(mName),
                    cityBodyPart = getBodyPart(mCity),
                    aadhaarBodyPart = getBodyPart(mAadhaarNumber),
                    panBodyPart = getBodyPart(mPanNumber),
                    matmReceivedBodyPart = getBodyPart(matmReceiveStatus),
                    latitudeBodyPart = getBodyPart("12313"),
                    longitudeBodyPart = getBodyPart("123123")
                )
            }
        )

    }


    fun onEditButton() {
        input.name.readOnly.value = false
        input.mobile.readOnly.value = false
        input.email.readOnly.value = false
        input.shopName.readOnly.value = false
        input.shopAddress.readOnly.value = false
        input.pinCode.readOnly.value = false
        input.aadhaarNumber.readOnly.value = false
    }

    fun onPickFile(docType: MATMDocumentType, file: File?) {
        when (docType) {
            MATMDocumentType.PAN_CARD -> selectedPanFile = file
            MATMDocumentType.ADDRESS_PROOF -> selectedAddressProofFile = file
        }
    }

    fun requestVerifyOrderOtp() {

        callApiForShareFlow(
            flow = _requestOtpResultFlow,
            call = { repository.requestOtp() }
        )

    }

    fun onVerifyOtp() {
        callApiForShareFlow(
            flow = _verifyOtpResultFlow,
            call = { repository.verifyOtp(otpInput.otp.getValue()) }
        )
    }


    private fun getFilePart(partParamName: String, selectedFile: File?): MultipartBody.Part? {

        return if (partParamName.isNotEmpty())
            getMultipartFormData(selectedFile, partParamName)
        else null


    }

    private fun getMultipartFormData(file: File?, fileField: String): MultipartBody.Part? {
        val requestBody = getRequestBody(file) ?: return null
        return MultipartBody.Part.createFormData(fileField, file?.name, requestBody)
    }


    private fun getRequestBody(file: File?) =
        file?.asRequestBody("multipart/form-data".toMediaTypeOrNull())


}


enum class MATMDocumentType {
    PAN_CARD, ADDRESS_PROOF
}

data class MATMDeviceOtpInput(
    val otp: InputWrapper = InputWrapper { AppValidator.otp(it) }
) : BaseInput(otp)

data class MATMDeviceOrderInput(
    val courierAddressValidation: MutableState<Boolean>,
    val name: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
    val mobile: InputWrapper = InputWrapper { AppValidator.mobile(it) },
    val email: InputWrapper = InputWrapper { AppValidator.email(it) },
    val shopName: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
    val shopAddress: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
    val landmark: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
    val city: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
    val pinCode: InputWrapper = InputWrapper { AppValidator.pinCodeValidation(it) },
    val panNumber: InputWrapper = InputWrapper { AppValidator.pan(it) },
    val aadhaarNumber: InputWrapper = InputWrapper { AppValidator.aadhaarValidation(it) },
    val courierAddress: InputWrapper = InputWrapper(courierAddressValidation) {
        AppValidator.minThreeChar(
            it
        )
    },
    val gstNumber: InputWrapper = InputWrapper { AppValidator.empty(it) },
) : BaseInput(
    name,
    mobile,
    email,
    shopName,
    shopAddress,
    landmark,
    city,
    pinCode,
    panNumber,
    aadhaarNumber,
    courierAddress
)