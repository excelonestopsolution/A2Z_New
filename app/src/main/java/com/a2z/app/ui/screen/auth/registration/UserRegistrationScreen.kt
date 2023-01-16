package com.a2z.app.ui.screen.auth.registration

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.StepperComponent
import com.a2z.app.ui.dialog.OTPVerifyDialog
import com.a2z.app.ui.theme.BackgroundColor


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserRegistrationScreen() {


    val viewModel: UserRegistrationViewModel = hiltViewModel()
    Scaffold(backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "User Registration") }) {

        @Composable
        fun mainContent() = when (viewModel.selectedStepperIndex.value) {
            1 -> UserRegistrationMobileContent()
            2 -> UserRegistrationEmailContent()
            3 -> UserRegistrationPanContent()
            4 -> UserRegistrationFinalContent()
            else -> {}
        }

        BaseContent(viewModel) {

            Column(Modifier.fillMaxSize()) {
                StepperComponent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .weight(1f),
                    totalCount = viewModel.totalStepperCount,
                    selectedIndex = viewModel.selectedStepperIndex.value
                ) {


                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        mainContent()
                    }
                }

                Button(
                    enabled = if (viewModel.selectedStepperIndex.value == 4)
                        viewModel.finalInput.isValidObs.value
                    else viewModel.input.isValidObs.value,
                    onClick = {
                        viewModel.onProceed()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(text = "Proceed")
                }

            }

        }

        UserRegistrationConfirmDialog(state = viewModel.confirmDialogState) {
            viewModel.onConfirm()
        }

        OTPVerifyDialog(state = viewModel.otpDialogState,
            description = viewModel.otpDialogMessage.value,
            onAction = {
                viewModel.onOtpSubmit(it)
            })
    }


}
