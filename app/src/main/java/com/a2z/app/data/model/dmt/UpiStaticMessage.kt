package com.a2z.app.data.model.dmt

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class UpiStaticMessage(
    val status : Int,
    val declarationMessage : List<String>?,
    val warningMessage : List<String>?,
    val chargeDetails : UpiVerifyCharge
) : Parcelable

@Keep
@Parcelize
data class UpiVerifyCharge(
    val txnAmount : String,
    val txnCharge : String,
    val totalAmount : String,
) : Parcelable