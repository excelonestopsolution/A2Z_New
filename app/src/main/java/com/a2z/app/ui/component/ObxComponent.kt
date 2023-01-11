package com.a2z.app.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.a2z.app.ui.util.ExceptionState
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.AppUtil
import com.a2z.app.util.VoidCallback
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow


@Composable
fun <T> ObsComponent(
    flow: StateFlow<ResultType<T>>,
    onRetry: VoidCallback? = null,
    onFailure: @Composable ((Exception) -> Unit?)? = null,
    onLoading: @Composable (() -> Unit?)? = null,
    onSuccess: @Composable (T) -> Unit,
) {
    when (val result = flow.collectAsState(ResultType.Loading()).value) {

        is ResultType.Failure -> {
            if (onFailure != null) onFailure(result.exception)
            if (onRetry != null) RetryComponent {
                onRetry.invoke()
            }
        }
        is ResultType.Loading -> {
            if (onLoading != null) onLoading()
            else AppProgress()
        }
        is ResultType.Success -> onSuccess(result.data)
    }
}

@Composable
fun RetryComponent(onClick : VoidCallback) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Connection Interrupt!")
            TextButton(onClick = onClick) {
                Text(text = "Try Again")
            }
        }
    }


}