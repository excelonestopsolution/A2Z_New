package com.a2z.app.ui.util.extension

import androidx.lifecycle.viewModelScope
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.resource.ResultType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


fun <T> BaseViewModel.callApiForStateFlow(
    flow: MutableStateFlow<ResultType<T>>? = null,
    beforeEmit: (ResultType<T>) -> Unit = {},
    showExceptionDialog: Boolean = true,
    popUpOnException: Boolean = true,
    call: suspend () -> T
): MutableSharedFlow<ResultType<T>> {

    val mFlow = flow ?: MutableSharedFlow()
    viewModelScope.launch(Dispatchers.IO) {
        beforeEmit(ResultType.Loading())
        mFlow.emit(ResultType.Loading())
        try {
            val response = call()
            beforeEmit(ResultType.Success(response))
            mFlow.emit(ResultType.Success(response))
        } catch (e: Exception) {
            if (showExceptionDialog) showExceptionDialog(e, popUpOnException)
            beforeEmit(ResultType.Failure(e))
            mFlow.emit(ResultType.Failure(e))
        }
    }
    return mFlow
}

fun <T> BaseViewModel.callApiForShareFlow(
    flow: MutableSharedFlow<ResultType<T>>? = null,
    beforeEmit: suspend (ResultType<T>) -> Unit = {},
    handleException: Boolean = true,
    popUpScreen: Boolean = true,
    jobCallback: ((Job) -> Unit)? = null,
    call: suspend () -> T
): MutableSharedFlow<ResultType<T>> {

    val mFlow = flow ?: MutableSharedFlow()
    val job = viewModelScope.launch(Dispatchers.IO) {
        beforeEmit(ResultType.Loading())
        mFlow.emit(ResultType.Loading())
        try {
            val response = call()
            beforeEmit(ResultType.Success(response))
            mFlow.emit(ResultType.Success(response))
        } catch (e: Exception) {
            if (handleException) {
                if (e !is CancellationException) showExceptionDialog(e, popUpScreen)
            }
            beforeEmit(ResultType.Failure(e))
            mFlow.emit(ResultType.Failure(e))
        }
    }
    jobCallback?.invoke(job)
    return mFlow
}




