package com.a2z.app.ui.screen.dmt.sender.register

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.CounterOtpTextField
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.theme.BackgroundColor


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SenderRegisterScreen() {

    val viewModel: SenderRegisterViewModel = hiltViewModel()

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = {
            NavTopBar(title = "Sender Registration")
        }
    ) {
        BaseContent(viewModel) {

            val input = viewModel.input

            AppFormUI(
                button = {
                    Button(
                        enabled = input.isValidObs.value,
                        onClick = {
                            viewModel.onRegisterAndVerifyButton()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(text = viewModel.registerButtonText)
                    }
                },
                cardContents = listOf(
                    AppFormCard(
                        contents = {

                            MobileTextField(
                                value = input.mobileNumber.getValue(),
                                onChange = { input.mobileNumber.onChange(it) },
                                error = input.mobileNumber.formError(),
                                readOnly = true
                            )

                            AppTextField(
                                value = input.firstName.getValue(),
                                label = "First Name",
                                downText = "Beneficiary first name",
                                onChange = { input.firstName.onChange(it) },
                                error = input.firstName.formError()
                            )

                            AppTextField(
                                value = input.lastName.getValue(),
                                label = "Last Name",
                                downText = "Beneficiary last name",
                                onChange = { input.lastName.onChange(it) },
                                error = input.lastName.formError()
                            )


                            AppTextField(
                                value = input.pinCode.getValue(),
                                label = "Pin Code",
                                maxLength = 6,
                                downText = "Area pin code",
                                keyboardType = KeyboardType.Number,
                                onChange = { input.pinCode.onChange(it) },
                                error = input.pinCode.formError(),

                                )

                            AppTextField(
                                value = input.address.getValue(),
                                label = "Address",
                                downText = "Area full address",
                                onChange = { input.address.onChange(it) },
                                error = input.address.formError()
                            )

                            if (viewModel.counterOtpVisibility) CounterOtpTextField(
                                value = input.otp.getValue(),
                                onChange = {input.otp.onChange(it)},
                                error = input.otp.formError(),
                                timerState = viewModel.timerState,
                                onResend = {viewModel.onResendOtp()},
                            )
                        }
                    )
                ),
                showWalletCard = false
            )

        }
    }
}
