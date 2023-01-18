package com.a2z.app.ui.util

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.a2z.app.util.FileUtil
import java.io.File

object ImagePickerUtil {

    @Composable
    fun pickCameraImage(onResult: (File) -> Unit): Pair<Uri?, ManagedActivityResultLauncher<Uri, Boolean>> {
        val context = LocalContext.current.applicationContext
        val file = FileUtil.createImageFile(context = context)
        val imageUri = FileUtil.toUri(context, file)
        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { isSuccess: Boolean ->
                if(isSuccess) onResult.invoke(file)
            })
        return Pair(imageUri, cameraLauncher)
    }


    @Composable
    fun pickGalleryImage(onResult: (File?) -> Unit): ManagedActivityResultLauncher<String, Uri?> {
        val context = LocalContext.current
        return rememberLauncherForActivityResult(contract =
        ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

           val file = FileUtil.getFile(context,uri!!)
            onResult(file)
        }
    }


}