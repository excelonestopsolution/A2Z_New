package com.a2z.app.data.model.dmt

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class UpiVerifyPayment(
    val operator_id: String?,
    val record_id: String?,
    val txn_time: String?,
    val account_number: String?,
    val bank_name: String?,
    val status: Int,
    val amount: String?,
    val bene_name: String?,
    val message: String,
    val status_desc: String?,
) : Parcelable