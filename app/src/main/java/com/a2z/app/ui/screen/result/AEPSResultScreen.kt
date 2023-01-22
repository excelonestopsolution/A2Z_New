package com.a2z.app.ui.screen.result

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.a2z.app.R
import com.a2z.app.data.model.dmt.TransactionDetail
import com.google.gson.Gson


@Composable
fun AEPSResultScreen(it: NavBackStackEntry) {

    val arg = it.arguments?.getString("response")
    val response = Gson().fromJson (arg!!,TransactionDetail::class.java)


    val upiTitleValue1 = listOf(

        "Order Id" to response.reportId.toString(),
        "Bank Ref" to response.bankRef.toString(),
        "Bank Name" to response.bankName.toString(),
        "Aadhaar No." to response.number.toString(),
        "Mobile No" to response.senderNumber.toString(),
    )

    BaseResultComponent(
        statusId = response.status ?: 0,
        message = response.message.toString(),
        status = response.statusDesc ?: "NA",
        dateTime = response.txnTime ?: "NA",
        amountTopText = response.txnType ?: "NA",
        amountBelowText = response.serviceName.toString(),
        amount = response.amount ?: "0.0",
        availableBalance = response.totalAmount ?: "0.0",
        serviceIconRes = R.drawable.ic_launcher_aeps2,
        titleValues = arrayOf(upiTitleValue1),
        isPaymentAmount = false,
        backPressHandle = response.isTransaction
    )
}





