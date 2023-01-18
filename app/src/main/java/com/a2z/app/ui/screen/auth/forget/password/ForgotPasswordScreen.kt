package com.a2z.app.ui.screen.auth.forget.password

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
import com.a2z.app.ui.component.CounterOtpTextField
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.common.MobileTextField
import com.a2z.app.ui.component.common.PasswordTextField
import com.a2z.app.ui.theme.BackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ForgotPasswordScreen() {

    val viewModel: ForgotPasswordViewModel = hiltViewModel()


    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Forgot Password") }) {
        BaseContent(viewModel) {

            AppFormUI(
                showWalletCard = false,
                button = {
                    Button(
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        enabled = viewModel.buttonValidation,
                        onClick = { viewModel.onProceed() }) {
                        Text(text = viewModel.buttonText)
                    }
                },
                cardContents = listOf(
                    AppFormCard(
                        isVisible = viewModel.mobileFormCard,
                        title = "Mobile Number",
                        contents = {
                            val input = viewModel.input
                            MobileTextField(
                                value = input.mobile.getValue(),
                                onChange = { input.mobile.onChange(it) },
                                error = input.mobile.formError()
                            )
                        }
                    ),
                    AppFormCard(
                        isVisible = viewModel.resetFormCard,
                        contents = {
                            val input = viewModel.input


                            PasswordTextField(
                                value = input.password.getValue(),
                                onChange = {input.password.onChange(it)},
                                error = input.password.formError(),
                                label = "New Password"
                            )

                            PasswordTextField(
                                value = input.confirmPassword.getValue(),
                                onChange = {input.confirmPassword.onChange(it)},
                                error = input.confirmPassword.formError(),
                                label = "Confirm Password"
                            )

                            CounterOtpTextField(
                                value = input.otp.getValue(),
                                onChange = { input.otp.onChange(it) },
                                error = input.otp.formError(),
                                onResend = {viewModel.onResendOtp()},
                                timerState = viewModel.timerState
                            )
                        }
                    )
                ))

        }

    }

}