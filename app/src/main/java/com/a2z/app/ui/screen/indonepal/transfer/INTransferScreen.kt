package com.a2z.app.ui.screen.indonepal.transfer

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.CounterOtpTextField
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.TitleValueHorizontally
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.screen.indonepal.INUtil
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.util.rememberStateOf

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun INTransferScreen() {

    val viewModel: INTransferViewModel = hiltViewModel()

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Transfer") }) {
        BaseContent(viewModel) {

            AppFormUI(
                showWalletCard = false,
                button = {
                    if (viewModel.isChargeFetched.value) Button(
                        enabled = viewModel.input.isValidObs.value,
                        onClick = { viewModel.onTransfer() },
                        modifier = Modifier
                            .height(52.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "Transfer")
                    }
                }, cardContents = listOf(


                    AppFormCard(
                        title = "Receiver Detail",
                        contents = {
                            BuildTopSection(viewModel = viewModel)
                        }
                    ),
                    AppFormCard(
                        title = "Transfer Amount",
                        contents = {

                            val input = viewModel.input
                            viewModel.input

                            AppDropDownMenu(
                                selectedState = viewModel.selectedReasonOfTransfer,
                                label = "Reason of Transfer",
                                list = INUtil.reasonOfTransferList
                            )
                            AmountTextField(
                                value = input.amount.formValue(),
                                onChange = { viewModel.onAmountChange(it) },
                                error = input.amount.formError()
                            )

                            if (viewModel.isChargeFetched.value) Column(
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .background(BackgroundColor, shape = MaterialTheme.shapes.small)
                                    .padding(12.dp)
                            ) {
                                TitleValueHorizontally(
                                    title = "Service Charge",
                                    value = viewModel.serviceCharge?.serviceCharge
                                )
                                TitleValueHorizontally(
                                    title = "Exchange Rate",
                                    value = viewModel.serviceCharge?.exchangeRate
                                )
                                TitleValueHorizontally(
                                    title = "Collected Amount",
                                    value = viewModel.serviceCharge?.collectionAmount
                                )
                                TitleValueHorizontally(
                                    title = "Amount Received At Receiver",
                                    value = viewModel.serviceCharge?.payoutAmount
                                )

                            }

                            if (!viewModel.isChargeFetched.value) Button(
                                enabled = input.isValidObs.value,
                                onClick = {
                                    viewModel.fetchCharge()
                                },
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .height(42.dp)
                                    .fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Fetch Service Charge")
                            }

                            if (viewModel.isChargeFetched.value)
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        PinTextField(
                                            value = input.otp.formValue(),
                                            onChange = { input.otp.onChange(it) },
                                            error = input.otp.formError()
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.sendOtp()
                                        },
                                    ) {
                                        Text(text = "Send OTP")
                                    }
                                }
                        }
                    )

                ))
        }
    }

}

@Composable
private fun BuildTopSection(viewModel: INTransferViewModel = hiltViewModel()) {

    val beneficiary = viewModel.beneficiary
    val isVisible = rememberStateOf(value = false)

    @Composable
    fun BuildItem(title: String, value: String?) {
        if (value != null
        ) Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, modifier = Modifier.weight(1f))
                Text(text = "  :  ")
                Text(
                    text = value,
                    modifier = Modifier.weight(1.5f),
                    fontWeight = FontWeight.SemiBold
                )
            }
            Divider(Modifier.padding(vertical = 4.dp))
        }
    }



    BuildItem(title = "Receiver Name", value = beneficiary.name)
    BuildItem(title = "Receiver Gender", value = beneficiary.gender)
    AnimatedVisibility(visible = isVisible.value) {
        Column {
            BuildItem(title = "Receiver Mobile No.", value = beneficiary.mobile)
            BuildItem(title = "Relationship with Receiver", value = beneficiary.relationship)
            BuildItem(title = "Receiver Address", value = beneficiary.address)
            BuildItem(title = "Payment Type", value = beneficiary.paymentMode)
            BuildItem(title = "Bank Name", value = beneficiary.bank_name)
            BuildItem(title = "Branch Name", value = beneficiary.bank_branch_name)
            BuildItem(title = "Account Number", value = beneficiary.account_number)
        }
    }

    TextButton(onClick = {
        isVisible.value = !isVisible.value
    }) {
        val text =if(isVisible.value) "Show Less" else "Show More"
        Text(text = text)
    }

}