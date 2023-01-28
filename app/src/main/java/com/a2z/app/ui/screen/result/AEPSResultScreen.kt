package com.a2z.app.ui.screen.result

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.a2z.app.R
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.dmt.MiniStatement
import com.a2z.app.data.model.dmt.TransactionDetail
import com.google.gson.Gson


@Composable
fun AEPSResultScreen(it: NavBackStackEntry) {

    val arg = it.arguments?.getString("response")
    val response = Gson().fromJson(arg!!, AepsTransaction::class.java)

    val orderId = if (response.order_id.isNullOrEmpty())
        response.txn_id.toString()
    else response.order_id.toString()

    val viewModel: TxnResultViewModel = hiltViewModel()
    viewModel.recordId = response.record_id.toString()

    if(response.isLedgerReport)
        viewModel.receiptType = TxnResultPrintReceiptType.OTHER
    else viewModel.receiptType = TxnResultPrintReceiptType.AEPS




    val upiTitleValue1 = listOf(

        "Order Id" to orderId,
        "Bank Ref" to response.bank_ref.toString(),
        "Bank Name" to response.bank_ref.toString(),
        "Aadhaar No." to response.aadhaar_number.toString(),
        "Mobile No" to response.customer_number.toString(),
    )

    BaseResultComponent(
        statusId = response.status ?: 0,
        message = response.message.toString(),
        status = response.status_desc ?: "NA",
        dateTime = response.txn_time ?: "NA",
        amountTopText = response.transaction_type ?: "NA",
        amountBelowText = response.service_name.toString(),
        amount = response.transaction_amount ?: "0.0",
        availableBalance = response.available_amount ?: "0.0",
        serviceIconRes = R.drawable.ic_launcher_aeps2,
        titleValues = arrayOf(upiTitleValue1),
        isPaymentAmount = false,
        iconSize = 42,
        statement = response.statement,
        backPressHandle = response.isTransaction
    )
}





