package com.a2z.app.ui.screen.dmt.transfer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MPinDialog(
    dialogState: MutableState<Boolean>,
) {
    if (dialogState.value)
        Dialog(
            onDismissRequest = { /*TODO*/ },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {

            Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){


            }
        }
}