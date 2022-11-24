package com.a2z.app.ui.screen.result

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.a2z.app.R
import com.a2z.app.data.model.utility.RechargeTransactionResponse
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
        serviceName = "Recharge",
        providerName = response.operatorName ?: "NA",
        amount = response.amount ?: "0.0",
        serviceIconRes = R.drawable.ic_launcher_mobile,
        titleValues = listOf(
            "Order Id" to response.recordId.toString(),
            "Operator Ref" to response.operatorRef.toString(),
            "Mobile Number" to response.mobileNumber.toString()
        )
    )
}





