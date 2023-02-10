package com.a2z.app.util.file

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import com.a2z.app.util.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object AppFileUtil {

    fun createTempPdfFileDirectory(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        return File.createTempFile(
            "a2z_${timeStamp}",
            ".pdf",
            storageDir
        )
    }

    fun createTempImageFileDirectory(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }


    fun File.toBitmap(): Bitmap {
        val filePath = this.path
        return BitmapFactory.decodeFile(filePath);
    }

    fun File.processForRightAngleImage() : File {
        val mimeType = FileUtils.getMimeType(this@processForRightAngleImage)
        return if (mimeType == "image/gif"
            || mimeType == "image/ief"
            || mimeType == "image/jpeg"
            || mimeType == "image/jpeg"
            || mimeType == "image/png"
        ) {
            val filePath = getRightAngleImage(this@processForRightAngleImage.absolutePath)
            File(filePath)
        } else this@processForRightAngleImage
    }


    private fun rotateImage(degree: Float, imagePath: String): String {
        if (degree <= 0) {
            return imagePath
        }
        try {
            var b = BitmapFactory.decodeFile(imagePath)
            val matrix = Matrix()
            if (b.width > b.height) {
                matrix.setRotate(degree)
                b = Bitmap.createBitmap(
                    b, 0, 0, b.width, b.height,
                    matrix, true
                )
            }
            val fOut = FileOutputStream(imagePath)
            val imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1)
            val imageType = imageName.substring(imageName.lastIndexOf(".") + 1)
            val out = FileOutputStream(imagePath)
            if (imageType.equals("png", ignoreCase = true)) {
                b.compress(Bitmap.CompressFormat.PNG, 100, out)
            } else if (imageType.equals("jpeg", ignoreCase = true) || imageType.equals(
                    "jpg",
                    ignoreCase = true
                )
            ) {
                b.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            fOut.flush()
            fOut.close()
            b.recycle()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return imagePath
    }


    private fun getRightAngleImage(photoPath: String): String {
        try {
            val ei = ExifInterface(photoPath)
            val orientation: Int =
                ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            var degree = 0f
            degree = when (orientation) {
                ExifInterface.ORIENTATION_NORMAL -> 0f
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                ExifInterface.ORIENTATION_UNDEFINED -> 0f
                else -> 90f
            }
            return rotateImage(degree, photoPath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return photoPath
    }


    fun saveBitmapToFile(file: File): File? {
        return try {

            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image
            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 100

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file
            file.createNewFile()
            val outputStream = FileOutputStream(file)
            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            file
        } catch (e: java.lang.Exception) {
            null
        }
    }
}