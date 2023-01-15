package com.a2z.app.ui.screen.kyc.document

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.kyc.DocumentKycDetail
import com.a2z.app.data.model.kyc.DocumentKycResponse
import com.a2z.app.data.repository.KycRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DocumentKycViewModel @Inject constructor(
    private val repository: KycRepository,
) : BaseViewModel() {

    val documentKycDetailResultFlow = resultStateFlow<DocumentKycResponse>()
    val selectedUri = mutableStateOf<Uri?>(null)
    private var selectedFile: File? = null
    private var selectDocType: DocumentKycType? = null
    var previewDialogState = mutableStateOf(false)
    var showImageState = mutableStateOf(false)
    var selectedUriState = mutableStateOf("")

    lateinit var docs: DocumentKycDetail

    private val _onUploadResponseResult = resultShareFlow<AppResponse>()

    init {
        fetchDocumentKycDetails()

        viewModelScope.launch {
            _onUploadResponseResult.getLatest {
                if (it.status == 1) successDialog(it.message) {
                    fetchDocumentKycDetails()
                }
                else failureDialog(it.message)
            }
        }
    }

    private fun fetchDocumentKycDetails() {

        callApiForStateFlow(
            flow = documentKycDetailResultFlow,
            call = { repository.documentKycDetails() },
            beforeEmit = {
                if (it is ResultType.Success) docs = it.data.data
            }
        )
    }

    fun getDocDetail(type: DocumentKycType): Triple<String, String, String> {
        val result = when (type) {
            DocumentKycType.PAN_CARD_FRONT -> Triple(
                "Pan Front",
                docs.pan_card_image,
                docs.pan_card_image_status
            )
            DocumentKycType.PROFILE_PHOTO -> Triple(
                "Profile Photo",
                docs.profile_picture,
                docs.profile_image_status
            )
            DocumentKycType.AADHAAR_FRONT -> Triple(
                "Aadhaar Front",
                docs.aadhaar_card_image,
                docs.aadhaar_front_image_status
            )
            DocumentKycType.AADHAAR_BACK -> Triple(
                "Aadhaar Back",
                docs.aadhaar_img_back,
                docs.aadhaar_back_image_status
            )
            DocumentKycType.SHOP_IMAGE -> Triple(
                "Shop Image",
                docs.shop_image,
                docs.shop_image_status
            )
            DocumentKycType.CANCEL_CHEQUE -> Triple(
                "Cancel Cheque",
                docs.cheque_image,
                docs.cheque_image_status
            )
            DocumentKycType.SEAL_CHEQUE -> Triple(
                "Seal Cheque",
                docs.seal_image,
                docs.seal_image_status
            )
            DocumentKycType.GST_IMAGE -> Triple(
                "GST Image",
                docs.gst_image,
                docs.gst_image_status
            )
        }
        return result
    }

    fun onPickFile(docType: DocumentKycType, uri: Uri?, file: File?) {
        this.selectedUri.value = uri
        this.selectedFile = file
        this.selectDocType = docType
        this.previewDialogState.value = true
    }

    fun uploadDoc() {
        callApiForShareFlow(
            flow = _onUploadResponseResult,
            call = {
                repository.uploadDocs(
                    panFilePart = getFilePart(),
                    profileFilePart = getFilePart(),
                    aadhaarFrontPart = getFilePart(),
                    aadhaarBackPart = getFilePart(),
                    shopPart = getFilePart(),
                    cancelChequePart = getFilePart(),
                    sealChequePart = getFilePart(),
                    gstPart = getFilePart(),
                )
            }
        )
    }

    private fun getFilePart(): MultipartBody.Part? {
        val partParamName = when (selectDocType) {
            DocumentKycType.PAN_CARD_FRONT -> "pan_card_image"
            DocumentKycType.PROFILE_PHOTO -> "profile_picture"
            DocumentKycType.AADHAAR_FRONT -> "aadhaar_card_image"
            DocumentKycType.AADHAAR_BACK -> "aadhaar_img_back"
            DocumentKycType.SHOP_IMAGE -> "shop_image"
            DocumentKycType.CANCEL_CHEQUE -> "cheque_image"
            DocumentKycType.SEAL_CHEQUE -> "seal_image"
            DocumentKycType.GST_IMAGE -> "gst_image"
            null -> ""
        }
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

enum class DocumentKycType {
    PAN_CARD_FRONT, PROFILE_PHOTO, AADHAAR_FRONT, AADHAAR_BACK, SHOP_IMAGE, CANCEL_CHEQUE,
    SEAL_CHEQUE, GST_IMAGE
}