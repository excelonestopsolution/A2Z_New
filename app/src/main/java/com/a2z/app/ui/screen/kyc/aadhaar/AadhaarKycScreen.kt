package com.a2z.app.ui.screen.kyc.aadhaar

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.common.AadhaarTextField
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.common.MobileTextField
import com.a2z.app.ui.component.permission.LocationComponent
import com.a2z.app.ui.dialog.OTPVerifyDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.GreenColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AadhaarKycScreen() {

    val viewModel: AadhaarKycViewModel = hiltViewModel()
    val input = viewModel.input


    OTPVerifyDialog(
        state = viewModel.showOtpDialogState,
        onAction = {
            viewModel.onOtpSubmit(it)
        })

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Aadhaar Kyc") }) {
        BaseContent(viewModel) {

            if (
                viewModel.appPreference.user?.isAadhaarKyc == 1 &&
                viewModel.parentUserId.isEmpty()
            )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Image(
                            painter = painterResource(id = com.a2z.app.R.drawable.icon_sucess),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(GreenColor),
                            modifier = Modifier.size(90.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Aadhaar Kyc Verified",
                            fontWeight = FontWeight.SemiBold,
                            color = GreenColor,
                            fontSize = 14.sp
                        )
                    }
                }
            else AppFormUI(
                showWalletCard = false,
                button = {
                    LocationComponent(onLocation = {
                        viewModel.onSubmit()
                    }) { action ->
                        Button(
                            enabled = input.isValidObs.value,
                            onClick = { action.invoke() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(text = "Submit")
                        }
                    }
                }, cardContents = listOf(
                    AppFormCard {
                        AadhaarTextField(
                            value = input.aadhaar.getValue(),
                            onChange = { input.aadhaar.onChange(it) },
                            error = input.aadhaar.formError()
                        )
                        MobileTextField(
                            value = input.mobile.getValue(),
                            onChange = { input.mobile.onChange(it) },
                            error = input.mobile.formError(),
                            label = "Mobile Number (Linked with aadhaar)"
                        )
                    }
                ))
        }
    }


}