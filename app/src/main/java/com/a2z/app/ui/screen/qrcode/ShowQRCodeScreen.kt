package com.a2z.app.ui.screen.qrcode

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.applyCanvas
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.ImageCaptureComponent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.permission.CheckCameraStoragePermission
import com.a2z.app.ui.component.permission.PermissionComponent
import com.a2z.app.ui.screen.util.permission.AppPermissionList
import com.a2z.app.ui.screen.util.permission.PermissionType
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.util.BitmapUtil
import com.a2z.app.util.BitmapUtil.toFile
import com.a2z.app.util.FileUtil
import com.a2z.app.util.VoidCallback
import org.apache.xml.security.utils.I18n.translate
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ShowQRCodeScreen() {


    val viewModel: ShowQRCodeViewModel = hiltViewModel()

    Scaffold(
        backgroundColor = Color.White,
        topBar = { NavTopBar(title = "A2Z Accepted Payment") }
    ) {

        BaseContent(viewModel) {
            ObsComponent(flow = viewModel.qrCodeObs) {
                ImageCaptureComponent() {download,share->
                    BuildContentForScreenShot(
                        response = it,
                        onDownload = {download.invoke()},
                        onShare = {share.invoke()},

                    )
                }
            }
        }

    }
}


@Composable
private fun BuildContentForScreenShot(
    response: QRCodeResponse,
    onDownload: VoidCallback,
    onShare: VoidCallback,

    ) {
    val viewModel: ShowQRCodeViewModel = hiltViewModel()
    val navController = LocalNavController.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
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
        Text(text = response.retailerQRUpiData)
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


                val bitmap = viewModel.textToImageEncode(response.strUrl)!!.asImageBitmap()
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
                        .fillMaxWidth()
                )


                Divider(modifier = Modifier.padding(top = 8.dp))

                Row {
                   CheckCameraStoragePermission {

                       TextButton(onClick = {
                           val result = it.invoke()
                           if(result) onDownload.invoke()
                       }) {
                           Text(
                               text = "Download",
                               style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                           )
                       }
                   }
                    Spacer(modifier = Modifier.weight(1f))
                    CheckCameraStoragePermission {
                        TextButton(onClick = {
                            val result = it.invoke()
                            if(result) onShare.invoke()
                        }) {
                            Text(
                                text = "Share",
                                style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }


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