package com.a2z.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.permission.CheckCameraStoragePermission
import com.a2z.app.ui.component.permission.LocationComponent
import com.a2z.app.ui.component.permission.PermissionComponent
import com.a2z.app.ui.screen.AppViewModel
import com.a2z.app.ui.screen.util.permission.AppPermissionList
import com.a2z.app.ui.screen.util.permission.PermissionType
import com.a2z.app.ui.theme.LocalLocationService
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.util.ImagePickerUtil
import com.a2z.app.util.AppUtil
import com.a2z.app.util.BitmapUtil.addWatermark
import com.a2z.app.util.BitmapUtil.getResizedBitmap
import com.a2z.app.util.BitmapUtil.toFile
import com.a2z.app.util.FileUtil.toBitmap
import com.a2z.app.util.VoidCallback
import java.io.File


@Composable
fun PickCameraAndGalleryImage(
    content: @Composable (capture: () -> Unit) -> Unit,
    onResult: (File?) -> Unit
) {
    val context = LocalContext.current
    val viewModel: AppViewModel = hiltViewModel()
    val appPreference = viewModel.appPreference
    val locationService = LocalLocationService.current

    val (uri, cameraLauncher) = ImagePickerUtil.pickCameraImage { file ->
        val bitmap = file.toBitmap(context)
        val resizeBitmap = bitmap?.getResizedBitmap()

        val (address1, address2, address3) =
            locationService.getAddress(appPreference.latitude, appPreference.longitude)

        val watermarkBitmap = resizeBitmap?.addWatermark(address1, address2, address3)
        val resizeFile = watermarkBitmap.toFile(context)
        onResult.invoke(resizeFile)
    }
    val galleryLauncher = ImagePickerUtil.pickGalleryImage { file ->
        val bitmap = file.toBitmap(context)
        val resizeBitmap = bitmap?.getResizedBitmap()

        val (address1, address2, address3) =
            locationService.getAddress(appPreference.latitude, appPreference.longitude)

        val watermarkBitmap = resizeBitmap?.addWatermark(address1, address2, address3)
        val resizeFile = watermarkBitmap.toFile(context)
        onResult.invoke(resizeFile)
    }
    val isDialogOpen = remember { mutableStateOf(false) }

    ImagePickerDialog(
        isDialogOpen = isDialogOpen,
        galleryPicker = {
            galleryLauncher.launch("image/*")
        },
        cameraPicker = {
            cameraLauncher.launch(uri)
        }
    )

    CheckCameraStoragePermission { action1 ->
        LocationComponent(
            onLocation = { isDialogOpen.value = true },
            content = { action2 ->
                content {
                    if (action1.invoke()) action2.invoke()
                }
            }
        )
    }

}


@Composable
private fun ImagePickerDialog(
    isDialogOpen: MutableState<Boolean>,
    cameraPicker: VoidCallback,
    galleryPicker: VoidCallback
) {

    @Composable
    fun Item(icon: ImageVector, title: String, callback: VoidCallback) {
        Column(modifier = Modifier.padding(vertical = 5.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        callback.invoke()
                    }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    title,
                    style = TextStyle.Default.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Divider()
        }
    }

    if (isDialogOpen.value) Dialog(
        onDismissRequest = {
            isDialogOpen.value = false
        }) {

        Box(
            modifier = Modifier
                .fillMaxWidth()

                .clip(shape = RoundedCornerShape(8.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(), horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Camera or Gallery", style = MaterialTheme.typography.h6.copy(
                        fontSize = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                Item(
                    icon = Icons.Default.Camera,
                    title = "Camera",
                    callback = {
                        isDialogOpen.value = false
                        cameraPicker.invoke()
                    }
                )
                Item(
                    icon = Icons.Default.Image,
                    title = "Gallery",
                    callback = {
                        isDialogOpen.value = false
                        galleryPicker.invoke()
                    }
                )

                OutlinedButton(
                    onClick = { isDialogOpen.value = false },
                    modifier = Modifier.align(alignment = Alignment.End)
                ) { Text("Close") }
            }
        }

    }
}