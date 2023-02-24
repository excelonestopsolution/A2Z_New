package com.a2z.app.ui.util.resource

sealed class StatusDialogType {
    object None : StatusDialogType()
    data class Progress(val message: String = "Loading") : StatusDialogType()
    object Transaction : StatusDialogType()
    data class ProgressFull(val message: String = "Loading") : StatusDialogType()
    data class Success(val message: String, val callback: () -> Unit = {}) : StatusDialogType()
    data class Failure(val message: String, val callback: () -> Unit = {}) : StatusDialogType()
    data class Alert(val message: String, val callback: () -> Unit = {}) : StatusDialogType()
    data class Pending(val message: String, val callback: () -> Unit = {}) : StatusDialogType()
}
