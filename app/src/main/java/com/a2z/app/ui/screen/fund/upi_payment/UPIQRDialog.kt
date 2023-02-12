package com.a2z.app.ui.screen.fund.upi_payment

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UPIQRCodeDialog(
    state: MutableState<Boolean>,
    upiUri: String
) {

    if (state.value) Dialog(
        onDismissRequest = {
            state.value = false
        }, properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {

       BuildMainContent(upiUri)
    }

}

@Composable
private fun BuildMainContent(upiUri: String) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.White)
            .padding(12.dp)
            .fillMaxSize()


    ) {
        Text(
            text = "Scan QR Code to make payment to",
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )


        Spacer(modifier = Modifier.height(12.dp))


        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .weight(1f, fill = false)
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 0.dp
                )
            ) {

                val bitmap =textToImageEncode(upiUri)!!.asImageBitmap()
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
                        .fillMaxWidth()
                )



            }
        }



        Text(
            text = "Scan and Pay using any UPI app", textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        BuildUpiAppList()
    }
}

@Composable
private fun BuildUpiAppList() {
    val appLists = listOf(
        R.drawable.google_pay,
        R.drawable.paytm,
        R.drawable.amazon_pay,
        R.drawable.phone_pay,
        R.drawable.upi,
    )

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        appLists.forEach {
            Image(
                painter = painterResource(id = it), contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(42.dp)
            )
        }
    }

}

private fun textToImageEncode(Value: String): Bitmap? {
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
                android.graphics.Color.parseColor("#000000")
            else android.graphics.Color.parseColor("#ffffff")
        }
    }
    val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
    bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
    return bitmap
}