package com.a2z.app.ui.screen.fund.upi_payment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.CollectLatestWithScope
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.common.AmountTextField
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.util.AppConstant

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
                                viewModel.fetchData(UpiPaymentViewModel.QRCodeActionType.SELF_PAYMENT)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(text = "Self Payment")
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Button(
                            enabled = input.isValidObs.value,
                            onClick = {
                                viewModel.fetchData(UpiPaymentViewModel.QRCodeActionType.GENERATE_QR_CODE)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(text = "Genereate QR Code")
                        }

                    },
                    AppFormCard {
                        BuildNote(
                            notes = listOf(
                                "* No Charges to load money using UPI.",
                                "* Topup between ${AppConstant.RUPEE_SYMBOL} 2,00,000 in a single transaction.",
                                "Pending / Timeout transaction may take upto 2 working days to reflect in your account."
                            )
                        )
                    }
                )
            )
            CollectLatestWithScope(flow = viewModel.onInitiateSuccessResult, callback = {

                if (!it) return@CollectLatestWithScope

                when (viewModel.actionType) {
                    UpiPaymentViewModel.QRCodeActionType.SELF_PAYMENT -> {

                        val intent = Intent()
                        intent.data = Uri.parse(viewModel.upiUri.value)
                        val chooser =
                            Intent.createChooser(intent, "Pay with...")
                        launcher.launch(chooser)
                    }
                    UpiPaymentViewModel.QRCodeActionType.GENERATE_QR_CODE -> {
                        viewModel.qrcodeDialogState.value = true
                    }
                }

            })


            UPIQRCodeDialog(
                state = viewModel.qrcodeDialogState,
                upiUri = viewModel.upiUri.value
            )
        }


    }


}

@Composable
private fun BuildNote(notes: List<String>) {

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primary, MaterialTheme.shapes.small)
            .padding(16.dp)
    ) {
        Text("Note : ", style = MaterialTheme.typography.h6.copy(color = Color.White))
        Spacer(modifier = Modifier.height(8.dp))
        notes.forEach {
            Text(
                it, style = TextStyle(
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp
                ), modifier = Modifier.padding(vertical = 4.dp)
            )
        }

    }
}
