package com.a2z.app.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.ui.component.common.AppCheckBox
import com.a2z.app.ui.component.common.AmountTextField
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.util.VoidCallback
import com.a2z.app.util.extension.nullOrEmptyToDouble


private data class ConfirmFormInput(
    val amount: String,
    val amountInput: InputWrapper = InputWrapper {
        if (it.nullOrEmptyToDouble() == amount.nullOrEmptyToDouble()) Pair(true, "")
        else Pair(false, "Amount didn't matched!")

    }
) : BaseInput(amountInput)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BaseConfirmDialog(
    state: MutableState<Boolean>,
    amount: String? = null,
    title: String = "Confirm",
    titleValues: List<Pair<String, String>>,
    warningMessage: String = "",
    successMessage: String = "",
    confirmAmount: Boolean = true,
    onConfirm: VoidCallback
) {
    if (state.value) Dialog(
        onDismissRequest = {
            state.value = false
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {

        val input = remember { ConfirmFormInput(amount ?: "0") }
        val amountInput = input.amountInput

        val isChecked = rememberStateOf(value = true)

        var message = ""
        if(successMessage.trim().isNotEmpty())
            message = successMessage
        else if(warningMessage.trim().isNotEmpty())
            message = warningMessage

        LaunchedEffect(key1 = Unit, block = {
            if(message.isNotEmpty()) isChecked.value = false
        })

        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(5.dp))
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    title, style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                if (amount != null) Text(
                    text = "₹ $amount/-",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = GreenColor,
                        fontSize = 22.sp
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(vertical = 12.dp)
                )
                if (amount == null) Spacer(modifier = Modifier.height(12.dp))

                titleValues.forEach {

                    if (it.second.isNotEmpty())
                        BuildTitleValue(title = it.first, value = it.second)

                }


                if (message.isNotEmpty()) Spacer(
                    modifier = Modifier.height(
                        8.dp
                    )
                )

                val color = if (successMessage.isNotEmpty()) GreenColor else RedColor

                if (message.isNotEmpty()) Row{

                    Checkbox(checked = isChecked.value, onCheckedChange = {
                        isChecked.value = it
                    })
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = message,
                        textAlign = TextAlign.Start,
                        lineHeight = 22.sp,
                        color = color.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (confirmAmount && amount != null) Spacer(modifier = Modifier.height(16.dp))


                if (confirmAmount && amount != null) AmountTextField(
                    value = amountInput.formValue(),
                    onChange = { amountInput.onChange(it) },
                    isOutline = true,
                    error = amountInput.formError()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    enabled = if (!confirmAmount || amount == null) true
                    else input.isValidObs.value && isChecked.value,
                    onClick = {
                        state.value = false
                        onConfirm()
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {

                    Text(text = "Confirm & Proceed")
                }

            }
        }

    }
}

@Composable
private fun BuildTitleValue(title: String, value: String) {

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row {
            Text(
                text = title, style = MaterialTheme.typography.subtitle2.copy(
                    color = Color.Black.copy(alpha = 0.5f),
                ),
                modifier = Modifier.weight(1f)
            )
            Text(text = "    :    ", fontWeight = FontWeight.W500)
            Text(
                text = value, style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                modifier = Modifier.weight(2.5f)
            )
        }
        Divider()
    }

}