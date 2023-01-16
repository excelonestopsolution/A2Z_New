package com.a2z.app.ui.screen.auth.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.CenterBox
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.EmailTextField
import com.a2z.app.ui.component.common.MobileTextField

@Composable
fun UserRegistrationMobileContent() {

    val viewModel: UserRegistrationViewModel = hiltViewModel()

    val input = viewModel.input

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Mobile Number",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            MobileTextField(
                value = input.mobile.getValue(),
                onChange = { input.mobile.onChange(it) },
                error = input.mobile.formError()
            )
        }
    }
}


@Composable
fun UserRegistrationEmailContent() {

    val viewModel: UserRegistrationViewModel = hiltViewModel()

    val input = viewModel.input

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Email ID",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            EmailTextField(
                value = input.email.getValue(),
                onChange = { input.email.onChange(it) },
                error = input.email.formError(),
                downText = "eg :- abc@xyz.com"
            )
        }
    }
}


@Composable
fun UserRegistrationPanContent() {

    val viewModel: UserRegistrationViewModel = hiltViewModel()

    val input = viewModel.input

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Pan Number",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            AppTextField(
                label = "Pan Number",
                value = input.pan.getValue(),
                keyboardCapitalization = KeyboardCapitalization.Characters,
                onChange = { input.pan.onChange(it) },
                error = input.pan.formError(),
                downText = "10 character pan number"
            )
        }
    }
}