package com.a2z.app.ui.component.permission

import androidx.compose.runtime.Composable
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.screen.util.permission.AppPermissionList
import com.a2z.app.ui.screen.util.permission.PermissionType
import com.a2z.app.ui.theme.LocalNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionComponent(
    permissions: List<String>,
    content: @Composable (() -> Boolean) -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(permissions = permissions)
    content { permissionState.allPermissionsGranted }
}


@Composable
fun CheckCameraStoragePermission(
    content: @Composable (() -> Boolean) -> Unit
) {
    val navController = LocalNavController.current
    PermissionComponent(
        permissions = AppPermissionList.cameraStorages().map { it.permission }) { action1 ->
        content {
            val result = action1.invoke()
            if(!result){
                navController.navigate(
                    NavScreen.PermissionScreen.passData(
                        permissionType = PermissionType.CameraAndStorage
                    )
                )
            }
            result
        }
    }
}