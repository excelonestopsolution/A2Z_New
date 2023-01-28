package com.a2z.app.ui.screen.utility.recharge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.component.permission.LocationComponent
import com.a2z.app.ui.dialog.BaseConfirmDialog
import com.a2z.app.ui.screen.home.component.HomeLocationServiceDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.ui.util.resource.StatusDialogType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RechargeScreen(

) {
    val navController = LocalNavController.current

    val viewModel: RechargeViewModel = hiltViewModel()



    BaseContent(viewModel) {
        Scaffold(
            topBar = {
                NavTopBar(
                    title = viewModel.util.getOperatorTitle("Recharge")
                )
            },
            backgroundColor = BackgroundColor
        ) {
            it.calculateBottomPadding()
            AppFormUI(
                button = { FormButton(viewModel) },
                cardContents = listOf(AppFormCard { FormCard(viewModel) })
            )
            ROfferDialog()

            val scope = rememberCoroutineScope()
            BaseConfirmDialog(
                title = "Confirm Recharge",
                state = viewModel.confirmDialogState,
                amount = viewModel.input.amountInputWrapper.formValue(),
                titleValues = listOf(
                    "Operator" to viewModel.operator.operatorName.toString(),
                    "Mobile" to viewModel.input.numberInputWrapper.formValue()
                )
            ) {

                proceedToRecharge(scope, viewModel, navController)
            }


            HomeLocationServiceDialog()
        }

    }
}


private fun proceedToRecharge(
    scope: CoroutineScope,
    viewModel: RechargeViewModel,
    navController: NavHostController,
) {

    scope.launch {
        viewModel.rechargeTransaction().collect {
            when (it) {
                is ResultType.Failure -> {
                    viewModel.dialogState.value = StatusDialogType.Failure(
                        it.exception.message.toString()
                    )
                }
                is ResultType.Loading ->
                    viewModel.dialogState.value = StatusDialogType.Transaction
                is ResultType.Success -> {
                    val status = it.data.status
                    val message = it.data.message
                    when (status) {
                        1, 2, 3, 34 -> {
                            viewModel.dialogState.value = StatusDialogType.None
                            val response = it.data
                            navController
                                .navigate(NavScreen.RechargeTxnScreen.passArgs(
                                    response.apply {
                                        val numberTitle = if (
                                            viewModel.operator.dealerName == null
                                            || viewModel.operator.dealerName.toLowerCase() == "null"
                                            || viewModel.operator.dealerName.toLowerCase() == "nul"
                                            || viewModel.operator.dealerName.isEmpty()
                                        ) "Number" else viewModel.operator.dealerName
                                        this.providerIcon = viewModel.util.getIconFromOperatorType()
                                        this.serviceName = viewModel.util.getOperatorTitle()
                                        this.number = viewModel.input.numberInputWrapper.getValue()
                                        this.numberTitle = numberTitle
                                        this.isTransaction = true

                                    }
                                ))
                        }
                        else -> viewModel.dialogState.value =
                            StatusDialogType.Alert(message)
                    }

                }
            }
        }
    }
}


@Composable
private fun FormCard(viewModel: RechargeViewModel) {

    @Composable
    fun ROfferButton() {
        Button(onClick = {
            viewModel.onFetchInfoButtonClick()
        }, modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(text = viewModel.getFetchInfoButtonText())
        }
    }


    val operator = viewModel.operator
    val image = operator.baseUrl + operator.providerImage

    Row(verticalAlignment = Alignment.CenterVertically) {
        AppNetworkImage(
            url = image, placeholderRes = viewModel.util.getIconFromOperatorType()
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = operator.operatorName.toString(),
            style = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }


    val input = viewModel.input
    val numberInputWrapper = input.numberInputWrapper
    val amountInputWrapper = input.amountInputWrapper


    val isMobile = viewModel.isPrepaid()


    AppTextField(
        label = if (isMobile) "Mobile Number" else "DTH Number",
        value = numberInputWrapper.formValue(),
        onChange = {
            numberInputWrapper.onChange(it)
            viewModel.onNumberChange(it)
        },
        error = numberInputWrapper.formError(),
        trailingIcon = { if (numberInputWrapper.isValid()) ROfferButton() },
        maxLength = viewModel.inputMaxLength,
        keyboardType = KeyboardType.Number,
        downText = viewModel.inputDownText

    )



    Spacer(modifier = Modifier.height(12.dp))
    AmountTextField(
        value = amountInputWrapper.formValue(),
        onChange = {
            amountInputWrapper.onChange(it)
            viewModel.onAmountChange(it)
        },
        error = amountInputWrapper.formError(),
        downText = "Enter amount ${viewModel.inputMinAmount} to 10000"
    )

    if (viewModel.rOfferState.value != null)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(BackgroundColor)
        ) {

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "₹ " + viewModel.rOfferState.value!!.price,
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                    )
                )
                Text(
                    text = viewModel.rOfferState.value!!.offer, style = TextStyle(
                        fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray
                    )
                )
            }
        }
    if (viewModel.dthInfoState.value != null)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(BackgroundColor)
        ) {

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "₹ " + viewModel.rOfferState.value!!.price,
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                    )
                )
                Text(
                    text = viewModel.rOfferState.value!!.offer, style = TextStyle(
                        fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray
                    )
                )
            }
        }

}


@Composable
private fun FormButton(viewModel: RechargeViewModel) {

    LocationComponent(
        onLocation = {
            viewModel.confirmDialogState.value = true
        }
    ) {
        AppButton(
            text = "Proceed To Recharge",
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            isEnable = viewModel.input.isValidObs.value
        ) {
            it.invoke()
        }
    }
}



