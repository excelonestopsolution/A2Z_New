package com.a2z.app.data.model.indonepal

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class INServiceChargeResponse(
    val status: Int,
    val message: String,
    val data: INServiceCharge?
) : Parcelable


@Keep
@Parcelize
data class INServiceCharge(
    val collectionAmount: String?,
    val collectionCurrency: String?,
    val serviceCharge: String?,
    val transferAmount: String?,
    val exchangeRate: String?,
    val payoutAmount: String?,
    val payoutCurrency: String?,
) : Parcelable
