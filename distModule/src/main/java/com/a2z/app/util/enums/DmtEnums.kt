package com.a2z.app.util.enums

import androidx.annotation.Keep


@Keep
enum class DmtType{
    WALLET_ONE, WALLET_TWO, WALLET_THREE,DMT_THREE,UPI,UPI_TWO
}

@Keep
enum class ReportTypeName{
    LADGER_REPORT, AEPS_REPORT, MATAM_REPORT,PAYMENTGATEWAY_REPORT
}

@Keep
enum class DmtSenderRegisterType{
    REGISTER,VERIFY_AND_UPDATE
}