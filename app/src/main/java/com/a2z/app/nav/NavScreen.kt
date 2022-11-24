package com.a2z.app.nav

import android.net.Uri
import android.os.Parcelable
import com.a2z.app.data.model.fund.FundMethod
import com.a2z.app.data.model.fund.FundRequestBank
import com.a2z.app.data.model.provider.Operator
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.ui.screen.utility.util.OperatorType
import com.a2z.app.util.AppException
import com.a2z.app.util.AppUtil
import com.google.gson.Gson
import java.io.Serializable


private fun String.params(vararg args: String): String {
    val builder = StringBuilder(this)
    args.forEach { builder.append("?${it}={${it}}") }
    AppUtil.logger("route : ${builder.toString()}")
    return builder.toString()
}

private fun String.args(vararg pairs: Pair<String, String>): String {
    val builder = StringBuilder(this)
    pairs.forEach { builder.append("?${it.first}=${it.second}") }
    AppUtil.logger("route : ${builder.toString()}")
    return builder.toString()
}

private fun Parcelable.toEncodedString(): String {
    val json = Gson().toJson(this)
    return Uri.encode(json)
}

fun Serializable.toEncodedString(): String {
    val json = Gson().toJson(this)
    return Uri.encode(json)
}


sealed class NavScreen(val route: String) {
    object ExceptionScreen : NavScreen("exception-screen".params("exception")) {
        fun passArgs(exception: Exception) =
            "exception-screen".args(
                "exception" to AppException(exception).toEncodedString()
            )
    }

    object DeviceLockScreen : NavScreen("device-lock-screen")
    object LoginScreen : NavScreen("login-screen")

    object LoginOtpScreen : NavScreen("login-otp-screen".params("mobile")) {
        fun passArgs(mobile: String) = "login-otp-screen".args("mobile" to mobile)
    }

    object DashboardScreen : NavScreen("dashboard-screen".params("fromLogin")){
        fun passArgs(fromLogin: Boolean = true) = "dashboard-screen".args(
            "fromLogin" to fromLogin.toString()
        )
    }


    object OperatorScreen : NavScreen("operator-screen".params("operatorType")) {
        fun passArgs(operatorType: OperatorType) =
            "operator-screen".args(
                "operatorType" to operatorType.toEncodedString()
            )

    }

    object RechargeScreen : NavScreen(
        "recharge-screen".params(
            "operatorType", "operator"
        )
    ) {
        fun passArgs(operatorType: OperatorType, operator: Operator) =
            "recharge-screen".args(
                "operatorType" to operatorType.toEncodedString(),
                "operator" to operator.toEncodedString()
            )
    }

    object BillPaymentScreen : NavScreen(
        "bill-payment-screen".params(
            "operatorType", "operator"
        )
    ) {
        fun passArgs(operatorType: OperatorType, operator: Operator) =
            "bill-payment-screen".args(
                "operatorType" to operatorType.toEncodedString(),
                "operator" to operator.toEncodedString()
            )
    }

    object RechargeTxnScreen : NavScreen("recharge-txn-screen".params("response")) {
        fun passArgs(response: RechargeTransactionResponse) = "recharge-txn-screen".args(
            "response" to response.toEncodedString()
        )
    }

    object FundMethodScreen : NavScreen("fund-method-screen")
    object FundBankListScreen : NavScreen("fund-bank-lists-screen".params("fundMethod")) {
        fun passData(fundMethod: FundMethod) =
            "fund-bank-lists-screen".args("fundMethod" to fundMethod.toEncodedString())
    }

    object FundRequestScreen :
        NavScreen("fund-request-screen".params("fundRequestBank", "fundMethod")) {
        fun passData(fundMethod: FundMethod, fundRequestBank: FundRequestBank) =
            "fund-request-screen".args(
                "fundRequestBank" to fundRequestBank.toEncodedString(),
                "fundMethod" to fundMethod.toEncodedString(),
            )

    }

    object PermissionScreen : NavScreen("permission-screen".params("permissionType")) {
        fun passData(permissionType: String) =
            "permission-screen".args("permissionType" to permissionType)

    }

    object TestScreen : NavScreen("test-screen")
}