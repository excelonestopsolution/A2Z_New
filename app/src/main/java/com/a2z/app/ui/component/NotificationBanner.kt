package com.a2z.app.ui.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.R
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.theme.ShapeZeroRounded
import com.a2z.app.ui.util.resource.BannerType
import kotlinx.coroutines.delay

@Composable
fun AppNotificationBanner(
    state: MutableState<BannerType>,
) {

    Log.d("AppNotification", "isVisible : $state ")
    Box(modifier = Modifier.fillMaxWidth()) {

        LaunchedEffect(key1 = state.value, block = {
            delay(3000)
            state.value = BannerType.None
        })


        var color: Color = PrimaryColorDark
        var icon: Int = R.drawable.baseline_info_24
        var title = "App Notification"
        var message: String = ""
        when (state.value) {
            is BannerType.Success -> {
                color = GreenColor
                icon = R.drawable.icon_sucess
                title = (state.value as BannerType.Success).title
                message = (state.value as BannerType.Success).message
            }
            is BannerType.Failure -> {
                color = RedColor
                icon = R.drawable.icon_failed
                title = (state.value as BannerType.Failure).title
                message = (state.value as BannerType.Failure).message
            }
            is BannerType.Alert -> {
                color = PrimaryColorDark
                icon = R.drawable.baseline_info_24
                title = (state.value as BannerType.Alert).title
                message = (state.value as BannerType.Alert).message
            }
            else -> {}
        }


        AnimatedVisibility(
            visible = state.value != BannerType.None,
            enter = slideInVertically(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(),
                shape = ShapeZeroRounded,
                elevation = 12.dp,
                backgroundColor = color
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = title,
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                            color = Color.White
                        )
                        Text(
                            text = message,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal),
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(id = icon), contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.8f)),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}