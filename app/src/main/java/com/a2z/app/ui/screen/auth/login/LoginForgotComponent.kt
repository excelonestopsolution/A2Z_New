package com.a2z.app.ui.screen.auth.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.a2z.app.util.VoidCallback

@Composable
fun LoginForgotComponent(
    onPassword : VoidCallback,
    onLoginId : VoidCallback
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Forgot ?",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
        Divider(modifier = Modifier.padding(vertical = 16.dp))

        TextButton(onClick = { onPassword.invoke() },) {
            Text(text = "Password")
        }

        Text(text = "OR")

        TextButton(onClick = { onLoginId.invoke() }) {
            Text(text = "Login ID")
        }




    }
}