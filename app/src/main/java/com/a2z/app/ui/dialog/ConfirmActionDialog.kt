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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.a2z.app.data.model.dmt.Beneficiary
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.util.VoidCallback


@Composable
fun ConfirmActionDialog(
    title: String = "Confirm ?",
    description: String = "You are sure! this action can not be undo.",
    buttonText: String = "Confirm",
    state: MutableState<Boolean>,
    onAction: VoidCallback
) {

    if (state.value) Dialog(onDismissRequest = {
        state.value = false
    }) {
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

            Button(onClick = {
                state.value = false
                onAction.invoke()
            }, shape = CircleShape) {
                Text(text = buttonText)
            }
        }
    }

}