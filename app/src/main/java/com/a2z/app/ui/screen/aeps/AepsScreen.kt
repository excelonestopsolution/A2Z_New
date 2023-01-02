package com.a2z.app.ui.screen.aeps

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.dialog.SpinnerSearchDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.util.VoidCallback

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AepsScreen() {
    val viewModel: AepsViewModel = hiltViewModel()
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Aeps Transaction") }
    ) {

        val name = remember {
            mutableStateOf("")
        }

        AppFormUI(
            showWalletCard = false,
            button = {
                Button(
                    onClick = { /*TODO*/ }, modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text(text = "Proceed")
                }
            },
            cardContents = listOf(
                AppFormCard(
                    title = "Fill Details",
                    contents = {

                        DropDownTextField(
                            value = viewModel.selectedBank.value,
                            hint = "Select Bank"
                        ) {
                            viewModel.spinnerDialogState.value = true
                        }

                        AadhaarTextField(
                            value = name.value,
                            onChange = { name.value = it },
                        )
                        MobileTextField(
                            value = name.value,
                            onChange = { name.value = it },
                        )
                    }
                ),
                AppFormCard(
                    title = "Transaction Type",
                    contents = {
                        Row {
                            TransactionTypeButton(AepsTransactionType.CASH_WITHDRAWAL) {
                                viewModel.transactionType.value =
                                    AepsTransactionType.CASH_WITHDRAWAL
                            }
                            TransactionTypeButton(AepsTransactionType.AADHAAR_PAY) {
                                viewModel.transactionType.value = AepsTransactionType.AADHAAR_PAY
                            }
                        }

                        Row {
                            TransactionTypeButton(AepsTransactionType.MINI_STATEMENT) {
                                viewModel.transactionType.value = AepsTransactionType.MINI_STATEMENT
                            }
                            TransactionTypeButton(AepsTransactionType.BALANCE_ENQUIRY) {
                                viewModel.transactionType.value =
                                    AepsTransactionType.BALANCE_ENQUIRY
                            }
                        }

                        if (viewModel.transactionType.value == AepsTransactionType.CASH_WITHDRAWAL
                            || viewModel.transactionType.value == AepsTransactionType.AADHAAR_PAY
                        ) AmountTextField(value = "0.00")
                    }
                )
            ))

    }

    SpinnerSearchDialog(
        title = "Select Aeps Bank",
        state = viewModel.spinnerDialogState,
        list = viewModel.aepsBankList(),
        initialSelectedValue = viewModel.selectedBank.value
    ) { state ->
        viewModel.onBankChange(state)
    }
}

@Composable
fun RowScope.TransactionTypeButton(transactionType: AepsTransactionType, onClick: VoidCallback) {


    val title = when (transactionType) {
        AepsTransactionType.AADHAAR_PAY -> "Aadhaar Pay"
        AepsTransactionType.CASH_WITHDRAWAL -> "Cash Withdrawal"
        AepsTransactionType.MINI_STATEMENT -> "Mini Statement"
        AepsTransactionType.BALANCE_ENQUIRY -> "Balance Enquiry"
    }

    val viewModel: AepsViewModel = hiltViewModel()

    val backgroundColor = if (viewModel.transactionType.value == transactionType)
        MaterialTheme.colors.primary else Color.Gray.copy(alpha = 0.7f)
    val contentColor = if (viewModel.transactionType.value == transactionType)
        Color.White else Color.White

    val style = ButtonDefaults.buttonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 5.dp),
        colors = style,
        shape = CircleShape
    ) {
        Text(text = title, fontSize = 12.sp)
    }
}