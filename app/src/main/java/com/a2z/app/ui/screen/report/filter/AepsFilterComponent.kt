package com.a2z.app.ui.screen.report.aeps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.a2z.app.ui.component.common.AppDropDownMenu
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.DateTextField
import com.a2z.app.ui.screen.report.filter.BaseReportFilterComponent
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.util.DateUtil
import com.a2z.app.util.extension.removeDateSeparator


private val transactionTypeList = linkedMapOf(
    "All" to "",
    "Balance Enquiry" to "BALANCE_INQUIRY",
    "Mini Statement" to "AEPS(MINI_STATEMENT)",
    "Cash Withdrawal" to "AEPS(CASH_WITHDRAWAL)",
    "Aadhaar Pay" to "Wallet Update(Adhaar Pay)"
)

private val inputModeList = linkedMapOf(
    "None" to "",
    "Mobile Number" to "MOB",
    "Aadhaar Number" to "AADHAAR",
)

private val transactionStatusList = linkedMapOf(
    "All" to "",
    "Success" to "1",
    "Failure" to "2",
    "Pending" to "3",
)


@Composable
fun AepsReportFilterDialog(
    dialogState : MutableState<Boolean>,
    onFilter: (AEPSReportViewModel.SearchInput) -> Unit
) {

    val currentDate = DateUtil.getDate()

    val statusState = rememberStateOf(value = "")
    val txnTypeState = rememberStateOf(value = "")
    val inputModeState = rememberStateOf(value = "")
    val startDateState = rememberStateOf(value = currentDate)
    val endDateState = rememberStateOf(value = currentDate)
    val inputState = rememberStateOf(value = "")


    if(dialogState.value) Dialog(onDismissRequest = {dialogState.value = false}) {
        BaseReportFilterComponent(
            onFilter = {

                val mStatus = transactionStatusList.entries.find { it.key == statusState.value }?.value
                val mInputMode = inputModeList.entries.find { it.key == inputModeState.value }?.value
                val mTxnType = transactionTypeList.entries.find { it.key == txnTypeState.value }?.value

                dialogState.value = false
                onFilter(
                    AEPSReportViewModel.SearchInput(
                        startDate = startDateState.value,
                        endDate = endDateState.value,
                        status = mStatus.orEmpty(),
                        searchType = mInputMode.orEmpty(),
                        txnType = mTxnType.orEmpty(),
                        input = inputState.value.trim()
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
                    selectedState = statusState,
                    label = "Select Status",
                    list = transactionStatusList.map { it.key },

                    )
                AppDropDownMenu(
                    selectedState = txnTypeState,
                    label = "Transaction Type",
                    list = transactionTypeList.map { it.key }
                )
                AppDropDownMenu(
                    selectedState = inputModeState,
                    label = "Input Mode",
                    list = inputModeList.map { it.key }
                )

                if (inputModeState.value.isNotEmpty() && inputModeState.value != "None") AppTextField(
                    value = inputState.value,
                    onChange = {
                        if (it == "," || it == "-" || it == ".") {
                        } else inputState.value = it
                    },
                    label = "Input Search Text",
                    keyboardType = KeyboardType.Number
                )

            }
        )
    }
}