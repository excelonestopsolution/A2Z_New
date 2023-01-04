package com.a2z.app.ui.screen.show_qrcode

import android.graphics.Bitmap
import android.graphics.Color
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.resultStateFlow
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class ShowQRCodeViewModel @Inject constructor(
    private val appRepository: AppRepository
) : BaseViewModel() {


    private val _qrCodeFlow = resultStateFlow<QRCodeResponse>()
    val qrCodeObs = _qrCodeFlow.asStateFlow()


    init {
        fetchQRCodeData()
    }


    private fun fetchQRCodeData() {
        callApiForShareFlow(flow = _qrCodeFlow) { appRepository.fetchQRCode() }

    }


    fun textToImageEncode(Value: String): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(Value, BarcodeFormat.QR_CODE, 500, 500, null)
        } catch (exception: IllegalArgumentException) {
            return null
        }
        val bitMatrixWidth = bitMatrix.getWidth()
        val bitMatrixHeight = bitMatrix.getHeight()
        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)
        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth
            for (x in 0 until bitMatrixWidth) {
                pixels[offset + x] = if (bitMatrix.get(x, y))
                    Color.parseColor("#000000")
                else Color.parseColor("#ffffff")
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }

    fun downloadQRCode() {


    }

}