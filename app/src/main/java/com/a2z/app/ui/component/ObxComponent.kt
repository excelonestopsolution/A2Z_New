package com.a2z.app.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.AppUtil
import com.a2z.app.util.Exceptions
import com.a2z.app.util.extension.showToast
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <T> ObsComponent(
    obs: LiveData<ResultType<T>>,
    onFailure: @Composable ((Exception) -> Unit?)? = null,
    onLoading: @Composable (() -> Unit?)? = null,
    onSuccess: @Composable (T) -> Unit,
) {

    val navController = LocalNavController.current

    var isNavigated = false

    when (val result = obs.observeAsState().value!!) {
        is ResultType.Failure -> {
            if (onFailure != null) {
                onFailure(result.exception)
            } else {
                ObsExceptionComponent(exceptionState = result.exception)
                val e = result.exception is Exceptions.SessionExpiredException
                LocalContext.current.showToast(e.toString())
                navController.navigate(NavScreen.ExceptionScreen.passArgs(result.exception))

            }


        }
        is ResultType.Loading -> {
            AppUtil.logger("Call Loading Testing...")
            if (onLoading != null) onLoading()
            else AppProgress()
        }
        is ResultType.Success -> onSuccess(result.data)
    }
}

@Composable
fun <T> ObsComponent(
    flow: StateFlow<ResultType<T>>,
    onFailure: @Composable ((Exception) -> Unit?)? = null,
    onLoading: @Composable (() -> Unit?)? = null,
    onSuccess: @Composable (T) -> Unit,
) {


    when (val result = flow.collectAsState(ResultType.Loading()).value) {
        is ResultType.Failure -> {
            if (onFailure != null) onFailure(result.exception)
            ObsExceptionComponent(exceptionState = result.exception)

            println(result.exception.stackTrace)
        }
        is ResultType.Loading -> {
            AppUtil.logger("Call Loading Testing...")
            if (onLoading != null) onLoading()
            else AppProgress()
        }
        is ResultType.Success -> onSuccess(result.data)
    }
}