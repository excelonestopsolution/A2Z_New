package com.a2z.app.ui.screen.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.ui.component.permission.LocationComponent
import com.a2z.app.ui.screen.AppViewModel
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.LocalLocationService

@Composable
fun HomeLocationServiceDialog() {
    val locationDialogState = remember {
        mutableStateOf(false)
    }

    val locationService = LocalLocationService.current
    val viewModel: AppViewModel = hiltViewModel()
    val appPreference = viewModel.appPreference


    LaunchedEffect(key1 = Unit, block = {
        val result = locationService.isEnable()
        val latitude = appPreference.latitude
        val longitude = appPreference.longitude
        if (latitude.isEmpty() || longitude.isEmpty()) {
            if (!result)
                locationDialogState.value = true
            else locationService.getCurrentLocation()
        }
    })

    LocationComponent() {
        if (locationDialogState.value) Dialog(onDismissRequest = {
            locationDialogState.value = false

        }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Location Service\nRequired!",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                )

                Divider(modifier = Modifier.padding(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.location_one),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "A2Z Suvidhaa app require location access and permission for" +
                            " money transfer, AEPS, Micro ATM etc. Please enable location service while using app only.",
                    textAlign = TextAlign.Start,
                    lineHeight = 22.sp,
                    color = Color.Black.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(16.dp))


                Button(onClick = {
                    it.invoke()
                    locationDialogState.value = false
                }, shape = CircularShape) {
                    Text(text = "Enable Location")
                }

            }
        }
    }
}