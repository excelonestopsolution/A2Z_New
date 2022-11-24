package com.a2z.app.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.ui.util.getHeightWidth

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TransactionDialog(
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {}
    ) {
        val (width, height) = getHeightWidth()
        Box(
            modifier = Modifier
                .requiredSize(width, height)
                .background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(120.dp),
                    strokeWidth = 10.dp,
                    color = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.padding(12.dp))
                Text(
                    text = "Transaction is being proceed", style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }

            Text(
                text = "Don't press back button or exit from the app",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color.Gray
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
            )

        }
    }
}

