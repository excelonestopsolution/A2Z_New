package com.a2z.app.ui.screen.aeps

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.TitleValueHorizontally
import com.a2z.app.ui.component.bottomsheet.BottomSheetAepsDevice
import com.a2z.app.ui.component.bottomsheet.BottomSheetComponent
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.component.permission.LocationComponent
import com.a2z.app.ui.dialog.BaseConfirmDialog
import com.a2z.app.ui.dialog.SpinnerSearchDialog
import com.a2z.app.ui.screen.home.component.HomeLocationServiceDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.util.resource.BannerType
import com.a2z.app.util.AepsUtil
import com.a2z.app.util.VoidCallback

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AepsScreen() {
    val viewModel: AepsViewModel = hiltViewModel()
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "AEPS Transaction") }
    ) {
        BaseContent(viewModels = arrayOf(viewModel)) {
            ObsComponent(flow = viewModel.bankListResponseFlow) {
                viewModel.bankList = it.banks
                BuildMainContent()
            }
        }
    }
}

@Composable
fun BuildMainContent() {

    val viewModel: AepsViewModel = hiltViewModel()
    val input = viewModel.input
    val context = LocalContext.current

    val pidLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            val result = AepsUtil.biometricResult(it)

            if (result.first != null) {
                viewModel.onBiometricResult(result.first.toString())
            } else viewModel.alertDialog(result.second.toString())
        }
    )


    BottomSheetComponent(
        sheetContent = { closeAction ->
            BottomSheetAepsDevice {
                closeAction.invoke()
                try {
                    viewModel.biometricDevice = it
                    pidLauncher.launch(AepsUtil.pidIntent(it.packageName))
                } catch (e: Exception) {
                    viewModel.bannerState.value = BannerType.Failure("RD Service", "not found!")
                }
            }
        },
        mainContent = { toggle ->
            AppFormUI(
                showWalletCard = false,
                button = {
                    LocationComponent(
                        onLocation = {
                            toggle.invoke()
                        }
                    ) {
                        Button(
                            onClick = {
                                it.invoke()
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            enabled = input.isValidObs.value && viewModel.selectedBank.value != null
                        ) {
                            Text(text = "Proceed")
                        }
                    }
                },
                cardContents = listOf(

                    AppFormCard(
                        contents = {

                            BuildAepsTypeContent(viewModel)

                            DropDownTextField2(
                                value = viewModel.selectedBank.value?.bankName ?: "",
                                label = "Aeps Bank",
                                hint = "Select Bank"
                            ) {
                                viewModel.spinnerDialogState.value = true
                            }


                            AadhaarTextField(
                                value = input.aadhaarInputWrapper.getValue(),
                                onChange = { input.aadhaarInputWrapper.setValue(it) },
                                error = input.aadhaarInputWrapper.formError(),
                                downText = null
                            )
                            MobileTextField(
                                value = input.mobileInputWrapper.getValue(),
                                onChange = { input.mobileInputWrapper.setValue(it) },
                                error = input.mobileInputWrapper.formError(),
                                downText = null
                            )
                        }
                    ),
                    AppFormCard(
                        title = "Transaction Type",
                        contents = {
                            Row {
                                TransactionTypeButton(AepsTransactionType.CASH_WITHDRAWAL) {
                                    viewModel.setTransactionType(AepsTransactionType.CASH_WITHDRAWAL)
                                }
                                TransactionTypeButton(AepsTransactionType.AADHAAR_PAY) {
                                    viewModel.setTransactionType(AepsTransactionType.AADHAAR_PAY)
                                }
                            }

                            Row {
                                TransactionTypeButton(AepsTransactionType.MINI_STATEMENT) {
                                    viewModel.setTransactionType(AepsTransactionType.MINI_STATEMENT)
                                }
                                TransactionTypeButton(AepsTransactionType.BALANCE_ENQUIRY) {
                                    viewModel.setTransactionType(AepsTransactionType.BALANCE_ENQUIRY)
                                }
                            }

                            if (viewModel.transactionType.value == AepsTransactionType.CASH_WITHDRAWAL
                                || viewModel.transactionType.value == AepsTransactionType.AADHAAR_PAY
                            ) AmountTextField(
                                value = input.amountInputWrapper.formValue(),
                                onChange = { input.amountInputWrapper.onChange(it) },
                                error = input.amountInputWrapper.formError(),
                                isOutline = true
                            )


                        }
                    ),
                    AppFormCard(
                        isVisible = viewModel.aepsLimit != null,
                        contents = {
                            TitleValueHorizontally(
                                title = "Daily Max Txn Count",
                                value = viewModel.aepsLimit?.daily_limit_count,
                                titleWeight = 2f
                            )
                            TitleValueHorizontally(
                                title = "Daily Max Txn Amount",
                                value = viewModel.aepsLimit?.daily_txn_amount,
                                titleWeight = 2f
                            )
                            Divider(color = PrimaryColorDark, thickness = 1.5.dp)
                            TitleValueHorizontally(
                                title = "Monthly Max Txn Count",
                                value = viewModel.aepsLimit?.monthly_txn_count,
                                titleWeight = 2f
                            )
                            TitleValueHorizontally(
                                title = "Monthly Max Txn Amount",
                                value = viewModel.aepsLimit?.monthly_txn_max_amount,
                                titleWeight = 2f
                            )
                            Divider(color = PrimaryColorDark, thickness = 1.5.dp)
                            val isAadhaarPay = viewModel.aepsLimit?.is_aadhaar_pay_serice_available == "1"
                            TitleValueHorizontally(
                                title = "Aadhaar Pay",
                                value = if (isAadhaarPay) "Yes" else "NO",
                                color = if(isAadhaarPay) GreenColor else RedColor,
                                titleWeight = 2f
                            )
                        }
                    )

                ))

            HomeLocationServiceDialog()
        }
    )


    BaseConfirmDialog(
        state = viewModel.showConfirmDialogState,
        amount = if (viewModel.transactionType.value == AepsTransactionType.AADHAAR_PAY ||
            viewModel.transactionType.value == AepsTransactionType.CASH_WITHDRAWAL
        ) input.amountInputWrapper.getValue()
        else null,
        titleValues = listOf(
            "Aadhaar Number" to input.aadhaarInputWrapper.getValue(),
            "Transaction Type" to viewModel.transactionTypeText,
            "Bank Name" to viewModel.selectedBank.value?.bankName.toString()
        )
    ) {
        viewModel.onConfirmTransaction()
    }




    SpinnerSearchDialog(
        title = "Select Aeps Bank",
        state = viewModel.spinnerDialogState,
        list = viewModel.getAepsStringBankList(),
    ) { state ->
        viewModel.onBankChange(state)
    }


}


@Composable
private fun BuildAepsTypeContent(viewModel: AepsViewModel) {


    @Composable
    fun RowScope.AepsTypeButton(aepsType: AepsType, onClick: (AepsType) -> Unit) {

        val title = when (aepsType) {
            AepsType.ICICI -> "ICICI"
            AepsType.FINO -> "FINO"
            AepsType.PAYTM -> "PAYTM"
        }


        val color = if (aepsType == viewModel.aepsType.value)
            GreenColor else Color.Gray

        Button(
            onClick = { onClick.invoke(aepsType) },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 3.dp),
            contentPadding = PaddingValues(horizontal = 2.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = color,
                contentColor = Color.White
            )
        ) {
            Text(title)
        }
    }

    Row(Modifier.fillMaxWidth()) {
        AepsTypeButton(aepsType = AepsType.ICICI, onClick = {
            viewModel.onAepsSelect(it)
        })
        AepsTypeButton(aepsType = AepsType.FINO, onClick = {
            viewModel.onAepsSelect(it)
        })
        AepsTypeButton(aepsType = AepsType.PAYTM, onClick = {
            viewModel.onAepsSelect(it)
        })

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
            .padding(horizontal = 2.dp),
        colors = style,
        shape = CircleShape
    ) {
        Text(text = title, fontSize = 10.sp)
    }
}