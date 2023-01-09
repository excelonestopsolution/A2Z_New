package com.a2z.app.ui.component

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppQRScanScreen() {
    var scanFlag by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val scanViewState = remember {
        CompoundBarcodeView(context).apply {
            val capture = CaptureManager(context as Activity, this)
            capture.initializeFromIntent(context.intent, null)
            this.setStatusText("")
            capture.decode()
            this.decodeContinuous { result ->
                if(scanFlag){
                    return@decodeContinuous
                }
                scanFlag = true
                result.text?.let { barCodeOrQr->
                    //Do something

                }
                scanFlag = false
            }
        }
    }

    Scaffold(
        topBar = { NavTopBar(title = "QR Scan")}
    ) {
        AndroidView(factory = { scanViewState }, modifier = Modifier.fillMaxSize())
    }

    DisposableEffect(key1 = Unit){
        scanViewState.resume()
        onDispose {
            scanViewState.pause()
        }
    }

}