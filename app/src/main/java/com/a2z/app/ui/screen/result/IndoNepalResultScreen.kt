package com.a2z.app.ui.screen.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.a2z.app.R
import com.a2z.app.data.model.dmt.TransactionDetail
import com.a2z.app.data.model.indonepal.INTransferData
import com.a2z.app.util.AppUtil
import com.google.gson.Gson


@Composable
fun IndoNepalResultScreen(it: NavBackStackEntry) {

    val arg = it.arguments?.getString("response")
    val response = Gson().fromJson(arg!!, INTransferData::class.java)


    val viewModel: TxnResultViewModel = hiltViewModel()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.recordId = response.report_id.toString()

        AppUtil.logger("response : : "+response.toString())
    })

    val dmtTitleValue = listOf(
        "Name" to response.name.toString(),
        "Sender Name" to response.sender_name.toString(),
        "Sender Number" to response.sender_number.toString(),
        "Account No." to response.number.toString(),
        "Bank Name" to response.bank_name.toString(),
        "Bank Ref" to response.bank_ref.toString(),
        "Debit Charge" to response.debit_charge.toString(),

    )


    BaseResultComponent(
        statusId = response.status ?: 0,
        message = response.message.toString(),
        status = response.status_desc ?: "NA",
        dateTime = response.txn_time ?: "NA",
        amountTopText = response.service_name ?: "NA",
        amountBelowText = "Money Transfer",
        amount = response.amount ?: "0",
        serviceIconRes = R.drawable.ic_launcher_money,
        dmtInfo = null,
        titleValues = arrayOf(dmtTitleValue),
        backPressHandle = response.isTransaction
    )
}





