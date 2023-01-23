package com.a2z.app.ui.screen.fund.upi_payment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.CollectLatestWithScope
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.common.AmountTextField
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.util.extension.showToast

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UpiPaymentScreen() {
    Scaffold(
        topBar = { NavTopBar(title = "Upi Payment") },
        backgroundColor = BackgroundColor
    ) {
        val viewModel: UpiPaymentViewModel = hiltViewModel()
        val input = viewModel.input
        val context = LocalContext.current

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
                if (it.resultCode == -1)
                    viewModel.navigateTo(NavScreen.DashboardScreen.route, true)
            }
        )
        BaseContent(viewModel) {
            AppFormUI(
                showWalletCard = false,
                button = {},
                cardContents = listOf(
                    AppFormCard {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.fund_upi),
                                contentDescription = null,
                                modifier = Modifier.size(50.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))

                            Column(horizontalAlignment = Alignment.Start) {
                                Text(text = "Credit your wallet with")
                                Text(
                                    text = "UPI Payment",
                                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))


                        AmountTextField(
                            value = input.amount.getValue(),
                            isOutline = true,
                            onChange = { input.amount.onChange(it) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            enabled = input.isValidObs.value,
                            onClick = {
                                viewModel.fetchData()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(text = "Proceed")
                        }

                    }
                )
            )
        }


        CollectLatestWithScope(flow = viewModel.onInitiateSuccessResult, callback = {

            val amount = input.amount.getValue()
            val mobile = viewModel.appPreference.user?.mobile
            val shopName = viewModel.appPreference.user?.shopName
            val refId = it
            val upiStr =
                "upi://pay?pa=excelone@icici&pn=Excel Stop&tr=$refId&am=$amount" +
                        "&cu=INR&mc=5411&tn=$mobile$shopName"
            val intent = Intent()
            intent.data = Uri.parse(upiStr)
            val chooser =
                Intent.createChooser(intent, "Pay with...")
            launcher.launch(chooser)

        })

        LaunchedEffect(key1 = Unit, block = {

        })
    }


}