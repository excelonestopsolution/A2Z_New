package com.a2z.app.ui.screen.home.retailer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.screen.home.retailer.RetailerHomeViewModel
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.theme.RedColor

@Composable
fun HomeKycComponent(
    homeViewModel : RetailerHomeViewModel = hiltViewModel()
) {


    val isKycPending = homeViewModel.dmtAndAEPSKycPendingState.value

    val kycInfo = homeViewModel.kycInfo()

    Column(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.Start
    ) {

        if (isKycPending) Card(
            shape = MaterialTheme.shapes.small,
            elevation = 8.dp,
            backgroundColor = RedColor,
            modifier = Modifier.clickable {
                homeViewModel.checkKycInfo()
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.Info, contentDescription = null,
                    tint = Color.White.copy(0.9f),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = kycInfo.second,
                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    lineHeight = 22.sp,
                )
            }
        }

        if (isKycPending) Spacer(modifier = Modifier.height(5.dp))

    }
}

@Composable
fun KycWarningDialog(
    state: MutableState<Boolean>,
    kycInfo: Triple<String, String, String>,
    onProceed: (String) -> Unit
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
            Text(
                text = kycInfo.first,
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold,
                    color = RedColor
                )
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = kycInfo.second,
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                color = PrimaryColor.copy(alpha = 0.9f),
                fontSize = 13.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    state.value = false
                    onProceed.invoke(kycInfo.third)
                }, modifier = Modifier
                    .height(52.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Proceed Kyc")
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }

}