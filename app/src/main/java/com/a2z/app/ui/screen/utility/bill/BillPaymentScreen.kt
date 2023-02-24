package com.a2z.app.ui.screen.utility.bill

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.component.permission.LocationComponent
import com.a2z.app.ui.dialog.BaseConfirmDialog
import com.a2z.app.ui.dialog.SpinnerSearchDialog
import com.a2z.app.ui.screen.home.component.HomeLocationServiceDialog
import com.a2z.app.ui.screen.utility.util.BillPaymentAction
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.theme.spacing
import com.a2z.app.util.extension.removeDateSeparator

@Composable
fun BillPaymentScreen() {
    val navController = LocalNavController.current
    val viewModel: BillPaymentViewModel = hiltViewModel()
    val util = viewModel.util


    BaseContent(viewModel) {
        Scaffold(
            topBar = {
                NavTopBar(title = util.getOperatorTitle("Payment"))
            }, backgroundColor = BackgroundColor
        ) {
            it.calculateBottomPadding()
            AppFormUI(
                button = { FormButton() },
                showWalletCard = viewModel.showCard(),
                cardContents = listOf(AppFormCard { FetchFormCard() })
            )



            HomeLocationServiceDialog()

            BaseConfirmDialog(
                title = "Confirm Bill Payment",
                state = viewModel.confirmDialogState,
                amount = viewModel.input.amountInputWrapper.formValue(),
                titleValues = listOf(
                    "BBPS Provider" to viewModel.selectedState.value,
                    "Operator" to viewModel.operator.operatorName.toString(),
                    "Number" to viewModel.input.numberInputWrapper.formValue(),
                    "Mobile" to viewModel.input.mobileInputWrapper.formValue()
                )
            ) { viewModel.makePayment() }

            SpinnerSearchDialog(
                title = "Select BBPS Provider",
                state = viewModel.spinnerDialogState,
                list = arrayListOf("BBPS Provider 1", "BBPS Provider 2"),
            ) { state ->
                viewModel.selectedState.value = state
            }
        }
    }
}


@Composable
private fun FetchFormCard() {
    val viewModel: BillPaymentViewModel = hiltViewModel()
    val util = viewModel.util

    val operator = viewModel.operator
    val image = operator.baseUrl + operator.providerImage

    Row(verticalAlignment = Alignment.CenterVertically) {
        AppNetworkImage(
            url = image, placeholderRes = util.getIconFromOperatorType()
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = operator.operatorName.toString(),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            if (viewModel.billInfo.value != null) TitleValueVertically(
                title = "Customer Ref",
                value = viewModel.billInfo.value?.customerRef
            )

        }
    }
    BuildInfoComponent()

    val input = viewModel.input
    val numberInputWrapper = input.numberInputWrapper
    val mobileInputWrapper = input.mobileInputWrapper
    val amountInputWrapper = input.amountInputWrapper
    val emailInputWrapper = input.emailInputWrapper
    val dobInputWrapper = input.dobInputWrapper


    val (numberKeyboardType, keyboardCapitalization) = util.getNumberFieldKeyboardOption()
    if (viewModel.billInfo.value == null) AppTextField(
        label = util.getNumberFieldLabel(),
        value = numberInputWrapper.formValue(),
        onChange = { numberInputWrapper.onChange(it) },
        error = numberInputWrapper.formError(),
        maxLength = util.inputMaxLength,
        keyboardType = numberKeyboardType,
        keyboardCapitalization = keyboardCapitalization,
        downText = util.getNumberFieldDownText()
    )

    if (util.useMobileValidation.value)
        if (viewModel.billInfo.value == null) MobileTextField(
            value = mobileInputWrapper.formValue(),
            onChange = { mobileInputWrapper.onChange(it) },
            error = mobileInputWrapper.formError(),
            topSpace = MaterialTheme.spacing.medium,
        )

    if (util.useEmailValidation.value)
        if (viewModel.billInfo.value == null) EmailTextField(
            value = emailInputWrapper.formValue(),
            onChange = { value -> emailInputWrapper.onChange(value) },
            error = emailInputWrapper.formError(),
            topSpace = MaterialTheme.spacing.medium,
            downText = "hint: abc@xyz.com"

        )
    if (util.useDobValidation.value)
        if (viewModel.billInfo.value == null) DateTextField(
            value = dobInputWrapper.formValue(),
            label = "Date of Birth",
            onChange = { value -> dobInputWrapper.onChange(value) },
            error = dobInputWrapper.formError(),
            topSpace = MaterialTheme.spacing.medium,
            downText = "hint : dob form 01/01/1970",
            onDateSelected = { dobInputWrapper.onChange(it.removeDateSeparator()) }
        )

    if (viewModel.util.actionType == BillPaymentAction.PROCEED_TO_PAYMENT)
        AmountTextField(
            value = amountInputWrapper.formValue(),
            onChange = { amountInputWrapper.onChange(it) },
            error = amountInputWrapper.formError(),
            topSpace = MaterialTheme.spacing.medium,
            downText = util.getAmountFieldDownText(),
            readOnly = viewModel.isAmountReadyOnly.value,
            isOutline = true
        )
    if (viewModel.util.actionType == BillPaymentAction.PROCEED_TO_PAYMENT)
        Column {
            Spacer(modifier = Modifier.height(12.dp))
            DropDownTextField(

                value = viewModel.selectedState.value,
                hint = "selected type",
                paddingValues = PaddingValues(horizontal = 0.dp),
            ) {
                viewModel.spinnerDialogState.value = true
            }
            Text(
                text = "Select BBPS Provider", style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            )
        }

}

@Composable
fun BuildInfoComponent() {
    val viewModel: BillPaymentViewModel = hiltViewModel()
    val billInfo = viewModel.billInfo.value
    if (billInfo != null)
        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .background(
                    color = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(12.dp)
        ) {
            TitleValueHorizontally(title = "Bill Amount", value = billInfo.billAmount)
            TitleValueHorizontally(title = "Bill Number", value = billInfo.billNumber)
            TitleValueHorizontally(title = "Bill Period", value = billInfo.billPeriod)
            TitleValueHorizontally(title = "Customer Name", value = billInfo.customerName)
            TitleValueHorizontally(title = "Bill Date", value = billInfo.Billdate, useNA = true)
            TitleValueHorizontally(title = "Due Date", value = billInfo.dueDate)
        }
}


@Composable
private fun FormButton() {
    val viewModel: BillPaymentViewModel = hiltViewModel()

    LocationComponent(
        onLocation = {
            viewModel.onButtonClick()
        }
    ) {
        AppButton(
            text = viewModel.util.buttonText,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            isEnable = viewModel.input.isValidObs.value
        ) {
            it.invoke()
        }
    }
}



