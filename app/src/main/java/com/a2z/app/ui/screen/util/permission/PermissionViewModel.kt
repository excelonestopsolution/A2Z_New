package com.a2z.app.ui.screen.util.permission

import android.Manifest
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.a2z.app.R
import com.a2z.app.data.local.AppPreference
import com.a2z.app.ui.util.extension.safeSerializable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    val appPreference: AppPreference, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val permissionType: PermissionType =
        savedStateHandle.safeSerializable("permissionType")!!

    fun getPermissions() = when (permissionType) {
        PermissionType.Location -> AppPermissionList.location()
        PermissionType.CameraAndStorage -> AppPermissionList.cameraStorages()
        PermissionType.BluetoothLocationReadPhoneState -> AppPermissionList.bluetoothLocationReadPhoneState()
    }

    fun getTitle() = when (permissionType) {
        PermissionType.Location -> "Location Permission"
        PermissionType.CameraAndStorage -> "Camera and Storage Permissions"
        PermissionType.BluetoothLocationReadPhoneState -> "Bluetooth and Location Permissions"
    }

    val allPermissionIsPermanentlyDenied = mutableStateOf(false)

    fun getIconList() = when (permissionType) {

        PermissionType.Location -> listOf(
            R.drawable.location_one,
            R.drawable.location_two
        )
        PermissionType.CameraAndStorage -> listOf(
            R.drawable.camera,
            R.drawable.storage
        )
        PermissionType.BluetoothLocationReadPhoneState -> listOf(
            R.drawable.location_one,
            R.drawable.bluetooth
        )
    }


}

object AppPermissionList {

    fun location() = listOf(
        AppPermission(
            permission = Manifest.permission.ACCESS_COARSE_LOCATION,
            title = "COARSE LOCATION", isAccepted = false
        ), AppPermission(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            title = "FINE LOCATION",
            isAccepted = false
        )
    )

    fun bluetooth() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) listOf(
        AppPermission(
            permission = Manifest.permission.BLUETOOTH_ADMIN,
            title = "BLUETOOTH ADMIN",
            isAccepted = false
        ), AppPermission(
            permission = Manifest.permission.BLUETOOTH_CONNECT,
            title = "BLUETOOTH CONNECT",
            isAccepted = false
        ), AppPermission(
            permission = Manifest.permission.BLUETOOTH_SCAN,
            title = "BLUETOOTH SCAN",
            isAccepted = false
        )
    )
    else listOf(
        AppPermission(
            permission = Manifest.permission.BLUETOOTH_ADMIN,
            title = "BLUETOOTH ADMIN",
            isAccepted = false
        )
    )

    fun readPhoneState() = listOf(
        AppPermission(
            permission = Manifest.permission.READ_PHONE_STATE,
            title = "PHONE STATE",
            isAccepted = false
        )
    )

    fun bluetoothLocationReadPhoneState() = readPhoneState() + bluetooth() + location()

    fun cameraStorages() = listOf(
        AppPermission(
            permission = Manifest.permission.CAMERA, title = "CAMERA", isAccepted = false
        ), AppPermission(
            permission = Manifest.permission.READ_EXTERNAL_STORAGE,
            title = "READ EXTERNAL STORAGE",
            isAccepted = false
        ), AppPermission(
            permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
            title = "WRITE EXTERNAL STORAGE",
            isAccepted = false
        )
    )

}

data class AppPermission(
    val permission: String, val title: String, var isAccepted: Boolean?
)

enum class PermissionType {
    Location,
    CameraAndStorage,
    BluetoothLocationReadPhoneState
}
