package com.a2z.app.ui.screen.fund.request

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.fund.FundMethod
import com.a2z.app.data.model.fund.FundRequestBank
import com.a2z.app.data.repository.FundRepository
import com.a2z.app.ui.screen.fund.method.FundMethodType
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.ui.util.resource.StatusDialogType
import com.a2z.app.util.resultShareFlow
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FundRequestViewModel @Inject constructor(
    private val repository: FundRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    var selectedFile: File? = null
    var fundRequestBank: FundRequestBank? = null
    private var fundMethod: FundMethod? = null
    val input = FundRequestInput()
    private var paymentMode: Pair<String, String> = Pair("", "")

    val isImageDialogOpen = mutableStateOf(false)
    val selectedFileUri = mutableStateOf<Uri?>(null)

    private var requestTo: String = "1"
    private var remark: String = ""
    private var onlineMode: String = ""

    //api observers
    private val _requestResultResponseFlow = resultShareFlow<AppResponse>()


    init {


        val arg1 = savedStateHandle.get<String>("fundMethod")
        val arg2 = savedStateHandle.get<String>("fundRequestBank")

        fundMethod = Gson().fromJson(arg1,FundMethod::class.java)
        fundRequestBank = Gson().fromJson(arg2,FundRequestBank::class.java)


        paymentMode = setupPaymentMode()
        input.paymentModeInput.setValue(paymentMode.first)
        if (fundMethod?.type != null) requestTo = "2"

        viewModelScope.launch { subscribers() }
    }

    private suspend fun subscribers() {
        _requestResultResponseFlow.collect {
            when (it) {
                is ResultType.Failure -> {
                    dialogState.value = StatusDialogType.None
                    showExceptionDialog(it.exception)
                }
                is ResultType.Loading -> dialogState.value = StatusDialogType.Progress()
                is ResultType.Success -> dialogState.value =
                    StatusDialogType.Success(it.data.message)
            }
        }
    }


    fun getRefPlaceholder(): String? {
        return when (fundMethod?.type) {
            FundMethodType.BANK_TRANSFER -> fundRequestBank?.bank_transfer_place_holder
            FundMethodType.CASH_IN_CDM -> fundRequestBank?.cash_cdm_place_holder
            FundMethodType.CASH_DEPOSIT -> fundRequestBank?.cash_deposit_place_holder
            else -> null
        }
    }

    private fun setupPaymentMode(): Pair<String, String> {
        if (fundMethod == null) return Pair("CASH", "Cash")
        return when (fundMethod!!.type) {
            FundMethodType.BANK_TRANSFER -> Pair("Bank Transfer", "OnLine")
            FundMethodType.CASH_DEPOSIT -> Pair("Cash Deposit (Counter)", "Cash@Counter")
            FundMethodType.CASH_IN_CDM -> Pair("Cash Deposit (Machine)", "Cash@CDM")
            FundMethodType.CASH_COLLECT -> Pair("Cash Collect", "Cash@Collect")
            else -> Pair("", "")
        }
    }


    fun makeFundRequest() {

        val fileSlipPart = getMultipartFormData(selectedFile, "d_picture")
        val amountBody =
            input.amountInput.getValue().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val requestToBody = requestTo.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val paymentDateBody = input.paymentModeInput.getValue()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val paymentModeBody =
            paymentMode.second.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val refNumberBody =
            input.bankRefInput.getValue().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val onlineModeBody = onlineMode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val bankIdBody = fundRequestBank?.id?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val bankNameBody = fundRequestBank?.bankName?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val remarkBody = remark.toRequestBody("multipart/form-data".toMediaTypeOrNull())


        callApiForShareFlow(flow = _requestResultResponseFlow) {
            repository.submitRequest(
                slipFile = fileSlipPart,
                amount = amountBody,
                requestTo = requestToBody,
                paymentDate = paymentDateBody,
                paymentMode = paymentModeBody,
                refNumber = refNumberBody,
                onlineMode = onlineModeBody,
                bankId = bankIdBody,
                bankName = bankNameBody,
                remark = remarkBody,
            )
        }


    }

    private fun getMultipartFormData(file: File?, fileField: String): MultipartBody.Part? {
        val requestBody = getRequestBody(file) ?: return null
        return MultipartBody.Part.createFormData(fileField, file?.name, requestBody)
    }


    private fun getRequestBody(file: File?) =
        file?.asRequestBody("multipart/form-data".toMediaTypeOrNull())


}
