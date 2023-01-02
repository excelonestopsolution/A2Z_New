package com.a2z.app.ui.component.permission

import android.Manifest
import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.screen.util.permission.PermissionType
import com.a2z.app.ui.theme.LocalLocationService
import com.a2z.app.ui.theme.LocalNavController
import com.google.gson.Gson

@Composable
fun LocationPermissionComponent(
    content: @Composable (() -> Boolean) -> Unit
) {

    val locationService = LocalLocationService.current

    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    val activity = LocalContext.current as Activity
    val navController = LocalNavController.current


    fun navigateToPermissionScreen() {
        navController.navigate(NavScreen.PermissionScreen.passData(
            permissionType = PermissionType.Location
        ))
    }

    PermissionComponent(permissions = permissions) { checkPermission ->

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult(),
            onResult = {
                if (it.resultCode == Activity.RESULT_OK) {
                    val result = checkPermission.invoke()
                    if (!result) navigateToPermissionScreen()
                }
            })

        content {
            if (locationService.enable()) {

                val granted = checkPermission.invoke()
                if (!granted) navigateToPermissionScreen()
                granted
            } else {
                locationService.enableSetting(activity) {
                    val intentSenderRequest = IntentSenderRequest.Builder(it.resolution).build()
                    launcher.launch(intentSenderRequest)
                }
                false
            }

        }
    }
}