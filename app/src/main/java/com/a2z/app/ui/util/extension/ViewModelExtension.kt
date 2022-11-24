package com.a2z.app.ui.util.extension

import androidx.lifecycle.viewModelScope
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.resource.ResultType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


fun <T> BaseViewModel.callApiForFlow(
    flow: MutableStateFlow<ResultType<T>>? = null,
    beforeEmit: (ResultType<T>) -> Unit = {},
    useExceptionNavigator: Boolean = true,
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
            if (useExceptionNavigator) gotoExceptionScreen(e)
            beforeEmit(ResultType.Failure(e))
            mFlow.emit(ResultType.Failure(e))
        }
    }
    return mFlow
}

fun <T> BaseViewModel.callApiForShareFlow(
    flow: MutableSharedFlow<ResultType<T>>? = null,
    beforeEmit: (ResultType<T>) -> Unit = {},
    useExceptionNavigator: Boolean = true,
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
            if (useExceptionNavigator) gotoExceptionScreen(e)
            beforeEmit(ResultType.Failure(e))
            mFlow.emit(ResultType.Failure(e))
        }
    }
    return mFlow
}




