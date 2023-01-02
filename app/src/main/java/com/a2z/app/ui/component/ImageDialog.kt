package com.a2z.app.ui.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.a2z.app.util.BitmapUtil
import com.a2z.app.util.extension.showToast

@Composable
fun ImageDialog(imageDialogOpen: MutableState<Boolean>, selectFile: MutableState<Uri?>) {
    if (imageDialogOpen.value) Dialog(onDismissRequest = {
        imageDialogOpen.value = false
    }) {

        val context = LocalContext.current.applicationContext

        val bitmap = BitmapUtil.uriToBitMap(selectFile.value, context)

        if(bitmap == null){
            return@Dialog
        }

        Box(contentAlignment = Alignment.Center) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null
            )
        }
    }
}