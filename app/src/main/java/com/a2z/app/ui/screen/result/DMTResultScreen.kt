package com.a2z.app.ui.screen.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.a2z.app.R
import com.a2z.app.data.model.dmt.TransactionDetail
import com.google.gson.Gson


@Composable
fun DMTResultScreen(it: NavBackStackEntry) {

    val arg = it.arguments?.getString("response")
    val response = Gson().fromJson(arg!!, TransactionDetail::class.java)


    val viewModel: TxnResultViewModel = hiltViewModel()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.recordId = response.reportId.toString()
    })

    val dmtTitleValue = listOf(
        "Name" to response.name.toString(),
        "Account No." to response.number.toString(),
        "Bank Name" to response.bankName.toString(),
        "IFSc Code" to response.ifsc.toString(),
        "Txn Type" to response.txnType.toString()

    )

    val paymentAmount = if (response.serviceName.equals("dmt one")) response.totalAmount
    else response.amount

    BaseResultComponent(
        statusId = response.status ?: 0,
        message = response.message.toString(),
        status = response.statusDesc ?: "NA",
        dateTime = response.txnTime ?: "NA",
        amountTopText = response.serviceName ?: "NA",
        amountBelowText = "Money Transfer",
        amount = paymentAmount ?: "0.0",
        serviceIconRes = R.drawable.ic_launcher_money,
        dmtInfo = response.dmtDetails,
        titleValues = arrayOf(dmtTitleValue),
        backPressHandle = response.isTransaction
    )
}





