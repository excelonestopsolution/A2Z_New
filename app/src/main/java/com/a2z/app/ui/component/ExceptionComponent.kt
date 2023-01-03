package com.a2z.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.a2z.app.R
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.util.ExceptionState
import com.a2z.app.util.Exceptions
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun DialogExceptionComponent(exceptionState: MutableState<ExceptionState?>) {

    val navController = LocalNavController.current
    val exception = exceptionState.value?.exception
    val popUp = exceptionState.value?.popUp

    if(exceptionState.value == null) return

    fun onDismiss() {
        exceptionState.value = null
        when (exception) {
            is Exceptions.SessionExpiredException -> {
                val currentRoute = navController.currentDestination?.route ?: ""
                navController.popBackStack(currentRoute,true)
                navController.navigate(NavScreen.LoginScreen.route)
            }
            else -> {
                if(popUp == true) navController.navigateUp()
            }
        }
    }

    if (exceptionState.value != null) Dialog(
        onDismissRequest = {
            onDismiss()
        },
        content = {
          if(exception!=null)  ExceptionComponentContent(exception) { onDismiss() }
        }
    )

}


@Composable
private fun ExceptionComponentContent(
    exception: Exception,
    onDismiss: () -> Unit
) {

    val lottieIcon = when (exception) {
        is Exceptions.NoInternetException -> R.raw.lottie_no_internet
        is Exceptions.InternalServerError -> R.raw.lottie_server
        is Exceptions.AppInProgressException -> R.raw.lottie_warning
        else -> R.raw.lottie_alert
    }


    val message =
        if (exception is Exceptions.InternalServerError) "Something went wrong! please try again"
        else exception.message.toString()

    val speed by remember { mutableStateOf(1f) }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottieIcon),
    )

    val isPlaying by remember { mutableStateOf(true) }

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false,
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(color = Color.White),
        content = {

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieAnimation(
                    composition,
                    progress,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.DarkGray,
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.width(150.dp),
                    shape = CircularShape
                ) {
                    Text("Close")
                }

            }
        }
    )
}