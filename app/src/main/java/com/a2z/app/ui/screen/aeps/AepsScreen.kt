package com.a2z.app.ui.screen.aeps

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.bottomsheet.BottomSheetAepsDevice
import com.a2z.app.ui.component.bottomsheet.BottomSheetComponent
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.dialog.SpinnerSearchDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.util.resource.BannerType
import com.a2z.app.util.AepsUtil
import com.a2z.app.util.VoidCallback

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AepsScreen() {
    val viewModel: AepsViewModel = hiltViewModel()
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Aeps Transaction") }
    ) {
        BaseContent(viewModels = arrayOf(viewModel)) {
            ObsComponent(flow = viewModel.aepsBankListResponse) {
                viewModel.aepsBankList = it.banks
                BuildMainContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BuildMainContent() {

    val viewModel: AepsViewModel = hiltViewModel()
    val input = viewModel.aepsInput
    val context = LocalContext.current

    val pidLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (it.resultCode == Activity.RESULT_OK || it.data != null) {

            } else viewModel.bannerState.value = BannerType.Failure(
                "Capture Failed",
                "Please check device is connected!"
            )

        }
    )


    BottomSheetComponent(
        sheetContent = {closeAction->
            BottomSheetAepsDevice {
                closeAction.invoke()
                try {
                    pidLauncher.launch(AepsUtil.pidIntent(it.packageName))

                } catch (e: Exception) {
                    viewModel.bannerState.value = BannerType.Failure("RD Service", "not found!")
                }
            }
        },
        mainContent = {
            AppFormUI(
                showWalletCard = false,
                button = {
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
                },
                cardContents = listOf(
                    AppFormCard(
                        title = "Fill Details",
                        contents = {

                            DropDownTextField(
                                value = viewModel.selectedBank.value?.bankName ?: "",
                                hint = "Select Bank",
                                paddingValues = PaddingValues(horizontal = 0.dp)
                            ) {
                                viewModel.spinnerDialogState.value = true
                            }
                            Text(
                                text = "Select AEPS Bank", style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W500,
                                    color = Color.Black.copy(alpha = 0.6f)
                                )
                            )


                            AadhaarTextField(
                                value = input.aadhaarInputWrapper.getValue(),
                                onChange = { input.aadhaarInputWrapper.setValue(it) },
                                error = input.aadhaarInputWrapper.formError()
                            )
                            MobileTextField(
                                value = input.mobileInputWrapper.getValue(),
                                onChange = { input.mobileInputWrapper.setValue(it) },
                                error = input.mobileInputWrapper.formError()
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
                                value = input.amountInputWrapper.getValue(),
                                onChange = { input.amountInputWrapper.setValue(it) },
                                error = input.amountInputWrapper.formError(),
                                isOutline = true
                            )
                        }
                    )
                ))
        }
    )




    SpinnerSearchDialog(
        title = "Select Aeps Bank",
        state = viewModel.spinnerDialogState,
        list = viewModel.getAepsStringBankList(),
        initialSelectedValue = viewModel.selectedBank.value?.bankName
    ) { state ->
        viewModel.onBankChange(state)
    }


    // Declaring Coroutine scope

    // Creating a Bottom Sheet

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