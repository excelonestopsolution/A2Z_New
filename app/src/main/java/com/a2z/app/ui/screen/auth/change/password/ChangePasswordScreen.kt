package com.a2z.app.ui.screen.auth.change.password

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.common.PasswordTextField
import com.a2z.app.ui.theme.BackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChangePasswordScreen() {
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Change Password") }
    ) {

        val viewModel: ChangePasswordViewModel = hiltViewModel()
        val input = viewModel.input

       BaseContent(viewModel) {
           AppFormUI(
               showWalletCard = false,
               button = {
                   Button(
                       onClick = {
                           viewModel.changePassword()
                       }, enabled = input.isValidObs.value,
                       modifier = Modifier
                           .fillMaxWidth()
                           .height(60.dp)
                   ) {
                       Text(text = "Change Password")
                   }
               }, cardContents = listOf(
                   AppFormCard(
                       contents = {
                           PasswordTextField(
                               value = input.currentPassword.getValue(),
                               onChange = { input.currentPassword.onChange(it) },
                               error = input.currentPassword.formError(),
                               label = "Current Password"
                           )
                           PasswordTextField(
                               value = input.newPassword.getValue(),
                               onChange = { input.newPassword.onChange(it) },
                               error = input.newPassword.formError(),
                               label = "New Password"
                           )
                           PasswordTextField(
                               value = input.confirmPassword.getValue(),
                               onChange = { input.confirmPassword.onChange(it) },
                               error = input.confirmPassword.formError(),
                               label = "Confirm Password"
                           )
                       }
                   )
               ))
       }
    }
}