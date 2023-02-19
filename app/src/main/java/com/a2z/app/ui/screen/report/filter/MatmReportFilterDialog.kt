package com.a2z.app.ui.screen.report.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.Dialog
import com.a2z.app.ui.component.common.AppDropDownMenu
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.DateTextField
import com.a2z.app.ui.screen.report.matm.MatmReportViewModel
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.util.DateUtil
import com.a2z.app.util.extension.removeDateSeparator

private val transactionTypeList = linkedMapOf(
    "All" to "",
    "Balance Enquiry" to "MATM(BALANCE_INQUIRY)",
    "Cash Withdrawal" to "MATM(CASH_WITHDRAWAL)",
    "Mpos / Sale" to "SALE",
)

private val inputModeList = linkedMapOf(
    "None" to "",
    "Card Number" to "NUMBER",
    "Customer Mobile" to "CUST_MOBILE",
    "Order ID" to "ORDERID",
)

private val transactionStatusList = linkedMapOf(
    "All" to "",
    "Success" to "1",
    "Failure" to "2",
    "Pending" to "3",
)


@Composable
fun MatmReportFilterDialog(
    dialogState : MutableState<Boolean>,
    onFilter: (MatmReportViewModel.SearchInput) -> Unit
) {

    val currentDate = DateUtil.getDate()

    val statusState = rememberStateOf(value = "")
    val transactionTypeState = rememberStateOf(value = "")
    val inputModeState = rememberStateOf(value = "")
    val startDateState = rememberStateOf(value = currentDate)
    val endDateState = rememberStateOf(value = currentDate)
    val inputState = rememberStateOf(value = "")



    if(dialogState.value) Dialog(onDismissRequest = {
        dialogState.value = false
    }) {
        BaseReportFilterComponent(
            onFilter = {

                val mStatus = transactionStatusList.entries.find { it.key == statusState.value }?.value
                val mTransactionType =
                    transactionTypeList.entries.find { it.key == transactionTypeState.value }?.value
                val mInputMode = inputModeList.entries.find { it.key == inputModeState.value }?.value

                dialogState.value = false
                onFilter(
                    MatmReportViewModel.SearchInput(
                        startDate = startDateState.value,
                        endDate = endDateState.value,
                        status = mStatus.orEmpty(),
                        transactionType = mTransactionType.orEmpty(),
                        inputMode = mInputMode.orEmpty(),
                        input = inputState.value
                    )
                )
            },
            content = {
                DateTextField(
                    label = "Start Date",
                    value = startDateState.value,
                    onChange = { startDateState.value = it },
                    onDateSelected = {
                        startDateState.value = it.removeDateSeparator()
                    }
                )

                DateTextField(
                    label = "End Date",
                    value = endDateState.value,
                    onChange = { endDateState.value = it },
                    onDateSelected = {
                        endDateState.value = it.removeDateSeparator()
                    }
                )

                AppDropDownMenu(
                    selectedState = transactionTypeState,
                    label = "Transaction Type",
                    list = transactionTypeList.map { it.key },

                    )
                AppDropDownMenu(
                    selectedState = statusState,
                    label = "Transaction Status",
                    list = transactionStatusList.map { it.key }
                )
                AppDropDownMenu(
                    selectedState = inputModeState,
                    label = "Input Mode",
                    list = inputModeList.map { it.key }
                )

                if (inputModeState.value != "None"
                    && inputModeState.value.isNotEmpty())
                    AppTextField(
                        value = inputState.value,
                        onChange = { inputState.value = it },
                        label = "Input Search Text"
                    )

            }
        )
    }

}