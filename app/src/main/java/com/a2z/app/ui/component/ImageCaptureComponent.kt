package com.a2z.app.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.applyCanvas
import com.a2z.app.util.BitmapUtil
import com.a2z.app.util.FileUtil
import com.a2z.app.util.VoidCallback
import com.a2z.app.util.extension.showToast
import kotlin.math.roundToInt

@Composable
fun ImageCaptureComponent(
    content: @Composable (download: VoidCallback, share: VoidCallback) -> Unit
) {

    val view = LocalView.current
    var capturingViewBounds by remember { mutableStateOf<Rect?>(null) }
    val context = LocalContext.current

    fun getImage(): Bitmap? {
        val bounds = capturingViewBounds ?: return null
        return Bitmap.createBitmap(
            bounds.width.roundToInt(), bounds.height.roundToInt(),
            Bitmap.Config.ARGB_8888
        ).applyCanvas {
            translate(-bounds.left, -bounds.top)
            view.draw(this)
        }
    }

    fun download() {
        val bitmap = getImage() ?: return
        val result = FileUtil.saveImage(context, bitmap, "qr_code")
        if (result) context.showToast("File saved to gallery")
        else context.showToast("Unable to save file")
    }

    fun share() {
        val bitmap = getImage() ?: return
        val file = BitmapUtil.getImageUri(context, bitmap)
        FileUtil.shareImage(file!!, context, isWhatsAppOnly = false)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned {
            capturingViewBounds = it.boundsInRoot()
        }) {

        content(
            download = { download() },
            share = { share() }
        )


    }

}