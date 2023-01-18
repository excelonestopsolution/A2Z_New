package com.a2z.app.ui.dialog

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.a2z.app.ui.component.common.AppNetworkImage
import com.a2z.app.util.BitmapUtil
import com.a2z.app.util.VoidCallback
import com.a2z.app.util.extension.showToast
import java.io.File


@Composable
fun ImageNetworkDialog(
    url : String,
    state : MutableState<Boolean>
) {

  if(state.value) Dialog(onDismissRequest = {
       state.value = false
   }) {
       Column(
           modifier = Modifier
               .clip(MaterialTheme.shapes.medium)
               .background(Color.White)
               .padding(16.dp),
           horizontalAlignment = Alignment.CenterHorizontally

       ) {

           val mPainter = rememberAsyncImagePainter(
               ImageRequest.Builder(LocalContext.current).data(data = url)
                   .apply(block = fun ImageRequest.Builder.() {
                       crossfade(true)
                           .transformations(
                           )
                           .build()
                   }).build()
           )
           Image(
               painter = mPainter,
               contentDescription = null,
               modifier = Modifier
                   .padding(8.dp)
                   .clip(MaterialTheme.shapes.medium)
                   .fillMaxWidth()
                   .height(500.dp),
               contentScale = ContentScale.FillBounds,
           )
           Button(
               onClick = {
                   state.value = false
               },
               modifier = Modifier,
               shape = CircleShape
           ) {
               Text(text = "Close")
           }

       }
   }

}


@Composable
fun ImageDialog(
    imageDialogOpen: MutableState<Boolean>,
    selectFile: MutableState<File?>,
    onConfirmClick: VoidCallback? = null,
) {
    if (imageDialogOpen.value) Dialog(onDismissRequest = {
        imageDialogOpen.value = false
    }) {

        val context = LocalContext.current.applicationContext

        val bitmap = BitmapUtil.uriToBitMap(selectFile.value?.toUri(), context) ?: return@Dialog

        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .fillMaxWidth()
                    .height(500.dp),
                contentScale = ContentScale.FillBounds,
            )
            if (onConfirmClick != null) Button(
                onClick = {
                    onConfirmClick.invoke()
                    imageDialogOpen.value = false
                },
                modifier = Modifier,
                shape = CircleShape
            ) {
                Text(text = "Confirm and Upload")
            }

        }
    }
}