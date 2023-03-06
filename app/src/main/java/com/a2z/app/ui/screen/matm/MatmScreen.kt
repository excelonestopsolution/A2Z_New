package com.a2z.app.ui.screen.matm

import android.annotation.SuppressLint
import android.widget.FrameLayout
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.MainActivity
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.component.permission.LocationComponent
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.spacing
import com.a2z.app.util.VoidCallback
import com.a2z.app.util.extension.showToast
import com.mosambee.lib.MosCallback

@Composable
private fun rememberInitializeMATM(): MosCallback {

    val context = LocalContext.current
    val activity = context as MainActivity
    val frameLayout = FrameLayout(context)
    val primaryColor = "#1A5C91"
    val secondaryColor = "#D81B60"

    return remember {
        val mosCallback = MosCallback(context)
        mosCallback.setInternalUi(activity, false)
        mosCallback.initializeSignatureView(frameLayout, primaryColor, secondaryColor)
        mosCallback
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MatmScreen() {
    val viewModel: MatmViewModel = hiltViewModel()
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = viewModel.getTitle()) }
    ) {
        BaseContent(viewModels = arrayOf(viewModel)) {
            BuildMainContent()
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun BuildMainContent() {

    val viewModel: MatmViewModel = hiltViewModel()
    val input = viewModel.formInput
    val context = LocalContext.current

    viewModel.mosCallback = rememberInitializeMATM()

    AppFormUI(
        showWalletCard = false,
        button = {
            BluetoothServiceComponent(onResult = {
                context.showToast(it.toString())
                if (it) viewModel.initTransaction()

            }) { action ->

                LocationComponent(
                    onLocation = {
                        action.invoke()
                    }
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        enabled = input.isValidObs.value,
                        onClick = {
                            it.invoke()
                        },
                    ) { Text(text = "Proceed") }
                }
            }
        },
        cardContents = listOf(
            AppFormCard(
                contents = {

                    MobileTextField(
                        label = "Customer Mobile Number",
                        value = input.mobileInputWrapper.getValue(),
                        onChange = { input.mobileInputWrapper.setValue(it) },
                        error = input.mobileInputWrapper.formError()
                    )
                }
            ),

            AppFormCard(
                title = "Transaction Type",
                contents = {
                    if (!viewModel.isMPos) Row {
                        TransactionTypeButton(MatmTransactionType.CASH_WITHDRAWAL) {
                            viewModel.setTransactionType(MatmTransactionType.CASH_WITHDRAWAL)
                        }
                        TransactionTypeButton(MatmTransactionType.BALANCE_ENQUIRY) {
                            viewModel.setTransactionType(MatmTransactionType.BALANCE_ENQUIRY)
                        }
                    }

                    if (viewModel.isMPos) Row {
                        TransactionTypeButton(MatmTransactionType.M_POS) {
                            viewModel.setTransactionType(MatmTransactionType.M_POS)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    if (viewModel.transactionType.value == MatmTransactionType.CASH_WITHDRAWAL
                        || viewModel.transactionType.value == MatmTransactionType.M_POS
                    )
                        Column {
                            AmountTextField(
                                value = input.amountInputWrapper.getValue(),
                                onChange = { input.amountInputWrapper.setValue(it) },
                                error = input.amountInputWrapper.formError(),
                                isOutline = true, topSpace = MaterialTheme.spacing.large
                            )

                            Column(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray,
                                        shape = MaterialTheme.shapes.medium
                                    )
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "Amount Hints",
                                    style = MaterialTheme.typography.body1.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                val minAmount = viewModel.mPosLimit.value?.minAmount ?: 100
                                val maxAmount = viewModel.mPosLimit.value?.maxAmount ?: 20000

                                if (viewModel.isMPos) Text(text = "Enter amount in range of $minAmount to $maxAmount")
                                else Text(text = "Enter amount in range of 100 to 10000")
                                 Text(text = "Enter amount multiple of 10")
                                 Text(text = "Such as 100, 110, 120 ... 1000")


                            }
                        }

                }
            )
        ))

}


@Composable
fun RowScope.TransactionTypeButton(
    transactionType: MatmTransactionType,
    onClick: VoidCallback
) {


    val title = when (transactionType) {
        MatmTransactionType.M_POS -> "Point Of Sale"
        MatmTransactionType.CASH_WITHDRAWAL -> "Cash Withdrawal"
        MatmTransactionType.BALANCE_ENQUIRY -> "Balance Enquiry"
    }

    val viewModel: MatmViewModel = hiltViewModel()

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