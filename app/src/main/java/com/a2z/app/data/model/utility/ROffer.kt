package com.a2z.app.data.model.utility

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ROfferResponse(
    val status : String,
    @SerializedName("Response")
    val offers : List<RechargePlan>
) : Parcelable



@Keep
@Parcelize
data class RechargePlanResponse(
    var status : Int,
    var message : String,
    var offerTab : List<RechargePlanTab>
) : Parcelable

@Keep
@Parcelize
data class RechargePlanTab(
    val planName : String,
    val rechargePlanList : List<RechargePlan>,
) : Parcelable

@Keep
@Parcelize
data class RechargePlan(
   @SerializedName("rs", alternate = ["price"]) val rs : String?,
   @SerializedName("desc", alternate = ["offer"]) val desc : String?,
    val validity : String?,
    val remark : String?,
    val discontinued : String?,

) : Parcelable