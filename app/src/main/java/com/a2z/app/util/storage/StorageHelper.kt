package com.a2z.app.util.storage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


object StorageHelper {

    fun saveImageToCacheDirectory(
        context: Context,
        bitmap: Bitmap?,
        fileName: String
    ): Uri {


        val cachePath = File(context.externalCacheDir, "images/")
        cachePath.mkdirs()

        val file = File(cachePath, fileName)
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName.toString() + ".provider",
            file
        )
    }

    fun shareImage(uri: Uri, context: Context, isWhatsAppOnly: Boolean) {

        val intent = Intent(Intent.ACTION_SEND)
        val whatsAppPackageName = "com.whatsapp"
        if (isWhatsAppOnly) intent.setPackage(whatsAppPackageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "image/jpg"
        context.startActivity(Intent.createChooser(intent, "A2Z Suvidhaa Transaction"))
    }

    fun getBitmapFromView(scrollView: ScrollView): Bitmap? {

        val linearLayout = scrollView.getChildAt(0) as LinearLayout
        val mainContentLayout = linearLayout.getChildAt(0)

        val height = mainContentLayout.height
        val width = mainContentLayout.width
        val view: View = scrollView
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
        mainContentLayout.draw(canvas)
        return bitmap
    }

}


