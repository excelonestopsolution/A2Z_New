package com.a2z.app.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.R
import com.a2z.app.ui.component.AppButton
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.theme.YellowColor
import com.a2z.app.ui.util.resource.StatusDialogType
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.*

@Composable
fun StatusDialog(
    dialogState: MutableState<StatusDialogType>,
) {

    val state by remember { dialogState }
    if (
        state is StatusDialogType.Progress ||
        state is StatusDialogType.Transaction ||
        state is StatusDialogType.None
    ) {
        when (state) {
            is StatusDialogType.Progress -> {
                val message = (state as StatusDialogType.Progress).message
                ProgressDialog(text = message)
            }
            is StatusDialogType.Transaction -> {
                TransactionDialog()
            }
            else -> Box {}
        }
        return
    }


    val color: Color
    val lottieIcon: Int
    val message: String

    when (state) {
        is StatusDialogType.Success -> {
            color = GreenColor
            lottieIcon = R.raw.lottie_success
            message = (state as StatusDialogType.Success).message
        }
        is StatusDialogType.Failure -> {
            color = RedColor
            lottieIcon = R.raw.lottie_failed
            message = (state as StatusDialogType.Failure).message
        }
        is StatusDialogType.Pending -> {
            color = YellowColor
            lottieIcon = R.raw.lottie_pending
            message = (state as StatusDialogType.Pending).message
        }
        else -> {
            color = PrimaryColorDark
            lottieIcon = R.raw.lottie_alert
            message = (state as StatusDialogType.Alert).message
        }
    }

    val isPlaying by remember { mutableStateOf(true) }
    val speed by remember { mutableStateOf(1f) }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottieIcon),
    )

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = color,
        ),
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false,

        )
    val visible = remember { mutableStateOf(true) }
    if (visible.value) Dialog(
        onDismissRequest = {
            visible.value = false
            when (val mResult = state as StatusDialogType) {
                is StatusDialogType.Success -> {
                    mResult.callback.invoke()
                }
                is StatusDialogType.Failure -> {
                    mResult.callback.invoke()
                }
                is StatusDialogType.Alert -> {
                    mResult.callback.invoke()
                }
                is StatusDialogType.Pending -> {
                    mResult.callback.invoke()
                }
                else -> {}
            }
            dialogState.value = StatusDialogType.None
        }, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = false
        )
    ) {

        val width = LocalConfiguration.current.screenWidthDp * 0.65

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(width = width.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .background(Color.White)
        ) {

            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieAnimation(
                    composition,
                    progress,
                    dynamicProperties = dynamicProperties,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp),

                    )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message, style = TextStyle(
                        color = color, fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                AppButton(text = "Done", shape = CircleShape) {
                    visible.value = false
                    when (val mResult = state as StatusDialogType) {
                        is StatusDialogType.Success -> {
                            mResult.callback.invoke()
                        }
                        is StatusDialogType.Failure -> {
                            mResult.callback.invoke()
                        }
                        is StatusDialogType.Alert -> {
                            mResult.callback.invoke()
                        }
                        is StatusDialogType.Pending -> {
                            mResult.callback.invoke()
                        }
                        else -> {}
                    }
                    dialogState.value = StatusDialogType.None
                }
            }
        }
    }


}