package com.a2z.app.ui.util.resource

sealed class ResultType<out T> {
    class Loading<out T> : ResultType<T>()
    data class Success<out T>(val data: T) : ResultType<T>()
    data class Failure<out T>(val exception: Exception) : ResultType<T>()
}



