package com.a2z.app.ui.screen.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.ui.theme.RedColor


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DeviceLockScreen() {

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White), contentAlignment = Alignment.CenterEnd
        ) {

            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    Modifier.size(100.dp)
                )
                Text(
                    "Device is not Secure!", style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "App use A2z Suvidhaa money transfer app need to have screen lock on " +
                            "your device. Please go to phone setting and set any screen lock first.",
                    style = TextStyle(lineHeight = 20.sp, color = Color.DarkGray, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                )

                Spacer(modifier = Modifier.height(16.dp))

                val context = LocalContext.current
                Text(text = "make it secure?", color = RedColor)
                Button(onClick = {
                    val intent = Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD)
                    context.startActivity(intent)
                    (context as Activity).finishAffinity()
                }) {
                    Text(text = "Setup Screen Lock")
                }
            }

        }
    }

}