package com.a2z.app.ui.screen.settlement.transfer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.TitleValueHorizontally
import com.a2z.app.ui.component.common.AmountTextField
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.dialog.BaseConfirmDialog
import com.a2z.app.ui.theme.BackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettlementTransferScreen() {

    val viewModel: SettlementTransferViewModel = hiltViewModel()
    val bank = viewModel.bank

    val termAndCondition = """
               1. Please check account number before settlement for imps option. Any transaction to wrong account using imps will not be returned back.
            """.trimIndent()

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Settlement Transfer") }
    ) {
        BaseContent(viewModel) {

            AppFormUI(
                showWalletCard = false,
                button = {
                    Button(
                        onClick = {
                            viewModel.confirmDialogState.value = true
                        },
                        enabled = viewModel.input.isValidObs.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                60.dp
                            )
                    ) {
                        Text(text = "Transfer")
                    }
                }, cardContents = listOf(
                    AppFormCard(
                        title = "Bank Details",
                        contents = {
                            TitleValueHorizontally(title = "Beneficiary Name", value = bank.name)
                            TitleValueHorizontally(
                                title = "Account No.",
                                value = bank.accountNumber
                            )
                            TitleValueHorizontally(title = "Bank Name", value = bank.bankName)
                            TitleValueHorizontally(title = "IFSC Code", value = bank.ifscCode)
                            TitleValueHorizontally(title = "Available Bal.", value = bank.balance)
                        }
                    ),
                    AppFormCard(
                        title = "Terms and Conditions",
                        contents = {
                            Text(text = termAndCondition, color = Color.Gray, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            AmountTextField(
                                value = viewModel.input.amount.getValue(),
                                onChange = { viewModel.input.amount.onChange(it) },
                                error = viewModel.input.amount.formError(),
                                isOutline = true
                            )
                        }
                    )
                ))


            BaseConfirmDialog(
                state = viewModel.confirmDialogState,
                amount = viewModel.input.amount.getValue(), titleValues = listOf(
                    "Account Number" to bank.accountNumber.toString(),
                    "Beneficiary Name" to bank.name.toString(),
                    "Bank Name" to bank.bankName.toString(),
                    "IFSC Code" to bank.ifscCode.toString(),
                )
            ) {
                viewModel.transfer()

            }
        }
    }

}