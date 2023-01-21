package com.a2z.app.ui.screen.report.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.a2z.app.ui.component.common.AppDropDownMenu
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.DateTextField
import com.a2z.app.ui.screen.report.ledger.LedgerReportViewModel
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.util.DateUtil
import com.a2z.app.util.DateUtil.toFormat
import com.a2z.app.util.extension.removeDateSeparator

private val statusList = linkedMapOf(
    "All" to "",
    "Accepted" to "34",
    "Success" to "1",
    "Failure" to "2",
    "Pending" to "3",
    "Refunded" to "4",
    "Successful" to "24",
    "Refund Success" to "21",
    "Debit" to "6",
    "Credit" to "7",
)

private val productList = linkedMapOf(
    "All" to "",
    "Recharge" to "1",
    "A2Z Plus Wallet" to "25",
    "A2Z Plus Two" to "52",
    "A2Z Plus Three" to "53",
    "Acc Verification" to "2",
    "UPI Verification" to "63",
    "VPA Payment" to "62",
    "DMT One" to "50",
    "DMT Two" to "16",
    "BBPS One" to "15",
    "BBPS Two" to "40",
    "Bank Settlement" to "29",
    "Payment Gateway" to "34",
    "AEPS" to "10",
    "Aadhaar Pay" to "28",
    "Reload Card" to "57",
    "Travels" to "100",
    "M-ATM" to "92",
    "M-POS" to "93",
)

private fun criteriaList(value: String?) = when (value) {
    "Recharge" -> linkedMapOf(
        "Number" to "NUMBER",
        "Order ID" to "ID",
    )
    "A2Z Plus Wallet",
    "A2Z Plus Two",
    "A2Z Plus Three",
    "DMT One",
    "DMT Two",
    "Acc Verification",
    -> linkedMapOf(
        "Account Number" to "NUMBER",
        "Remitter Number" to "CUST_MOBILE",
        "Order ID" to "ID",
    )

    "UPI Verification",
    "VPA Payment",
    -> linkedMapOf(
        "UPI ID" to "NUMBER",
        "Remitter Number" to "CUST_MOBILE",
        "Order ID" to "ID",
    )

    "BBPS One", "BBPS Two" -> linkedMapOf(
        "Number" to "NUMBER",
        "Customer Number" to "CUST_MOBILE",
        "Order ID" to "ID",
    )
    "Aadhaar Pay", "AEPS" -> linkedMapOf(
        "Aadhaar Number" to "NUMBER",
        "Customer Number" to "CUST_MOBILE",
        "Order ID" to "ID",
    )
    "Reload Card" -> linkedMapOf(
        "Card Ref Number" to "NUMBER",
        "Mobile Number" to "CUST_MOBILE",
        "Order ID" to "ID",
    )
    "Payment Gateway" -> linkedMapOf(
        "Customer Number" to "CUST_MOBILE",
        "Order ID" to "ID",
    )
    "Bank Settlement" -> linkedMapOf(
        "Order ID" to "ID",
        "Account Number" to "NUMBER",
    )

    "Travels" -> linkedMapOf(
        "Order ID" to "ID",
        "PNR Number" to "NUMBER",
    )
    "M-POS", "M-ATM" -> linkedMapOf(
        "Card Number" to "NUMBER",
        "Customer Mobile" to "CUST_MOBILE",
        "Order ID" to "ORDERID",
    )
    else -> linkedMapOf()


}


@Composable
fun LedgerReportFilterComponent(
    onFilter: (LedgerReportViewModel.SearchInput) -> Unit
) {

    val currentDate = DateUtil.getDate()

    val statusState = rememberStateOf(value = "")
    val productState = rememberStateOf(value = "")
    val criteriaState = rememberStateOf(value = "")
    val startDateState = rememberStateOf(value = currentDate)
    val endDateState = rememberStateOf(value = currentDate)
    val inputState = rememberStateOf(value = "")



    BaseReportFilterComponent(
        onFilter = {

            val mStatus = statusList.entries.find { it.key == statusState.value }?.value
            val mProduct = productList.entries.find { it.key == productState.value }?.value
            val mCriteria = criteriaList(mProduct).entries.find { it.key == criteriaState.value }?.value

            onFilter(
                LedgerReportViewModel.SearchInput(
                    startDate = startDateState.value,
                    endDate = endDateState.value,
                    status = mStatus.orEmpty(),
                    product = mProduct.orEmpty(),
                    criteria = mCriteria.orEmpty(),
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
                selectedState = statusState,
                label = "Select Status",
                list = statusList.map { it.key },

                )
            AppDropDownMenu(
                selectedState = productState,
                label = "Select Product",
                list = productList.map { it.key }
            )
            AppDropDownMenu(
                selectedState = criteriaState,
                label = "Select Criteria",
                list = criteriaList(productState.value).map { it.key }
            )

            if (criteriaState.value.isNotEmpty()) AppTextField(
                value = inputState.value,
                onChange = { inputState.value = it },
                label = "Input Search Text"
            )

        }
    )
}