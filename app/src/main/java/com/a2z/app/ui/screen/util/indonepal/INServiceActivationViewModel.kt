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
import com.a2z.app.ui.util.BaseViewModel
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
    private val apiUtil: ApiUtil,
    val appPreference: AppPreference
) : BaseViewModel() {

    val staticData = INUtil.staticServiceData()

    val initialDataResultResponse = resultStateFlow<INActivationInitialResponse>()

    val selectedFile = mutableStateOf<File?>(null)

    private val _documentUploadResultFlow = resultShareFlow<AppResponse>()

    init {
        fetchInitialData()

        _documentUploadResultFlow.getLatest {
            if (it.status == 1) successDialog(it.message) {
                fetchInitialData()
            }
            else alertDialog(it.message)
        }

    }

    private fun fetchInitialData() {

        callApiForStateFlow(initialDataResultResponse, beforeEmit = {
            if (it is ResultType.Success) {
                if (it.data.data.service_status == "1")
                    appPreference.user =
                    appPreference.user?.copy(indoNepal = "1")
            }
        })
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


    }

    fun uploadDoc() {

        val multiFile = RetrofitUtil.fileToMultipart(selectedFile.value, "document_image")
        callApiForShareFlow(_documentUploadResultFlow) { repository.uploadActivationDoc(multiFile) }

    }


    enum class DownloadFormType {
        SAMPLE, FORM
    }
}