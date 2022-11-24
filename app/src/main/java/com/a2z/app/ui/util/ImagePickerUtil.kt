package com.a2z.app.ui.util

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.a2z.app.util.FileUtil

object ImagePickerUtil {

    @Composable
    fun pickCameraImage(onResult: (Uri?) -> Unit): Pair<Uri?, ManagedActivityResultLauncher<Uri, Boolean>> {
        val context = LocalContext.current.applicationContext
        val file = FileUtil.createImageFile(context = context)
        val imageUri = FileUtil.toUri(context, file)
        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { isSuccess: Boolean ->
                if(isSuccess) onResult.invoke(imageUri)
            })
        return Pair(imageUri, cameraLauncher)
    }


    @Composable
    fun pickGalleryImage(onResult: (Uri?) -> Unit) = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onResult(uri)
    }


}