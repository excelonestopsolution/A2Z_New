package com.a2z.app.ui.screen.home.component

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.screen.dashboard.DashboardViewModel
import com.a2z.app.ui.theme.RedColor

@Composable
fun HomeSignInOptionWidget(
    viewModel: DashboardViewModel) {

    val activity = LocalContext.current as Activity
    if (viewModel.singInDialogState.value) Dialog(onDismissRequest = {}) {

        AnimatedVisibility(visible = viewModel.singInDialogState.value) {
            Column(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sign In Option",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "Sign In with biometric authentication (fingerprint, face lock, pin lock" +
                            " or pattern lock). Or you can use login with username and password.",

                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                    style = androidx.compose.ui.text.TextStyle(
                        lineHeight = 20.sp,
                        color = Color.DarkGray
                    ),
                )
                Divider()
                TextButton(onClick = {
                    viewModel.singInDialogState.value = false
                    viewModel.setOnLaunchEffect()
                }) {
                    Text(text = "Use Biometric Login", textAlign = TextAlign.Center)
                }
                Divider()
                TextButton(onClick = {
                    viewModel.appPreference.user = null
                    viewModel.singInDialogState.value = false
                    viewModel.navigateTo(NavScreen.LoginScreen.route, true)
                }) {
                    Text(text = "Username and Password", textAlign = TextAlign.Center)
                }
                Divider()
                TextButton(
                    onClick = {
                        activity.finishAffinity();

                    }, colors = ButtonDefaults.textButtonColors(
                        contentColor = RedColor
                    )
                ) {
                    Row(modifier = Modifier.background(Color.Transparent)) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Exit", textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }

}