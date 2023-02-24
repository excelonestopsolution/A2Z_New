package com.a2z.app.data.model.utility

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Keep
data class BillFetchInfoResponse(
    val status : Int,
    val message : String?,
    val serviceName : String?,
    val isAmountEditable : Int?,
   @SerializedName("billInfo") val info : BillFetchInfo?,
) : Parcelable

@Parcelize
@Keep
data class BillFetchInfo(
    @SerializedName("context") val context : String?,
    @SerializedName("CustomerName") val customerName : String?,
    @SerializedName("customerRefId") val customerRef : String?,
    @SerializedName("billPeriod") val billPeriod : String?,
    @SerializedName("Billamount", alternate = ["billAmount"]) val billAmount : String,
    @SerializedName("Duedate")val dueDate : String,
    @SerializedName("BillNumber", alternate = ["billNumber"])val billNumber : String,
    @SerializedName("Billdate")val Billdate : String,
) : Parcelable
