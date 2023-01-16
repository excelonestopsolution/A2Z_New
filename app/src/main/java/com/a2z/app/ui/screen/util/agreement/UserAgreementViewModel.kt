package com.a2z.app.ui.screen.util.agreement

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AgreementInitialInfoResponse
import com.a2z.app.data.model.AgreementStartResponse
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.repository.AgreementRepository
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UserAgreementViewModel @Inject constructor(
    private val repository: AgreementRepository,
    appPreference: AppPreference
) : BaseViewModel() {
    val showWebViewContentState = mutableStateOf(false)

    val user = appPreference.user
    val buttonState = mutableStateOf(false)
    val input = FormInput()

    private val _generatePdfResultFlow = resultShareFlow<AppResponse>()
    private val _startAgreementResultFlow = resultShareFlow<AgreementStartResponse>()
    private val _checkStatusResultFlow = resultShareFlow<AppResponse>()
    private val _agreementDownloadResultFlow = resultShareFlow<AppResponse>()
    private val _agreementDownloadReportResultFlow = resultShareFlow<AppResponse>()
    val initialAgreementDetailResultFlow = resultStateFlow<AgreementInitialInfoResponse>()

    val startBrowserIntent = MutableSharedFlow<Boolean>()

    var orderId = ""
    var intentUrl = ""

    init {

        fetchAgreementDetail()

        viewModelScope.launch {
            _generatePdfResultFlow.getLatest {
                if (it.status == 1) startAgreement()
                else failureDialog(it.message)
            }
        }

        viewModelScope.launch {
            _startAgreementResultFlow.getLatest {
                if (it.status == 1) {
                    orderId = it.orderId
                    intentUrl = it.url
                    checkStatus(delayInSecond = 0)
                    delay(2000)
                    startBrowserIntent.emit(true)
                } else failureDialog(it.message)
            }
        }

        viewModelScope.launch {
            _checkStatusResultFlow.getLatest {
                when (it.status) {
                    1 -> agreementDownload()
                    2 -> checkStatus()
                    else -> alertDialog(it.message)
                }
            }
        }

        viewModelScope.launch {
            _agreementDownloadResultFlow.getLatest {
                if (it.status == 1) {
                    agreementDownloadReport()
                } else failureDialog(it.message)
            }
        }

        viewModelScope.launch {
            _agreementDownloadReportResultFlow.getLatest {
                if (it.status == 1) {
                    fetchAgreementDetail()
                } else failureDialog(it.message)
            }
        }
    }

    private fun fetchAgreementDetail() {

        callApiForStateFlow(
            flow = initialAgreementDetailResultFlow,
            call = { repository.fetchInitialAgreement() }
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun onDownloadAgreement(url: String, downloadManager: DownloadManager) {

        progressDialog("Downloading..")

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "A2Z_Agreement_${timeStamp}.pdf"
        val desc = "A2Z Suvidhaa Agreement File"
        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName)
            .setDescription(desc)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        downloadManager.enqueue(request)

        viewModelScope.launch {
            delay(1000)
            dismissDialog()
            successBanner("Agreement File","File has been downloaded")
        }

    }

    fun onFormSubmit() {
        showWebViewContentState.value = true
    }

    fun onUserAcceptAndProceed() {
        generateAgreementPDF()
    }

    private fun startAgreement() {
        callApiForShareFlow(
            flow = _startAgreementResultFlow,
            call = { repository.startAgreement(hashMapOf("undefine" to "undefine")) }
        )
    }

    private fun generateAgreementPDF() {
        callApiForShareFlow(
            flow = _generatePdfResultFlow,
            call = { repository.generateAgreementPdf() }
        )
    }

    private fun checkStatus(delayInSecond: Int = 10) {
        val param = hashMapOf("order_id" to orderId)
        callApiForShareFlow(
            flow = _checkStatusResultFlow,
            call = {
                delay(timeMillis = (1000 * delayInSecond).toLong())
                repository.checkStatus(param)
            }
        )
    }

    private fun agreementDownload() {
        val param = hashMapOf("order_id" to orderId)
        callApiForShareFlow(
            flow = _agreementDownloadResultFlow,
            call = {
                repository.agreementDownload(param)
            }
        )
    }

    private fun agreementDownloadReport() {
        val param = hashMapOf("order_id" to orderId)
        callApiForShareFlow(
            flow = _agreementDownloadReportResultFlow,
            call = {
                repository.agreementDownloadReport(param)
            }
        )
    }


    data class FormInput(
        val name: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
        val email: InputWrapper = InputWrapper { AppValidator.email(it) },
        val mobile: InputWrapper = InputWrapper { AppValidator.mobile(it) },
    ) : BaseInput(name, email, mobile)

}