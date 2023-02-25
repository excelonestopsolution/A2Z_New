package com.a2z.app.ui.screen.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import com.a2z.app.ui.component.common.AmountTextField
import com.a2z.app.ui.component.keyboardAsState
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.ui.theme.PrimaryColorLight
import com.a2z.app.ui.util.rememberStateOf


@Composable
fun DMTCommissionDialog(
    state: MutableState<Boolean>,
    onProceed: (String) -> Unit
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
                text = "Commission Amount",
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColorDark
                )
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Money transfer commission amount, It will be added with transfer amount on receipt.",
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                color = PrimaryColorLight.copy(alpha = 0.8f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            val amount = rememberStateOf(value = "")

            AmountTextField(
                value = amount.value,
                onChange = { amount.value = it },
                isOutline = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {


                TextButton(onClick = {
                    onProceed("0")
                    state.value = false
                }) {
                    Text(text = "  SKIP  ")
                }

                Button(
                    onClick = {
                      if(keyboard)  manager.clearFocus()
                        state.value = false
                        if (amount.value.isEmpty())
                            onProceed.invoke("0")
                        else onProceed.invoke(amount.value)
                    }, modifier = Modifier
                        .height(52.dp)
                        .weight(1f)
                ) {
                    Text(text = "Proceed")
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}
