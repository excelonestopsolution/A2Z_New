package com.a2z.app.ui.screen.dmt.transfer

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.bottomsheet.BottomSheetComponent
import com.a2z.app.ui.component.common.AmountTextField
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.permission.LocationComponent
import com.a2z.app.ui.dialog.BaseConfirmDialog
import com.a2z.app.ui.screen.dmt.util.DMTType
import com.a2z.app.ui.screen.dmt.util.DMTUtil
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.util.AppConstant
import com.a2z.app.util.VoidCallback

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DMTMoneyTransferScreen() {

    val manager = LocalFocusManager.current
    val keyboard = keyboardAsState()

    val viewModel: DMTMoneyTransferViewModel = hiltViewModel()


    val dmtTitleValue = listOf(
        "Name" to viewModel.beneficiary.name.orEmpty(),
        "A/C Number" to viewModel.beneficiary.accountNumber.orEmpty(),
        "Bank Name" to viewModel.beneficiary.bankName.orEmpty(),
        "Ifsc Code" to viewModel.beneficiary.ifsc.orEmpty(),
        "DMT Type" to DMTUtil.dmtTypeToTitle(viewModel.dmtType),
    )


    val upiTitleValue = listOf(
        "Name" to viewModel.beneficiary.name.orEmpty(),
        "Upi Id" to viewModel.beneficiary.accountNumber.orEmpty(),
        "Provider" to viewModel.beneficiary.bankName.orEmpty(),
        "DMT Type" to DMTUtil.dmtTypeToTitle(viewModel.dmtType),
    )


    BaseConfirmDialog(
        title = "Transaction Confirmation ?",
        state = viewModel.confirmDialogState,
        amount = viewModel.input.amount.getValue(),
        titleValues = if (viewModel.dmtType == DMTType.UPI) upiTitleValue else dmtTitleValue
    ) {
        viewModel.mpinDialogVisibleState.value = true
    }

    MpinInputComponent(
        visibleState = viewModel.mpinDialogVisibleState,
        amount = if (viewModel.mpinType.value == MoneyTransferMPinType.TRANSFER)
            viewModel.input.amount.getValue() else null,
        onSubmit = { mpin ->
            viewModel.mpin = mpin
            when (viewModel.mpinType.value) {
                MoneyTransferMPinType.COMMISSION -> {
                    viewModel.fetchCharge()
                }
                MoneyTransferMPinType.TRANSFER -> {
                    manager.clearFocus()
                    viewModel.proceedTransaction()

                }
            }
        })

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Money Transfer") }
    ) {

        BaseContent(viewModel) {
            AppFormUI(
                button = {

                    LocationComponent (
                        onLocation = {
                            viewModel.mpinType.value = MoneyTransferMPinType.TRANSFER
                            manager.clearFocus()
                            viewModel.confirmDialogState.value = true
                        }
                            ){
                        Button(
                            onClick = {
                              it.invoke()
                            },
                            enabled = viewModel.input.isValidObs.value,
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth()
                        ) {
                            Text(text = "Proceed Transaction")
                        }
                    }

                }, cardContents = listOf(
                    AppFormCard { BuildTopSection(viewModel) },
                    AppFormCard(
                        isVisible = viewModel.dmtType != DMTType.UPI,
                        title = "Transaction Type"
                    ) {
                        BuildTransactionType(viewModel)
                    },
                    AppFormCard(
                        title = "Transaction Amount",
                        contents = {
                            AmountTextField(
                                value = viewModel.input.amount.getValue(),
                                onChange = {
                                    if (viewModel.chargeState.value != null)
                                        viewModel.chargeState.value = null
                                    viewModel.input.amount.setValue(it)
                                },
                                isOutline = true,
                                error = viewModel.input.amount.formError()
                            )
                        }
                    ),
                    AppFormCard(

                        contents = {
                            val text = if (viewModel.chargeState.value == null) "Show" else "Hide"
                            val icon =
                                if (viewModel.chargeState.value == null) Icons.Default.Visibility else
                                    Icons.Default.VisibilityOff

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Commission/Charge",
                                    style = MaterialTheme.typography.h6.copy(
                                        fontSize = 16.sp
                                    )
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                TextButton(onClick = {
                                    val result = viewModel.input.validate()

                                    if (viewModel.chargeState.value != null) {
                                        viewModel.chargeState.value = null
                                    } else {

                                        if (!result)
                                            viewModel.failureBanner(
                                                title = "Amount Validation Failed",
                                                message = "Enter valid amount"
                                            )
                                        else {
                                            if (viewModel.chargeState.value == null) {
                                                if (!keyboard.value) manager.clearFocus()
                                                viewModel.mpinType.value =
                                                    MoneyTransferMPinType.COMMISSION
                                                manager.clearFocus()
                                                viewModel.mpinDialogVisibleState.value = true
                                            } else {
                                                viewModel.chargeState.value = null
                                            }
                                        }
                                    }


                                }) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(text = text)
                                }
                            }

                            if (viewModel.chargeState.value != null) Column {
                                Spacer(modifier = Modifier.height(8.dp))

                                Row {
                                    if (viewModel.dmtType != DMTType.WALLET_1) Text(
                                        text = "S.No", modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Start,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Txn Amount",
                                        modifier = Modifier.weight(2f),
                                        textAlign = TextAlign.Start,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Total Amount",
                                        modifier = Modifier.weight(2f),
                                        textAlign = TextAlign.End,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.height(5.dp))
                                viewModel.chargeState.value!!.commissions?.forEachIndexed { index, item ->
                                    Row {

                                        if (viewModel.dmtType != DMTType.WALLET_1) Text(
                                            text = "${index + 1}",
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Start,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = MaterialTheme.colors.primary
                                        )
                                        Text(
                                            text = AppConstant.RUPEE_SYMBOL + item.txnAmount.toString(),
                                            modifier = Modifier.weight(2f),
                                            textAlign = TextAlign.Start,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = MaterialTheme.colors.primary
                                        )
                                        Text(
                                            text = AppConstant.RUPEE_SYMBOL + item.total.toString(),
                                            modifier = Modifier.weight(2f),
                                            textAlign = TextAlign.End,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = MaterialTheme.colors.primary
                                        )

                                    }
                                    Divider()
                                }
                            }

                        }
                    )
                ))

        }

    }

}

@Composable
fun ColumnScope.BuildTransactionType(viewModel: DMTMoneyTransferViewModel) {
    Column {
        Row {
            BuildRadioButton(MoneyTransactionType.IMPS) {
                viewModel.onTransactionChange(MoneyTransactionType.IMPS)
            }
            BuildRadioButton(MoneyTransactionType.NEFT) {
                viewModel.onTransactionChange(MoneyTransactionType.NEFT)
            }
        }

        if (viewModel.bankDownText.value.isNotEmpty()) BuildBlinkText(message = viewModel.bankDownText.value)

    }

}

@Composable
private fun RowScope.BuildRadioButton(
    type: MoneyTransactionType,
    onChange: VoidCallback
) {

    val viewModel: DMTMoneyTransferViewModel = hiltViewModel()

    val title = when (type) {
        MoneyTransactionType.IMPS -> "IMPS"
        MoneyTransactionType.NEFT -> "NEFT"
    }


    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
        RadioButton(selected = viewModel.transactionType.value == type, onClick = {
            onChange.invoke()
        })
        Text(text = title)
    }
}


@Composable
private fun BuildTopSection(viewModel: DMTMoneyTransferViewModel) {

    val beneficiary = viewModel.beneficiary

    Column {

        val primaryColor = MaterialTheme.colors.primary

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = primaryColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                val bankNameValue =
                    if (viewModel.dmtType == DMTType.UPI)
                        beneficiary.accountNumber else beneficiary.bankName
                Text(
                    bankNameValue.orEmpty(),
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = primaryColor,
                        fontWeight = FontWeight.Bold
                    )
                )
                val ifscTitle = if (viewModel.dmtType == DMTType.UPI)
                    "Provider  : " else "IFSC   : "
                val ifscValue = if (viewModel.dmtType == DMTType.UPI)
                    beneficiary.bankName else beneficiary.ifsc

                if (ifscValue.orEmpty().trim().isNotEmpty()) Text(
                    (ifscTitle + ifscValue), style = TextStyle(
                        fontSize = 14.sp,
                        color = primaryColor.copy(alpha = 0.8f),
                        fontWeight = FontWeight.SemiBold
                    )
                )
                if (viewModel.dmtType != DMTType.UPI) Text(
                    ("Name : " + beneficiary.name), style = TextStyle(
                        fontSize = 14.sp,
                        color = primaryColor.copy(alpha = 0.8f),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        if (viewModel.dmtType != DMTType.UPI) Column {
            Spacer(modifier = Modifier.height(8.dp))

            val accountTitle = if (viewModel.dmtType == DMTType.UPI) "UPI ID" else "Account Number"
            val bankTitle = if (viewModel.dmtType == DMTType.UPI) "Upi Id" else "Bank"
            TitleValueVertically(title = accountTitle, value = beneficiary.accountNumber)
            if (beneficiary.bankVerified == 1)
                Row {
                    Text(text = "$bankTitle is verified", color = GreenColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = if (beneficiary.bankVerified == 1) GreenColor else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
        }
        else BuildBlinkText(if (viewModel.dmtType == DMTType.UPI) "Selected upi id is not verified!" else "Selected bank is not verified!")
    }

}

@Composable
private fun BuildBlinkText(message: String) {

    val transition = rememberInfiniteTransition()
    val colorState by transition.animateColor(
        initialValue = PrimaryColorDark,
        targetValue = RedColor,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Restart
        )
    )



    Text(text = message, color = colorState, fontSize = 14.sp)

}