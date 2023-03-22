package com.a2z.app.ui.screen.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.a2z.app.ui.component.common.MobileTextField
import com.a2z.app.ui.component.keyboardAsState
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.ui.theme.PrimaryColorLight
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.ui.util.resource.FormErrorType

@Composable
fun WhatsAppSendDialog(
    state: MutableState<Boolean>,
    withNumber: (String) -> Unit,
    withContact: () -> Unit,
) {

    if (state.value) Dialog(onDismissRequest = { state.value = false }) {

        val manager = LocalFocusManager.current
        val keyboard = keyboardAsState().value

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "WhatsApp Share",
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColorDark
                )
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Enter customer whatsapp number",
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                color = PrimaryColorLight.copy(alpha = 0.8f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            val mobileNumber = rememberStateOf(value = "")
            val formError = rememberStateOf<FormErrorType<String>>(
                FormErrorType.Initial
            )

            MobileTextField(
                value = mobileNumber.value,
                onChange = {
                    mobileNumber.value = it
                    if (it.length == 10) {
                        formError.value = FormErrorType.Success
                    } else if (it.isEmpty()) {
                        formError.value = FormErrorType.Initial
                    } else {
                        formError.value = FormErrorType.Failure("Enter 10 digits mobile number")
                    }
                },
                isOutline = true,
                error = formError.value,
                downText = null
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {


                TextButton(onClick = {
                    withContact()
                    state.value = false
                }) {
                    Icon(
                        imageVector = Icons.Default.Person, contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Contact")
                }
                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    enabled = formError.value == FormErrorType.Success,
                    onClick = {
                        if (keyboard) manager.clearFocus()
                        state.value = false
                        withNumber(mobileNumber.value)
                    }, modifier = Modifier
                        .height(42.dp)
                        .weight(1f)
                ) {
                    Text(text = "Send")
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}