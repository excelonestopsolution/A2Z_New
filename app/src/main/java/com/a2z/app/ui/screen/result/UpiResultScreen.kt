package com.a2z.app.ui.screen.result

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.a2z.app.R
import com.a2z.app.data.model.dmt.TransactionDetail
import com.google.gson.Gson


@Composable
fun UPIResultScreen(it: NavBackStackEntry) {

    val arg = it.arguments?.getString("response")
    val response = Gson().fromJson (arg!!,TransactionDetail::class.java)
    val viewModel : TxnResultViewModel = hiltViewModel()
    viewModel.recordId  = response.reportId.toString()


    val upiTitleValue1 = listOf(
        "Name" to response.name.toString(),
        "Upi Id" to response.number.toString(),
        "Sender Name" to response.senderName.toString(),
        "Sender Number" to response.senderNumber.toString(),
    )
    val upiTitleValue2 = listOf(
        "Txn Id" to response.reportId.toString(),
        "Bank Ref" to response.bankRef.toString(),
    )

    BaseResultComponent(
        statusId = response.status ?: 0,
        message = response.message.toString(),
        status = response.statusDesc ?: "NA",
        dateTime = response.txnTime ?: "NA",
        amountTopText = response.serviceName ?: "NA",
        amountBelowText = "UPI Payment",
        amount = response.amount ?: "0.0",
        serviceIconRes = R.drawable.ic_launcher_money,
        titleValues = arrayOf(upiTitleValue1,upiTitleValue2),
        backPressHandle = response.isTransaction,
        commissionAmount = true
    )
}





