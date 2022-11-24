package com.a2z.app.ui.screen.permission

import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.a2z.app.R
import com.a2z.app.receiver.LocationBroadcast
import com.a2z.app.service.location.LocationService
import com.a2z.app.ui.theme.GreenColor


@Composable
fun LocationPermissionScreen() {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    DisposableEffect(key1 = Unit, effect = {
        val lifeCycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
                    filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
                    context.registerReceiver(LocationBroadcast.gpsSwitchStateReceiver, filter)
                }
                Lifecycle.Event.ON_PAUSE -> {
                    context.unregisterReceiver(LocationBroadcast.gpsSwitchStateReceiver)
                }
                else -> Unit
            }
        }
        lifeCycle.addObserver(observer)

        onDispose {
            lifeCycle.removeObserver(observer)
        }
    })

    Scaffold { it ->
        it.calculateBottomPadding()
        Column {
            Box(
                modifier = Modifier.weight(1f)
                    .fillMaxSize(), contentAlignment = Alignment.Center) {

                if (LocationService.locationState.value)

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.DoneOutline,
                            contentDescription = null,
                            Modifier.size(120.dp),
                            tint = GreenColor
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "All Set",
                            style = MaterialTheme.typography.h5.copy(color = GreenColor)
                        )
                    }
                else Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = null,
                        Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Location Required !", style = MaterialTheme.typography.h5.copy(
                            color =
                            MaterialTheme.colors.primary, fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Location is require to access application's further features. Please grant it.",
                        lineHeight = 24.sp,
                        style = TextStyle(color = Color.Gray, fontWeight = FontWeight.SemiBold),
                        textAlign = TextAlign.Center
                    )

                }

            }

            Button(
                onClick = {


                },
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = if (LocationService.locationState.value) "Go Back" else "Enable")
            }
        }
    }
}