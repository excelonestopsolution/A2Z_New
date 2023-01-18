package com.a2z.app.util

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.ColorInt
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


object BitmapUtil {

    fun uriToBitMap(uri: Uri?, context: Context): Bitmap? {
        if (uri == null) return null
        return if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images
                .Media.getBitmap(context.contentResolver, uri)

        } else {
            val source = ImageDecoder
                .createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
    }

    fun Bitmap.getResizedBitmap(maxSize: Int = 800): Bitmap {
        var width = this.width
        var height = this.height
        if (width <= maxSize || height <= maxSize) return this
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        val mBitmap = Bitmap.createScaledBitmap(this, width, height, true)

        val outputStream = ByteArrayOutputStream()
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        return BitmapFactory.decodeStream(ByteArrayInputStream(outputStream.toByteArray()))

    }

    private const val ONE_MB = 1024


    fun Bitmap.fileSize(): Int {
        val outputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val imageSize = outputStream.size() / ONE_MB
        outputStream.reset()
        outputStream.close()
        AppUtil.logger("bitmap size : $imageSize")
        return imageSize
    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun Bitmap?.toFile(context: Context?): File? {
        if (context == null) return null;
        if (this == null) return null
        var file: File? = null
        return try {
            file = FileUtil.createImageFile(context)

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            this.compress(Bitmap.CompressFormat.PNG, 100, bos) // YOU can also save it in JPEG
            val bitmapData = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }


    fun Bitmap.addWatermark(
        vararg watermarkTexts: String,
        options: WatermarkOptions = WatermarkOptions()
    ): Bitmap {

        val result = this.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        paint.textAlign = when (options.corner) {
            Corner.TOP_LEFT,
            Corner.BOTTOM_LEFT -> Paint.Align.LEFT
            Corner.TOP_RIGHT,
            Corner.BOTTOM_RIGHT -> Paint.Align.RIGHT
        }
        val textSize = result.width * options.textSizeToWidthRatio
        paint.textSize = textSize
        paint.color = options.textColor
        if (options.shadowColor != null) {
            paint.setShadowLayer(textSize / 2, 0f, 0f, options.shadowColor)
        }
        if (options.typeface != null) {
            paint.typeface = options.typeface
        }
        val padding = result.width * options.paddingToWidthRatio
        val coordinates =
            calculateCoordinates(
                watermarkTexts[0],
                paint,
                options,
                canvas.width,
                canvas.height,
                padding
            )

        val textHeight = paint.descent() - paint.ascent()

        var mCount = watermarkTexts.count()
        watermarkTexts.forEach {
            canvas.drawText(it, coordinates.x, coordinates.y - (textHeight * mCount), paint)
            mCount -= 1
        }


        return result
    }

    private fun calculateCoordinates(
        watermarkText: String,
        paint: Paint,
        options: WatermarkOptions,
        width: Int,
        height: Int,
        padding: Float
    ): PointF {
        val x = when (options.corner) {
            Corner.TOP_LEFT,
            Corner.BOTTOM_LEFT -> {
                padding
            }
            Corner.TOP_RIGHT,
            Corner.BOTTOM_RIGHT -> {
                width - padding
            }
        }
        val y = when (options.corner) {
            Corner.BOTTOM_LEFT,
            Corner.BOTTOM_RIGHT -> {
                height - padding
            }
            Corner.TOP_LEFT,
            Corner.TOP_RIGHT -> {
                val bounds = Rect()
                paint.getTextBounds(watermarkText, 0, watermarkText.length, bounds)
                val textHeight = bounds.height()
                textHeight + padding

            }
        }
        return PointF(x, y)
    }


    enum class Corner {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
    }

    data class WatermarkOptions(
        val corner: Corner = Corner.BOTTOM_RIGHT,
        val textSizeToWidthRatio: Float = 0.04f,
        val paddingToWidthRatio: Float = 0.03f,
        @ColorInt val textColor: Int = Color.WHITE,
        @ColorInt val shadowColor: Int? = Color.BLACK,
        val typeface: Typeface? = null
    )

}