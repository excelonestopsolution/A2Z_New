package com.a2z.app.ui.screen.indonepal.search_sender

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.theme.RedColor

@Composable
fun INKycOnboardingDialog(
    state: MutableState<Boolean>,
    type : INKycType,
    onProceed: () -> Unit
) {

    if (state.value) Dialog(onDismissRequest = { state.value = false }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val text = when (type) {
                INKycType.KYC -> "KYC"
                INKycType.ON_BOARDING -> "Onboarding"
            }


            val subTitle ="To proceed transaction user need to complete $text process."


            Text(
                text = "$text Required!",
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold,
                    color = RedColor
                )
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = subTitle,
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                color = RedColor.copy(alpha = 0.8f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            Button(
                onClick = {
                    state.value = false
                    onProceed.invoke()
                }, modifier = Modifier
                    .height(52.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Proceed")
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

enum class INKycType{
    KYC,ON_BOARDING
}