package com.a2z_di.app.util.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
enum class FundRequestType : Parcelable {
    UPI_PAYMENT, PAYMENT_GATEWAY, BANK_TRANSFER, CASH_COLLECT, CASH_DEPOSIT_COUNTER, CASH_DEPOSIT_MACHINE
}