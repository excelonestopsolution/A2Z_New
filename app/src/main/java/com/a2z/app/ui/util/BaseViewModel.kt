package com.a2z.app.ui.util

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a2z.app.ui.util.resource.BannerType
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.ui.util.resource.StatusDialogType
import com.a2z.app.util.AppUtil
import com.a2z.app.util.VoidCallback
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


data class NavigateTo(
    val route: String,
    val popUpAll: Boolean = false
)

data class ExceptionState(
    var exception: java.lang.Exception?,
    val popUp: Boolean
)


open class BaseViewModel : ViewModel() {
    val bannerState = mutableStateOf<BannerType>(BannerType.None)
    val dialogState = mutableStateOf<StatusDialogType>(StatusDialogType.None)
    val exceptionState = mutableStateOf<ExceptionState?>(null)
    val dashboardState = MutableSharedFlow<Boolean>()
    val navigateToFlow = MutableSharedFlow<NavigateTo>()
    val navigateUpWithResultFlow = MutableSharedFlow<Map<String, Any>>()

    fun showExceptionDialog(e: Exception, popUpScreen: Boolean = true) {
        val exception = ExceptionState(e, popUpScreen)
        viewModelScope.launch { exceptionState.value = exception }
    }

    fun gotoMainDashboard() {
        viewModelScope.launch { dashboardState.emit(true) }
    }

    private fun dismissDialog() {
        dialogState.value = StatusDialogType.None
    }

    fun progressDialog(message: String = "Loading") {
        AppUtil.logger("onProgress")
        dialogState.value = StatusDialogType.Progress(message)
    }

    fun transactionProgressDialog() {
        dialogState.value = StatusDialogType.Transaction
    }


    fun successDialog(message: String, callback: VoidCallback = {}) {
        dialogState.value = StatusDialogType.Success(message, callback)
    }

    fun failureDialog(message: String, callback: VoidCallback = {}) {
        dialogState.value = StatusDialogType.Failure(message, callback)
    }
    fun pendingDialog(message: String, callback: VoidCallback = {}) {
        dialogState.value = StatusDialogType.Pending(message, callback)
    }

    fun alertDialog(message: String, callback: VoidCallback = {}) {
        dialogState.value = StatusDialogType.Alert(message, callback)
    }


    fun setBanner(withNewType: BannerType = BannerType.None) {
        bannerState.value = withNewType
    }


    fun dismissBanner() {
        bannerState.value = BannerType.None
    }

    fun successBanner(title: String, message: String?) {
        bannerState.value = BannerType.Success(title, message.toString())
    }

    fun failureBanner(title: String, message: String) {
        bannerState.value = BannerType.Failure(title, message)
    }

    fun alertBanner(title: String, message: String?) {
        bannerState.value = BannerType.Alert(title, message.toString())
    }


    fun navigateTo(route: String, popUpAll: Boolean = false) {
        viewModelScope.launch { navigateToFlow.emit(NavigateTo(route, popUpAll = popUpAll)) }
    }

    fun navigateUpWithResult(vararg data: Pair<String, Any>) {
        viewModelScope.launch { navigateUpWithResultFlow.emit(data.toMap()) }
    }


    suspend fun <T> SharedFlow<ResultType<T>>.getLatest(
        failure: ((Exception) -> Unit?)? = null,
        progress: (() -> Unit?)? = null,
        success: suspend (T) -> Unit,
    ) {
        this.collectLatest {
            when (it) {
                is ResultType.Failure -> {
                    dismissDialog()
                    if (failure != null) failure(it.exception)
                }

                is ResultType.Loading -> {
                    if (progress != null) progress.invoke()
                    else progressDialog()
                }

                is ResultType.Success -> {
                    dismissDialog()
                    success(it.data)
                }
            }
        }
    }



}
