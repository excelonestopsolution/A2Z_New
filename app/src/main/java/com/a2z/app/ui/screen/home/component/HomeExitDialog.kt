package com.a2z.app.ui.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.screen.home.HomeViewModel
import com.a2z.app.ui.theme.RedColor

@Composable
fun HomeExitDialogComponent(
    dialogState: MutableState<Boolean>,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    if (!dialogState.value) return


    val activity = LocalContext.current as FragmentActivity

    val boxModifier = Modifier
        .padding(horizontal = 12.dp)
        .clip(MaterialTheme.shapes.medium)
        .background(Color.White)

    Dialog(onDismissRequest = { dialogState.value = false }) {
        Box(
            modifier = boxModifier,
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Logout or Exit ?", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "You are sure you want to logout. It will clear all login sessions !",
                    style =
                    TextStyle(
                        color = Color.DarkGray,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                Divider()
                TextButton(onClick = {
                    dialogState.value = false
                    homeViewModel.logout()
                }) {
                    Row(modifier = Modifier.background(Color.Transparent)) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Logout", textAlign = TextAlign.Center)
                    }
                }
                Divider()
                TextButton(
                    onClick = { activity.finishAffinity() },
                    colors = ButtonDefaults.textButtonColors(
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