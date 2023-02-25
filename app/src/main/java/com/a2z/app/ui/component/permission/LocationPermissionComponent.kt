package com.a2z.app.ui.component.permission

import android.Manifest
import android.app.Activity
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.MainActivity
import com.a2z.app.nav.NavScreen
import com.a2z.app.service.location.LocationService
import com.a2z.app.ui.dialog.StatusDialog
import com.a2z.app.ui.screen.AppViewModel
import com.a2z.app.ui.screen.util.permission.PermissionType
import com.a2z.app.ui.theme.LocalLocationService
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.util.resource.StatusDialogType
import com.a2z.app.util.VoidCallback

@Composable
fun LocationComponent(
    context: Activity?=null,
    onLocation: VoidCallback = {},
    content: @Composable (() -> Boolean) -> Unit,
) {

    val appViewModel: AppViewModel = hiltViewModel()


    val locationService = LocalLocationService.current

    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    val activity =context ?: LocalContext.current as Activity
    val navController = LocalNavController.current


    fun navigateToPermissionScreen() {
        navController.navigate(
            NavScreen.PermissionScreen.passData(
                permissionType = PermissionType.Location
            )
        )
    }

    val dialogState = remember { mutableStateOf<StatusDialogType>(StatusDialogType.None) }

    fun fetchLocation() {

        if (appViewModel.appPreference.locationFetched) {
            onLocation.invoke()
            return
        }

        dialogState.value = StatusDialogType.Progress("Fetching Location")
        locationService.getCurrentLocation()
        locationService.setupListener(object : LocationService.MLocationListener {
            override fun onLocationChange(location: Location) {
                appViewModel.appPreference.apply {
                    locationFetched = true
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()
                }
                dialogState.value = StatusDialogType.None
                onLocation.invoke()
            }
        })
    }




    PermissionComponent(permissions) { checkPermission ->

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult(),
            onResult = {
                if (it.resultCode == Activity.RESULT_OK) {
                    val result = checkPermission.invoke()
                    if (!result) navigateToPermissionScreen()
                    else fetchLocation()
                }
            })

        content {
            if (locationService.isEnable()) {
                val granted = checkPermission.invoke()
                if (!granted) navigateToPermissionScreen()
                else fetchLocation()
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

    StatusDialog(dialogState = dialogState)


}

