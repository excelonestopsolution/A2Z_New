package com.a2z.app.data.model.aeps

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class AepsLimitResponse(
    val status : Int,
    val data : AepsLimit?
) : Parcelable


@Keep
@Parcelize
data class AepsLimit(
    val iin : String,
    val daily_limit_count : String,
    val daily_txn_amount : String,
    val monthly_txn_count : String,
    val monthly_txn_max_amount : String,
    val is_aadhaar_pay_serice_available : String
) : Parcelable