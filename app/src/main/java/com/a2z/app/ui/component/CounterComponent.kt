package com.a2z.app.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.BuildConfig
import com.a2z.app.R
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.spacing
import com.a2z.app.ui.util.resource.FormErrorType
import com.a2z.app.util.AppUtil
import com.a2z.app.util.FormFieldError
import com.a2z.app.util.VoidCallback
import kotlinx.coroutines.delay


@Composable
fun CounterOtpTextField(
    value: String,
    maxLength: Int = 6,
    onChange: (String) -> Unit,
    isOutline: Boolean = false,
    error: FormFieldError = FormErrorType.Initial,
    topSpace: Dp = MaterialTheme.spacing.small,
    timerSecond : Int = if(BuildConfig.DEBUG) 6 else 60,
    timerState : MutableState<Boolean>,
    onResend : VoidCallback,
    onTimerComplete : VoidCallback? =null
) {

    val passwordVisibility by rememberSaveable { mutableStateOf(true) }

    CounterComponent(
        second = timerSecond,
        startState = timerState,
        onComplete = onTimerComplete,
        content = { counter ->
            Column {
                AppTextField(
                    value = value,
                    hint = "******",
                    label =  "OTP",
                    onChange = onChange,
                    keyboardType = KeyboardType.Number,
                    leadingIcon = Icons.Default.Password,
                    error = error,
                    maxLength = maxLength,
                    isOutline = isOutline,
                    visualTransformation = if (passwordVisibility) PasswordVisualTransformation()
                    else VisualTransformation.None,
                    topSpace = topSpace,
                    trailingIcon = {

                        Button(
                            enabled = counter == 0,
                            onClick = onResend,
                            contentPadding = PaddingValues(
                                horizontal = 12.dp
                            ),
                            shape = CircularShape,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(end = 8.dp)
                        ) {
                            Text(text = "Resend")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null
                            )
                        }

                    },

                )

                if (counter != 0) Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(
                        top = 8.dp
                    )
                ) {
                    Text(
                        text = "Please wait for 1 minute to resend otp",
                        fontSize = 12.sp, color = MaterialTheme.colors.primary.copy(0.7f)
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    val progress = ((counter * 100) / timerSecond) * 0.01

                    AppUtil.logger("progressTest : $progress")

                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = progress.toFloat(),

                            )
                        Text(text = counter.toString())
                    }
                }

            }


        },
    )


}



@Composable
fun CounterComponent(
    second: Int,
    content: @Composable (timer: Int) -> Unit,
    onComplete: VoidCallback? = null,
    startState: MutableState<Boolean>
) {
    val remainingValue = remember { mutableStateOf(second) }


    if (startState.value) {
        startState.value = false
        remainingValue.value = second

    }


    content(remainingValue.value)

    LaunchedEffect(remainingValue.value) {
        if (remainingValue.value > 0) {
            delay(1000)
            remainingValue.value = remainingValue.value - 1
        } else {
            onComplete?.invoke()
        }

    }
}
