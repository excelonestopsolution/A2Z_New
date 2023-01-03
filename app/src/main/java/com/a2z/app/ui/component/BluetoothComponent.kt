package com.a2z.app.ui.component

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Space
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.a2z.app.R
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.permission.PermissionComponent
import com.a2z.app.ui.screen.util.permission.AppPermissionList
import com.a2z.app.ui.screen.util.permission.PermissionType
import com.a2z.app.ui.theme.LocalNavController

@SuppressLint("MissingPermission")
@Composable
fun BluetoothServiceComponent(
    onResult: (Boolean) -> Unit,
    content: @Composable (() -> Unit) -> Unit
) {

    fun isPaired(bluetoothAdapter: BluetoothAdapter): Boolean {
        var isPaired = false
        bluetoothAdapter.bondedDevices.forEach { device ->
            if (device.name.startsWith("QPOS")) isPaired = true
        }
        return isPaired
    }


    val navController = LocalNavController.current
    val bluetoothServiceDialog = remember { mutableStateOf(false) }
    val permissions =
        AppPermissionList.bluetoothLocationReadPhoneState().map { it.permission }

    val bluetoothAdapter = rememberBluetoothAdapter()
    val bluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            val result = isPaired(bluetoothAdapter)
            if (result) onResult.invoke(true)
            else bluetoothServiceDialog.value = true
        })

    PermissionComponent(
        permissions = permissions,
        content = { permissionAction ->
            content.invoke {
                val permissionResult = permissionAction.invoke()
                if (!permissionResult)
                    navController.navigate(
                        NavScreen.PermissionScreen.passData(
                            permissionType = PermissionType.BluetoothLocationReadPhoneState
                        )
                    )
                else {
                    if (!bluetoothAdapter.isEnabled) {
                        val intentBtEnabled = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        bluetoothLauncher.launch(intentBtEnabled)
                    } else {
                        val result = isPaired(bluetoothAdapter)
                        if (result) onResult.invoke(true)
                        else bluetoothServiceDialog.value = true
                    }
                }
            }
        }
    )

    BluetoothServiceDialog(bluetoothServiceDialog)

}


@Composable
fun rememberBluetoothAdapter(): BluetoothAdapter {

    val context = LocalContext.current.applicationContext
    val bluetoothAdapter = remember {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }
    return bluetoothAdapter
}

@Composable
fun BluetoothServiceDialog(enable: MutableState<Boolean>) {

    val bluetoothSettingLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {})


    if (enable.value) Dialog(onDismissRequest = {
        enable.value = false
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Connect To Device", style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.Bold
            ))
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(
                    id = R.drawable.bluetooth
                ),
                contentDescription = null, modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = "M-ATM device is not connected or paired with android device." +
                        " Please scan device and connect it manually. Device name should be " +
                        "start with QPOS",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                enable.value = false
                val mIntent = Intent().apply { action = Settings.ACTION_BLUETOOTH_SETTINGS }
                bluetoothSettingLauncher.launch(mIntent)
            }) {
                Text(text = "Go To Bluetooth Setting")

            }
        }
    }
}