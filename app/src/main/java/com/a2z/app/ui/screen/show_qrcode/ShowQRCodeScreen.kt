package com.a2z.app.ui.screen.show_qrcode

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.permission.PermissionComponent
import com.a2z.app.ui.screen.util.permission.AppPermissionList
import com.a2z.app.ui.screen.util.permission.PermissionType
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.util.VoidCallback
import com.a2z.app.util.storage.StorageHelper

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ShowQRCodeScreen() {

    val viewModel: ShowQRCodeViewModel = hiltViewModel()
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "A2Z Accepted Payment") }
    ) {

        BaseContent(viewModel) {
            ObsComponent(flow = viewModel.qrCodeObs) {
                BuildContentForScreenShot(response = it) {
                }
            }
        }

    }
}


@Composable
private fun BuildContentForScreenShot(
    response: QRCodeResponse,
    downloadCallback: VoidCallback? = null,

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
            textAlign = TextAlign.Center
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
                    bottom =  0.dp
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
                    PermissionComponent(
                        permissions = AppPermissionList.cameraStorages()
                            .map { it.permission }) { granted ->
                        TextButton(onClick = {
                            val result = granted.invoke()
                            if (!result) navController.navigate(
                                NavScreen.PermissionScreen.passData(
                                    permissionType = PermissionType.CameraAndStorage
                                )
                            )
                            else {
                                downloadCallback?.invoke()
                            }

                        }) {
                            Text(
                                text = "Download",
                                style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "Share",
                            style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }


            }
        }



        Text(text = "Scan and Pay using any UPI app", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
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