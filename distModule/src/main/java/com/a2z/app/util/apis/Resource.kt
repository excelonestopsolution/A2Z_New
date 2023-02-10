package com.a2z.app.util.apis

sealed class Resource<out T> {
    class Loading<out T> : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure<out T>(val exception: Exception) : Resource<T>()
    class FailureWithOtherProblem<out T> : Resource<T>()
}