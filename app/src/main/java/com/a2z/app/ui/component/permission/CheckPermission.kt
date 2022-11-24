package com.a2z.app.ui.component.permission

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckPermission(
    vararg permissions: String,
    content: @Composable (() -> Boolean) -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(permissions = permissions.toList())
    content { permissionState.allPermissionsGranted }
}