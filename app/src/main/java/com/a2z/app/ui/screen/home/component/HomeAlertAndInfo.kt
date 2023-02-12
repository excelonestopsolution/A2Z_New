package com.a2z.app.ui.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Message
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
import com.a2z.app.ui.screen.home.HomeViewModel
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.util.VoidCallback

@Composable
fun BuildAlertComponent(homeViewModel: HomeViewModel) {


    val news = homeViewModel.newsResponseState.value
    val kyc = homeViewModel.dmtAndAEPSKycPendingState.value

    val isNews = news != null && news.status == 1

    val newsDialogState = rememberStateOf(false)

    if (newsDialogState.value) Dialog(onDismissRequest = { newsDialogState.value = false }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "NEWS",
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text(text = news?.retailerNews.toString())
        }
    }

    val kycInfo = homeViewModel.kycInfo()


    Column(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.Start
    ) {

        if (kyc) Card(
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
                    text = kycInfo?.second.toString(),
                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    lineHeight = 22.sp,
                )
            }
        }

        if (kyc) Spacer(modifier = Modifier.height(5.dp))
        if (isNews) Card(
            shape = MaterialTheme.shapes.small,
            elevation = 8.dp,
            backgroundColor = Color.White,
            modifier = Modifier.clickable {
                newsDialogState.value = true
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
                    imageVector = Icons.Default.Message, contentDescription = null,
                    tint = PrimaryColor.copy(0.9f),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = news?.retailerNews.toString(),
                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                    color = PrimaryColor.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    lineHeight = 22.sp
                )
            }
        }


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