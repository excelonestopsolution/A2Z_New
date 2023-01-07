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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.TitleValueVertically
import com.a2z.app.ui.component.bottomsheet.BottomSheetComponent
import com.a2z.app.ui.component.common.AmountTextField
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.keyboardAsState
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.util.VoidCallback

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DMTMoneyTransferScreen() {

    val manager = LocalFocusManager.current
    val keyboard = keyboardAsState()

    val viewModel: DMTMoneyTransferViewModel = hiltViewModel()

        BottomSheetComponent(
            sheetContent = {actionClose ->
                MPinBottomSheetComponent{mpin->
                    actionClose.invoke()
                    viewModel.fetchCharge(mpin)
                }
            }

        ) {toggleAction->
            Scaffold(
                backgroundColor = BackgroundColor,
                topBar = { NavTopBar(title = "Money Transfer") }
            ) {

                BaseContent(viewModel) {
                    AppFormUI(button = {}, cardContents = listOf(
                        AppFormCard { BuildTopSection(viewModel) },
                        AppFormCard(title = "Transaction Type") {
                            BuildTransactionType(viewModel)
                        },
                        AppFormCard(
                            title = "Transaction Amount",
                            contents = {
                                AmountTextField(
                                    value = viewModel.input.amount.getValue(),
                                    onChange = {viewModel.input.amount.setValue(it)},
                                    isOutline = true,
                                    error = viewModel.input.amount.formError()
                                )
                            }
                        ),
                        AppFormCard(

                            contents = {
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Text(text = "Commission/Charge", style = MaterialTheme.typography.h6.copy(
                                        fontSize = 16.sp
                                    ))
                                    Spacer(modifier = Modifier.weight(1f))
                                    TextButton(onClick = {
                                        val result =  viewModel.input.validate()
                                        if(!result) viewModel.failureBanner(
                                            title = "Amount Validation Failed",
                                            message = "Enter valid amount"
                                        )else {
                                            if(!keyboard.value) manager.clearFocus()
                                            toggleAction.invoke()
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Visibility,
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(text = "Show")
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        )
                    ))

                }

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

       if(viewModel.bankDownText.value.isNotEmpty()) BuildBlinkText(message = viewModel.bankDownText.value)

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
                Text(
                    beneficiary.bankName ?: "",
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = primaryColor,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    ("IFSC   : " + beneficiary.ifsc), style = TextStyle(
                        fontSize = 14.sp,
                        color = primaryColor.copy(alpha = 0.8f),
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    ("Name : " + beneficiary.name), style = TextStyle(
                        fontSize = 14.sp,
                        color = primaryColor.copy(alpha = 0.8f),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TitleValueVertically(title = "Account Number", value = beneficiary.accountNumber)
        if (beneficiary.bankVerified == 1)
            Row {
                Text(text = "Bank is verified", color = GreenColor)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = if (beneficiary.bankVerified == 1) GreenColor else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        else BuildBlinkText("Selected bank is not verified!")
    }

}

@Composable
private fun BuildBlinkText(message : String) {

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