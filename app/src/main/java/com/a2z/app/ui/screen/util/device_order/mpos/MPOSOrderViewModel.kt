package com.a2z.app.ui.screen.util.device_order.mpos

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.matm.MatmServiceInformationfoResponse
import com.a2z.app.data.model.matm.MposDocTypeResponse
import com.a2z.app.data.repository.MatmRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
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
class MPOSOrderViewModel @Inject constructor(
    private val repository: MatmRepository
) : BaseViewModel() {

    var businessLegalityTypeStatus: Int = 0
    var businessAddressTypeStatus: Int = 0
    val deviceInfoResultFlow = resultStateFlow<MatmServiceInformationfoResponse>()
    private val _uploadResultFlow = resultStateFlow<AppResponse>()
    private val _docTypeListResultFlow = resultShareFlow<MposDocTypeResponse>()
    val docTypeListState = mutableStateOf<MposDocTypeResponse?>(null)

    val selectedBusinessLegalityProofTypeState = mutableStateOf("")
    val selectedBusinessProofProofTypeState = mutableStateOf("")

    var selectedFileShopInside: File? = null
    var selectedFileShopOutside: File? = null
    var selectedFileShopBusinessProof: File? = null
    var selectedFileShopLegalityProof: File? = null


    init {
        fetchDeviceOrderInfo()
        viewModelScope.launch {
            _docTypeListResultFlow.getLatest {
                if (it.status == 1) {
                    docTypeListState.value = it
                } else {
                    docTypeListState.value = null
                    failureDialog("Oops something went wrong!") {
                        navigateUpWithResult()
                    }
                }
            }
        }

        viewModelScope.launch {
            _uploadResultFlow.getLatest {
                if (it.status == 1)successDialog(it.message){
                    fetchDeviceOrderInfo()
                } else {
                    failureDialog(it.message)
                }
            }
        }
    }

    private fun fetchDeviceOrderInfo() {

        callApiForStateFlow(
            flow = deviceInfoResultFlow,
            call = { repository.fetchOrderInfo() },
        )
    }

    fun fetchDocTypeList() {
        if (docTypeListState.value == null)
            callApiForShareFlow(
                flow = _docTypeListResultFlow,
                call = {
                    repository.getDocTypeList()
                },
                beforeEmit = {

                }
            )
    }

    fun onRefresh() {

        fetchDeviceOrderInfo()

    }

    fun onPickFile(docType: MPOSDocumentType, file: File?) {
        when (docType) {
            MPOSDocumentType.SHOP_INSIDE -> selectedFileShopInside = file
            MPOSDocumentType.SHOP_OUTSIDE -> selectedFileShopOutside = file
            MPOSDocumentType.BUSINESS_LEGALITY_PROOF -> selectedFileShopLegalityProof = file
            MPOSDocumentType.BUSINESS_ADDRESS_PROOF -> selectedFileShopBusinessProof = file
        }
    }

    fun onProceed() {

        if (selectedBusinessLegalityProofTypeState.value.isEmpty() ||
            selectedBusinessProofProofTypeState.value.isEmpty()
        ) {
            failureBanner(
                "Proof type",
                "Select both proof type"
            )
            return
        }

        if (
            selectedFileShopBusinessProof == null ||
            selectedFileShopInside == null ||
            selectedFileShopOutside == null ||
            selectedFileShopLegalityProof == null

        ) {
            failureBanner(
                "File not selected",
                "Select all 4 file image"
            )
            return
        }

        val shopInsideFilePart = getFilePart("shop_inside_image", selectedFileShopInside)
        val shopOutsideFilePart = getFilePart("shop_outside_image", selectedFileShopOutside)
        val businessLegalityFilePart =
            getFilePart("business_proof_image", selectedFileShopLegalityProof)
        val businessAddressFilePart =
            getFilePart("business_address_proof_image", selectedFileShopBusinessProof)
        val businessLegalityTypeBodyPart = getBodyPart(selectedBusinessLegalityProofTypeState.value)
        val businessAddressTypeBodyPart = getBodyPart(selectedBusinessProofProofTypeState.value)
        val latitudeBodyPart = getBodyPart("12312312")
        val longitudeBodyPart = getBodyPart("23123123")



        callApiForShareFlow(
            flow =_uploadResultFlow,
            call = {
                repository.uploadDetail(
                    shopInsideFilePart = shopInsideFilePart,
                    shopOutsideFilePart = shopOutsideFilePart,
                    businessLegalityFilePart = businessLegalityFilePart,
                    businessAddressFilePart = businessAddressFilePart,
                    businessAddressTypeBodyPart = businessAddressTypeBodyPart,
                    businessLegalityTypeBodyPart = businessLegalityTypeBodyPart,
                    latitudeBodyPart = latitudeBodyPart,
                    longitudeBodyPart = longitudeBodyPart,
                    )
            }
        )


    }

    private fun getBodyPart(param: String): RequestBody {
        return param.toRequestBody("multipart/form-data".toMediaTypeOrNull())
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

enum class MPOSDocumentType {
    SHOP_INSIDE, SHOP_OUTSIDE, BUSINESS_LEGALITY_PROOF, BUSINESS_ADDRESS_PROOF
}