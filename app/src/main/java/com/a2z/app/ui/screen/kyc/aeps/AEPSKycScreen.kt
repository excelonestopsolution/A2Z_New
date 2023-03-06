package com.a2z.app.ui.screen.kyc.aeps

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.bottomsheet.BottomSheetAepsDevice
import com.a2z.app.ui.component.bottomsheet.BottomSheetComponent
import com.a2z.app.ui.component.common.AadhaarTextField
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.dialog.OTPVerifyDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.util.resource.BannerType
import com.a2z.app.util.AepsUtil
import com.a2z.app.util.VoidCallback

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AEPSKycScreen() {
    val viewModel: AEPSKycViewModel = hiltViewModel()

    val pidLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            val result = AepsUtil.biometricResult(it)
            if (result.first != null) {
                viewModel.onBiometricResult(result.first.toString())
            } else viewModel.alertDialog(result.second.toString())
        }
    )


    OTPVerifyDialog(
        state = viewModel.showOtpDialogState,
        otpLength = 7,
        onAction = {
            viewModel.verifyKycOtp(it)
    })


    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "AEPS Kyc") }) {
        BaseContent(viewModel) {

            BottomSheetComponent(sheetContent = { action ->
                BottomSheetAepsDevice(onSelect = {
                    action.invoke()
                    try {
                        pidLauncher.launch(AepsUtil.pidIntent(it.packageName))
                    } catch (e: Exception) {
                        viewModel.bannerState.value = BannerType.Failure("RD Service", "not found!")
                    }
                })
            }) { action ->
                MainContent(viewModel) {
                    action.invoke()
                }
            }

        }
    }
}

@Composable
private fun MainContent(viewModel: AEPSKycViewModel, callback: VoidCallback) {
    ObsComponent(flow = viewModel.kycDetailResultFlow) {
        AppFormUI(
            showWalletCard = false,
            button = {
                Button(
                    onClick = { callback.invoke() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(text = "Proceed")
                }
            },
            cardContents = listOf(
                AppFormCard(contents = {

                    val input = viewModel.input

                    AppTextField(
                        value = input.name.getValue(),
                        label = "Name",
                        onChange = { input.name.onChange(it) },
                        error = input.name.formError(),
                        readOnly = true
                    )

                    AppTextField(
                        value = input.pan.getValue(),
                        label = "Pan Number",
                        maxLength = 10,
                        keyboardCapitalization = KeyboardCapitalization.Characters,
                        onChange = { input.pan.onChange(it) },
                        error = input.pan.formError(),
                        readOnly = true
                    )

                    AadhaarTextField(
                        value = input.aadhaar.getValue(),
                        onChange = { input.aadhaar.onChange(it) },
                        error = input.aadhaar.formError(),
                        readOnly = true
                    )

                    AppTextField(
                        value = input.merchantId.getValue(),
                        label = "Merchant ID",
                        onChange = { input.merchantId.onChange(it) },
                        error = input.merchantId.formError(),
                        readOnly = true
                    )


                })
            )
        )
    }

}