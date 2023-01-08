package com.a2z.app.ui.screen.result

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.a2z.app.R
import com.a2z.app.data.model.dmt.TransactionDetail
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.google.gson.Gson


@Composable
fun DMTResultScreen(it: NavBackStackEntry) {

    val arg = it.arguments?.getString("response")
    val response = Gson().fromJson (arg!!,TransactionDetail::class.java)


    BaseResultComponent(
        statusId = response.status ?: 0,
        message = response.message.toString(),
        status = response.statusDesc ?: "NA",
        dateTime = response.txnTime ?: "NA",
        serviceName = response.serviceName ?: "NA",
        providerName = "Money Transfer",
        amount = response.amount ?: "0.0",
        serviceIconRes = R.drawable.ic_launcher_money,
        dmtInfo = response.dmtDetails,
        titleValues = listOf(
           "Name" to response.name.toString(),
            "Account No." to response.number.toString(),
            "Bank Name" to response.bankName.toString(),
            "IFSc Code" to response.ifsc.toString(),
            "Txn Type" to response.txnType.toString()

        )
    )
}





