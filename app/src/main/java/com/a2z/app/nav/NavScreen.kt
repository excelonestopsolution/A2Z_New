package com.a2z.app.nav

import android.net.Uri
import android.os.Parcelable
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.auth.RegistrationRole
import com.a2z.app.data.model.auth.RegistrationRoleUser
import com.a2z.app.data.model.dmt.*
import com.a2z.app.data.model.fund.FundMethod
import com.a2z.app.data.model.fund.FundRequestBank
import com.a2z.app.data.model.matm.MatmTransactionResponse
import com.a2z.app.data.model.provider.Operator
import com.a2z.app.data.model.report.CommissionSchemeDetailResponse
import com.a2z.app.data.model.settlement.SettlementAddedBank
import com.a2z.app.data.model.utility.BillPaymentResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.ui.screen.aeps.AepsType
import com.a2z.app.ui.screen.dmt.sender.register.SenderRegistrationArgs
import com.a2z.app.ui.screen.dmt.transfer.MoneyTransferArgs
import com.a2z.app.ui.screen.dmt.util.DMTType
import com.a2z.app.ui.screen.members.list.MemberListType
import com.a2z.app.ui.screen.util.permission.PermissionType
import com.a2z.app.ui.screen.utility.util.OperatorType
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

    object DeviceLockScreen : NavScreen("device-lock-screen")
    object LoginScreen : NavScreen("login-screen")

    object LoginOtpScreen : NavScreen("login-otp-screen".params("mobile")) {
        fun passArgs(mobile: String) = "login-otp-screen".args("mobile" to mobile)
    }

    object DashboardScreen : NavScreen("dashboard-screen".params("fromLogin")) {
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

    object BillPaymentTxnScreen : NavScreen("bill-payment-txn-screen".params("response")) {
        fun passArgs(response: BillPaymentResponse) = "bill-payment-txn-screen".args(
            "response" to response.toEncodedString()
        )
    }

    object DMTTxnScreen : NavScreen("dmt-txn-screen".params("response")) {
        fun passArgs(response: TransactionDetail) = "dmt-txn-screen".args(
            "response" to response.toEncodedString()
        )
    }

    object UPITxnScreen : NavScreen("upi-txn-screen".params("response")) {
        fun passArgs(response: TransactionDetail) = "upi-txn-screen".args(
            "response" to response.toEncodedString()
        )
    }

    object AEPSTxnScreen : NavScreen("aeps-txn-screen".params("response")) {
        fun passArgs(response: AepsTransaction) = "aeps-txn-screen".args(
            "response" to response.toEncodedString()
        )
    }

    object MATMTxnScreen : NavScreen("matm-txn-screen".params("response")) {
        fun passArgs(response: MatmTransactionResponse) = "matm-txn-screen".args(
            "response" to response.toEncodedString()
        )
    }

    object UpiPaymentScreen : NavScreen("upi-payment-screen")
    object PaymentGatewayScreen : NavScreen("payment-gateway-screen")
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

    object ParentPaymentReturnScreen : NavScreen("parent-payment-return-screen")


    object PermissionScreen : NavScreen("permission-screen".params("permissionType")) {
        fun passData(permissionType: PermissionType): String {
            val gson = Gson()
            val json = gson.toJson(permissionType)
            return "permission-screen".args("permissionType" to Uri.encode(json))
        }


    }

    object ShowQRScreen : NavScreen("show-qr-screen") {

    }

    object TestScreen : NavScreen("test-screen")
    object AepsScreen : NavScreen("aeps-screen".params("aeps_type")) {
        fun passArgs(aepsType: AepsType) =
            "aeps-screen".args("aeps_type" to aepsType.toEncodedString())
    }

    object MatmScreen : NavScreen("matm-screen".params("isMPos")) {
        fun passArgs(isMPos: Boolean) = "matm-screen".args("isMPos" to isMPos.toString())
    }

    //dmt screens
    object DmtSenderSearchScreen : NavScreen("sender-search-screen".params("dmtType", "bankDown")) {
        fun passArgs(dmtType: DMTType, bankDown: BankDownResponse): String {
            return "sender-search-screen".args(
                "dmtType" to dmtType.toEncodedString(),
                "bankDown" to bankDown.toEncodedString(),
            )
        }
    }

    object DmtBeneficiaryListInfoScreen :
        NavScreen("beneficiary-list-screen".params("moneySender", "dmtType", "accountDetail")) {
        fun passArgs(
            moneySender: MoneySender,
            dmtType: DMTType,
            accountDetail: SenderAccountDetail
        ) =
            "beneficiary-list-screen".args(
                "moneySender" to moneySender.toEncodedString(),
                "dmtType" to dmtType.toEncodedString(),
                "accountDetail" to accountDetail.toEncodedString()
            )
    }

    object DmtBeneficiaryRegisterScreen :
        NavScreen("beneficiary-register-screen".params("moneySender")) {
        fun passArgs(moneySender: MoneySender) =
            "beneficiary-register-screen".args("moneySender" to moneySender.toEncodedString())
    }

    object UpiBeneficiaryRegisterScreen :
        NavScreen("upi-beneficiary-register-screen".params("moneySender")) {
        fun passArgs(moneySender: MoneySender) =
            "upi-beneficiary-register-screen".args("moneySender" to moneySender.toEncodedString())
    }


    object DmtSenderRegisterScreen :
        NavScreen("beneficiary-sender-screen".params("senderRegistrationArgs")) {
        fun passArgs(args: SenderRegistrationArgs) =
            "beneficiary-sender-screen".args(
                "senderRegistrationArgs" to args.toEncodedString()
            )
    }


    object DMTMoneyTransferScreen :
        NavScreen("money-transfer-screen".params("moneyTransferArgs")) {
        fun passArgs(args: MoneyTransferArgs) =
            "money-transfer-screen".args(
                "moneyTransferArgs" to args.toEncodedString()
            )
    }


    object R2RTransferScreen : NavScreen("r2r-transfer-screen")


    object QRScanScreen : NavScreen("qr-scan-screen")
    object ChangePasswordScreen : NavScreen("change-password-screen")
    object ChangePinScreen : NavScreen("change-pin-screen")
    object SettlementBankScreen : NavScreen("settlement-bank-screen")
    object SettlementBankAddScreen : NavScreen("settlement-bank-add-screen")
    object SettlementTransferScreen : NavScreen("settlement-transfer-screen".params("bank")) {
        fun passArgs(bank: SettlementAddedBank) = "settlement-transfer-screen".args(
            "bank" to bank.toEncodedString()
        )
    }


    object DocumentKycScreen : NavScreen("document-kyc-screen")
    object AEPSKycScreen : NavScreen("aeps-kyc-screen")
    object AadhaarKycScreen : NavScreen("aadhaar-kyc-screen")
    object CommissionScreen : NavScreen("commission-screen")
    object SchemeDetailScreen : NavScreen("scheme-detail-screen".params("data")) {
        fun passArgs(data: CommissionSchemeDetailResponse) = "scheme-detail-screen".args(
            "data" to data.toEncodedString()
        )
    }

    object PanServiceScreen : NavScreen("pan-service-screen")

    object DeviceOrderScreen : NavScreen("device-order-screen")
    object UserAgreementScreen : NavScreen("user-agreement-screen")
    object UserRegistrationScreen : NavScreen(
        "user-registration-screen".params(
            "role", "mapRole", "selfRegister"
        )
    ) {
        fun passArg(
            role: RegistrationRole? = null,
            mapRole: RegistrationRoleUser? = null,
            selfRegister: Boolean
        ): String {
            return "user-registration-screen".args(
                "role" to role?.toEncodedString().toString(),
                "mapRole" to mapRole?.toEncodedString().toString(),
                "selfRegister" to selfRegister.toString()
            )
        }
    }

    object RegistrationTypeScreen : NavScreen("user-role-registration-screen".params("shouldMap")) {
        fun passArgs(shouldMap: Boolean) =
            "user-role-registration-screen".args("shouldMap" to shouldMap.toString())
    }

    object ForgotPasswordScreen : NavScreen("forgot-password-screen")
    object ForgotLoginIdScreen : NavScreen("forgot-login-id-screen")
    object ComplaintScreen : NavScreen("complaint-screen")
    object MATMRequestReportScreen : NavScreen("matm-request-report-screen")
    object AEPSReportScreen : NavScreen("aeps-report-screen")
    object FundReportScreen : NavScreen("fund-report-screen")
    object DTReportScreen : NavScreen("dt-report-screen")
    object PGReportScreen : NavScreen("pg-report-screen")
    object FundTransferReportScreen : NavScreen("fund-transfer-report-screen")
    object PaymentReportScreen : NavScreen("payment-report-screen")
    object AccountStatementReport : NavScreen("account-statement-screen")
    object ProfileScreen : NavScreen("profile-screen")
    object MemberListScreen : NavScreen(
        "member-list-screen".params(
            "memberType", "isTransfer"
        )
    ) {
        fun passArgs(
            memberType: MemberListType,
            isTransfer: Boolean
        ) = "member-list-screen".args(
            "memberType" to memberType.toEncodedString(),
            "isTransfer" to isTransfer.toString(),
        )
    }

}