package com.a2z.app.ui.screen.auth.change.pin

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
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.common.PinTextField
import com.a2z.app.ui.theme.BackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChangePinScreen() {

    val viewModel: ChangePinViewModel = hiltViewModel()
    val input = viewModel.input

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Change Pin") }
    ) {
        BaseContent(viewModel) {

            AppFormUI(
                showWalletCard = false,
                button = {
                    Button(
                        enabled = viewModel.input.isValidObs.value,
                        onClick = { viewModel.onProceed() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(viewModel.buttonText)
                    }
                }, cardContents = listOf(

                    AppFormCard(
                        contents = {

                            PinTextField(

                                value = input.txnPin.getValue(),
                                onChange = { input.txnPin.onChange(it) },
                                mpin = true,
                                maxLength = 4,
                                label = "Transaction M-PIN",
                                readOnly = viewModel.isInputReadOnly
                            )


                            PinTextField(
                                value = input.confirmTxnPin.getValue(),
                                onChange = { input.confirmTxnPin.onChange(it) },
                                mpin = true,
                                maxLength = 4,
                                label = "Confirm M-PIN",
                                readOnly = viewModel.isInputReadOnly
                            )

                            if (viewModel.actionType.value ==
                                ChangePinActionType.CHANGE_PIN
                            ) CounterOtpTextField(
                                value = input.otp.getValue(),
                                onChange = { input.otp.onChange(it) },
                                timerState = viewModel.timerState,
                                onResend = { viewModel.onResendOtp() },
                                maxLength = 4
                            )
                        }
                    )

                )
            )

        }

    }
}