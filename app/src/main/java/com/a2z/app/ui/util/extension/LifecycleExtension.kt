package com.a2z.app.ui.util.extension


import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.collectLifeCycleFlow(flow: Flow<T>, collector: FlowCollector<T>) {
    val owner = this
    owner.lifecycleScope.launch {
        owner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            flow.collect(collector)
        }
    }
}