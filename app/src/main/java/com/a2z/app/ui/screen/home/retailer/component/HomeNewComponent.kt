package com.a2z.app.ui.screen.home.retailer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.util.rememberStateOf

@Composable
fun HomeNewsComponent(
    newsResponse : NewsResponse?,
    retailerNew : Boolean = true
) {

    val isNews = newsResponse != null && newsResponse.status == 1
    val newsDialogState = rememberStateOf(false)

    val newsStr = if(retailerNew) newsResponse?.retailerNews else newsResponse?.distributorNews


    if (isNews) Card(
        shape = MaterialTheme.shapes.small,
        elevation = 8.dp,
        backgroundColor = Color.White,
        modifier = Modifier.padding(vertical = 2.dp, horizontal = 12.dp).clickable {
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
                text = newsStr.toString(),
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                color = PrimaryColor.copy(alpha = 0.9f),
                fontSize = 13.sp,
                lineHeight = 22.sp
            )
        }
    }

    if (newsDialogState.value)
        Dialog(onDismissRequest = { newsDialogState.value = false }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Message",
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text(text = newsStr.toString())
        }
    }

}
