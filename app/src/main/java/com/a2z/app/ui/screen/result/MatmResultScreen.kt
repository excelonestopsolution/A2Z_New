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
        "Txn Id" to response.txnId.toString(),
        "Order Id" to response.orderId.toString(),
        "Bank Ref" to response.bankRef.toString(),
        "Card Number" to response.cardNumber.toString(),
        "Mobile Number" to response.customerNumber.toString(),
        "Card Type" to response.cardType.toString(),
        "Transaction Mode" to response.transactionMode.toString(),
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
        backPressHandle = response.isTransaction
    )
}





