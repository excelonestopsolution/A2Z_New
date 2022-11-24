package com.a2z.app.data.model.utility

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RechargeOfferResponse(
    val status : String,
    @SerializedName("Response")
    val offers : List<RechargeOffer>
) : Parcelable

@Keep
@Parcelize
data class RechargeOffer(
    val price : String,
    val offer : String
) : Parcelable