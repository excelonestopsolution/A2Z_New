package com.a2z.app.ui.screen.fund.method

import com.a2z.app.R
import com.a2z.app.data.model.fund.FundMethod

class FundMethodUtil {

    val methodList = listOf(
        FundMethod(
            type = FundMethodType.UPI,
            name = "UPI\n",
            drawable = R.drawable.fund_upi
        ),

        FundMethod(
            type = FundMethodType.BANK_TRANSFER,
            name = "Bank Transfer\n(Online)",
            drawable = R.drawable.fund_bank_transfer
        ),
        FundMethod(
            type = FundMethodType.CASH_IN_CDM,
            name = "Cash In CDM\n(Machine)",
            drawable = R.drawable.fund_cash_in_cdm
        ),
        FundMethod(
            type = FundMethodType.CASH_DEPOSIT,
            name = "Cash Deposit\n",
            drawable = R.drawable.fund_cash_deposit
        ),
        FundMethod(
            type = FundMethodType.PAYMENT_GATEWAY,
            name = "Payment\nGateway",
            drawable = R.drawable.fund_payment_gateway
        ),
        FundMethod(
            type = FundMethodType.CASH_COLLECT,
            name = "Cash\nCollect",
            drawable = R.drawable.fun_cash_collect
        )
    )

}
enum class FundMethodType {
    UPI, BANK_TRANSFER, CASH_IN_CDM, CASH_DEPOSIT, PAYMENT_GATEWAY, CASH_COLLECT
}