package com.a2z.app.ui.screen.result

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.a2z.app.R
import com.a2z.app.data.model.matm.MatmTransactionResponse
import com.google.gson.Gson


@Composable
fun MatmResultScreen(it: NavBackStackEntry) {

    val arg = it.arguments?.getString("response")
    val response = Gson().fromJson (arg!!,MatmTransactionResponse::class.java)


    val upiTitleValue1 = listOf(
        "Name" to response.txnId.toString(),
        "Upi Id" to response.orderId.toString(),
        "Sender Name" to response.bankRef.toString(),
        "Sender Number" to response.cardNumber.toString(),
        "Sender Number" to response.customerNumber.toString(),
        "Sender Number" to response.cardType.toString(),
        "Sender Number" to response.transactionMode.toString(),
    )

    BaseResultComponent(
        statusId = response.status ?: 0,
        message = response.message.toString(),
        status = response.statusDesc ?: "NA",
        dateTime = response.txnTime ?: "NA",
        amountTopText = response.transactionType ?: "NA",
        amountBelowText = response.serviceName.toString(),
        amount = response.transactionAmount ?: "0.0",
        availableBalance = response.availableAmount ?: "0.0",
        serviceIconRes = R.drawable.ic_launcher_matm,
        titleValues = arrayOf(upiTitleValue1),
        isPaymentAmount = false,
    )
}





