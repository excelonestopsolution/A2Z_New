package com.a2z.app.ui.component

import android.net.Uri
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.a2z.app.ui.util.ImagePickerUtil
import com.a2z.app.util.VoidCallback

@Composable
fun PickCameraImage(
    content: @Composable (capture: () -> Unit) -> Unit,
    onResult: (Uri?) -> Unit
) {
    val (fileUri, cameraLauncher) = ImagePickerUtil.pickCameraImage { uri ->
        onResult(uri)
    }
    content { cameraLauncher.launch(fileUri) }
}

@Composable
fun PickGalleryImage(
    content: @Composable (capture: () -> Unit) -> Unit,
    onResult: (Uri?) -> Unit
) {

    val galleryLauncher = ImagePickerUtil.pickGalleryImage {
        onResult.invoke(it)
    }

    content { galleryLauncher.launch("image/*") }
}

@Composable
fun PickCameraAndGalleryImage(
    content: @Composable (capture: () -> Unit) -> Unit,
    onResult: (Uri?) -> Unit
) {

    val (fileUri, cameraLauncher) = ImagePickerUtil.pickCameraImage { uri ->
        onResult(uri)
    }

    val galleryLauncher = ImagePickerUtil.pickGalleryImage {
        onResult.invoke(it)
    }


    val isDialogOpen = remember { mutableStateOf(false) }

    content(capture = {
        isDialogOpen.value = true
    })
    ImagePickerDialog(
        isDialogOpen = isDialogOpen,
        galleryPicker = {
            galleryLauncher.launch("image/*")
        },
        cameraPicker = {
            cameraLauncher.launch(fileUri)
        }
    )


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