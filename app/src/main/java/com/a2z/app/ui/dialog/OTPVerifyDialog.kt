package com.a2z.app.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.ui.component.common.PasswordTextField
import com.a2z.app.ui.component.common.PinTextField
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.util.VoidCallback

@Composable
fun OTPVerifyDialog(
    title: String = "Verify with OTP",
    otpLength: Int = 6,
    description: String = "Enter $otpLength digits Otp.",
    buttonText: String = "Submit",
    state: MutableState<Boolean>,
    onAction: (String) -> Unit
) {

    if (state.value) Dialog(
        onDismissRequest = {
            state.value = false
        }, properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        val otp = remember {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier

                .background(color = Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = description, textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 12.dp),
                color = PrimaryColorDark.copy(alpha = 0.7f)
            )

            PinTextField(value = otp.value,
                maxLength = otpLength,
                onChange = {
                    otp.value = it
                })

            Button(
                onClick = {
                    state.value = false
                    onAction.invoke(otp.value)
                }, shape = CircleShape,
                enabled = otp.value.length == otpLength
            ) {
                Text(text = buttonText)
            }
        }
    }

}