package com.a2z.app.ui.screen.util.indonepal

import android.app.DownloadManager
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.indonepal.INActivationInitialResponse
import com.a2z.app.data.repository.IndoNepalRepository
import com.a2z.app.ui.screen.indonepal.INUtil
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.PrimaryColorLight
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class INServiceActivationViewModel @Inject constructor(
    private val repository: IndoNepalRepository,
    val appPreference: AppPreference
) : BaseViewModel() {

    val staticData = INUtil.staticServiceData()

    val input = FormInput()

    val initialDataResultResponse = resultStateFlow<INActivationInitialResponse>()

    val selectedFile = mutableStateOf<File?>(null)

    val confirmDialogState = mutableStateOf(false)

    private val _documentUploadResultFlow = resultShareFlow<AppResponse>()
    private val _activateServiceResulFlow = resultShareFlow<AppResponse>()
    private val _courierDataResultFlow = resultShareFlow<AppResponse>()

    init {
        fetchInitialData()

        _documentUploadResultFlow.getLatest {
            if (it.status == 1) successDialog(it.message) {
                fetchInitialData()
            }
            else alertDialog(it.message)
        }

        _activateServiceResulFlow.getLatest {
            if (it.status == 1) {
                successDialog(it.message) {
                    fetchInitialData()
                }
            } else alertDialog(it.message)
        }


        _courierDataResultFlow.getLatest {
            if (it.status == 1) {
                successDialog(it.message) {
                    fetchInitialData()
                }
            } else alertDialog(it.message)
        }
    }

    private fun fetchInitialData() {

        callApiForStateFlow(initialDataResultResponse)
        { repository.fetchActivationInitialData() }
    }

    fun randomColor(index: Int): Color {
        return if (index == 0) Color.DarkGray
        else if (index % 2 == 0) GreenColor
        else if (index % 3 == 0) RedColor
        else PrimaryColorLight


    }


    fun onDownloadForm(type: DownloadFormType) {
        when (type) {
            DownloadFormType.SAMPLE -> TODO()
            DownloadFormType.FORM -> TODO()
        }
    }

    fun onActivation() {


        callApiForShareFlow(_activateServiceResulFlow)
        { repository.activateIndoNepalService(hashMapOf("na" to "na")) }
    }

    fun uploadDoc() {

        if(selectedFile.value ==null){
            alertBanner("PDF Document","select pdf document")
            return
        }

        val multiFile = RetrofitUtil.fileToMultipart(selectedFile.value, "document_image")
        callApiForShareFlow(_documentUploadResultFlow) { repository.uploadActivationDoc(multiFile) }

    }

    fun onCourierDataSubmit() {

        callApiForShareFlow(_courierDataResultFlow) {
            repository.submitCourierData(hashMapOf(
                "courierName" to input.courierName.getValue(),
                "docketNumber" to input.docketNumber.getValue(),
                "CourierDispatchDate" to input.courierDate.getValue()
            ))
        }
    }


    enum class DownloadFormType {
        SAMPLE, FORM
    }


    data class FormInput(
        val courierName : InputWrapper = InputWrapper{AppValidator.minThreeChar(it)},
        val docketNumber : InputWrapper = InputWrapper{AppValidator.minThreeChar(it)},
        val courierDate : InputWrapper = InputWrapper{AppValidator.dobValidation(it)},
    ) : BaseInput(courierName,docketNumber,courierDate)
}