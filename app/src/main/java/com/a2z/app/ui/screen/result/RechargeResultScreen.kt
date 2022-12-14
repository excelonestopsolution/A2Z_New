package com.a2z.app.ui.screen.result

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.util.AppConstant
import com.google.gson.Gson


@Composable
fun RechargeResultScreen(it: NavBackStackEntry) {

    val arg = it.arguments?.getString("response")
    val response = Gson().fromJson (arg!!,RechargeTransactionResponse::class.java)


    BaseResultComponent(
        statusId = response.status,
        message = response.message,
        status = response.statusDescription ?: "NA",
        dateTime = response.dateTime ?: "NA",
        amountTopText = response.serviceName ?: AppConstant.NA,
        amountBelowText = response.providerName ?: "NA",
        amount = response.amount ?: "0.0",
        serviceIconRes = response.providerIcon,
        titleValues = arrayOf(listOf(
            "Order Id" to response.recordId.toString(),
            "Operator Ref" to response.operatorRef.toString(),
            response.numberTitle.toString() to response.number.toString()
        )),
        backPressHandle = response.isTransaction
    )
}





