package com.a2z.app.ui.screen.auth.forget.login_id

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
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.theme.BackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ForgotLoginIdScreen() {

    val viewModel: ForgotLoginIdViewModel = hiltViewModel()


    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Forgot Login ID") }) {
        BaseContent(viewModel) {

            AppFormUI(
                showWalletCard = false,
                button = {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = viewModel.buttonValidation,
                        onClick = { viewModel.onProceed() }) {
                        Text(text = viewModel.buttonText)
                    }
                },
                cardContents = listOf(
                    AppFormCard(
                        contents = {
                            val input = viewModel.input
                            MobileTextField(
                                value = input.mobile.getValue(),
                                onChange = { input.mobile.onChange(it) },
                                error = input.mobile.formError(),
                                readOnly = viewModel.isOtpVisible
                            )

                            AadhaarTextField(
                                value = input.aadhaar.getValue(),
                                onChange = { input.aadhaar.onChange(it) },
                                error = input.aadhaar.formError(),
                                readOnly = viewModel.isOtpVisible
                            )

                            if (viewModel.isOtpVisible) PinTextField(
                                value = input.otp.getValue(),
                                onChange = { input.otp.onChange(it) },
                                error = input.otp.formError()
                            )
                        }
                    ),
                ))

        }

    }

}