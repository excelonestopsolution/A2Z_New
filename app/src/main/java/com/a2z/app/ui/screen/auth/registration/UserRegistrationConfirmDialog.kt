package com.a2z.app.ui.screen.auth.registration

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.a2z.app.util.VoidCallback

@Composable
fun UserRegistrationConfirmDialog(
    state: MutableState<UserRegistrationConfirmDialogState>,
    onConfirm: VoidCallback
) {


    val title = state.value.title
    val title2 = state.value.title2
    val data = state.value.data

    if (state.value.showDialog) Dialog(onDismissRequest = {
        state.value = UserRegistrationConfirmDialogState()
    }) {

        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(title, fontWeight = FontWeight.SemiBold, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Text(data, style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(16.dp))
            Text(title2, fontWeight = FontWeight.SemiBold, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = {
                    state.value = UserRegistrationConfirmDialogState()
                }, modifier = Modifier.weight(1f)) {
                    Text(text = "Cancel")
                }
                Button(onClick = {
                    state.value = UserRegistrationConfirmDialogState()
                    onConfirm.invoke()
                }, Modifier.weight(1f)) {
                    Text(text = "Confirm")
                }
            }

        }

    }

}

data class UserRegistrationConfirmDialogState(
    var showDialog: Boolean = false,
    val title: String = "",
    val title2: String = "",
    val data: String = "",
)