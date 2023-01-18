package com.a2z.app.ui.screen.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Message
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.ui.screen.home.HomeViewModel
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.theme.RedColor

@Composable
fun BuildAlertComponent(homeViewModel: HomeViewModel) {


    val news = homeViewModel.newsResponseState.value
    val kyc = homeViewModel.checkDMTAndAEPSKycPending()

    val isNews = news != null && news.status == 1


    Column(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.Start
    ) {


        val kycInfo = homeViewModel.kycInfo()

        if (kyc) Card(
            shape = MaterialTheme.shapes.small,
            elevation = 8.dp,
            backgroundColor = RedColor
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
            backgroundColor = Color.White
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