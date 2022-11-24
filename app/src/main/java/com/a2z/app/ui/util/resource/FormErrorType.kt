package com.a2z.app.ui.util.resource

sealed class FormErrorType<out String> {
    object Initial : FormErrorType<Nothing>()
    object Success : FormErrorType<Nothing>()
    data class Failure<out String>(val msg: String) : FormErrorType<String>()
}

