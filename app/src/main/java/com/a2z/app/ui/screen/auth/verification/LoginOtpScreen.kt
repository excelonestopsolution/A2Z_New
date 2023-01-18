package com.a2z.app.ui.screen.auth.verification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.ui.component.AppButton
import com.a2z.app.ui.component.AppCard
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.CounterOtpTextField
import com.a2z.app.ui.component.common.PinTextField
import com.a2z.app.ui.screen.auth.component.AuthBackgroundDraw
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.util.extension.collectLifeCycleFlow
import com.a2z.app.ui.util.resource.BannerType

@Preview()
@Composable
fun LoginOtpScreen(
    mobileNumber: String = "7982607742",
) {
    val viewModel: LoginOtpViewModel = hiltViewModel()
    viewModel.mobileNumber = mobileNumber

    BaseContent(viewModel) {
        Box {
            AuthBackgroundDraw()
            BuildCardForm()
        }
    }
}

@Composable
fun BoxScope.BuildCardForm(viewModel: LoginOtpViewModel = hiltViewModel()) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val navController = LocalNavController.current
    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.collectLifeCycleFlow(viewModel.verifyFlow) {

        }
    }


    val formData = remember { viewModel.formData }
    val otpIW = formData.otpWrapper


    val cardModifier =
        Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 5.dp)




    AppCard(modifier = cardModifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_device_unknown_24),
                contentDescription = null,
                modifier = Modifier.size(70.dp),
                tint = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Verify Device",
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text =
                "OTP has sent to your registered mobile number. Please verify it to proceed further",
                textAlign = TextAlign.Center, style = TextStyle(
                    color = Color.Black.copy(alpha = 0.6f)
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            CounterOtpTextField(
                value = remember { otpIW.input }.value,
                onChange = { otpIW.onChange(it) },
                error = remember { otpIW.error }.value,
                onResend = {viewModel.onResendOtp()},
                timerState = viewModel.timerState

            )
            Spacer(modifier = Modifier.height(48.dp))

            Button(
                enabled = formData.isValidObs.value,
                onClick ={viewModel.verifyOtp()},
                modifier = Modifier.height(48.dp),
                shape = CircularShape
            ) {
                Text(text = "   Verify Device   ")
            }
        }
    }
}


