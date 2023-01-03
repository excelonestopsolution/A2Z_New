package com.a2z.app.util

import androidx.compose.runtime.Composable
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.ui.util.resource.FormErrorType
import kotlinx.coroutines.flow.MutableSharedFlow

typealias VoidCallback = () -> Unit
typealias ToggleBottomSheet = () -> Unit
typealias  Callback<T> = (T) -> Unit
typealias FunCompose = @Composable () -> Unit
typealias FormFieldError = FormErrorType<String>
typealias PairRequest = Pair<String,String>
typealias MapRequest = Map<String,String>


fun <T> resourceMutableShareFlow() = MutableSharedFlow<ResultType<T>>()


