package com.a2z.app.ui.util.extension

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@ExperimentalPermissionsApi
fun PermissionState.isPermanentlyDenied(isFirstRequested: Boolean): Boolean {
    return (!shouldShowRationale && !hasPermission) && isFirstRequested
}