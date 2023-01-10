package com.a2z.app.data.model.utility

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize



@Keep
@Parcelize
data class BillPaymentResponse(
    @SerializedName("statusId") val status : Int,
    @SerializedName("message") val message : String,
    @SerializedName("payid") val payId : String?,
    @SerializedName("txnTime") val dateTime : String?,
    @SerializedName("operator_ref") val operatorRef : String?,
    @SerializedName("status") val statusDescription : String?,
    @SerializedName("billerName") val billName : String?,
    @SerializedName("providerName") val providerName : String?,
    @SerializedName("amount") val amount : String?,
    @SerializedName("type") val type : String?,
    @SerializedName("recordId") val recordId : String?,
    var providerIcon  : Int?,
    var number : String?,
    var numberTitle : String?,
    var serviceName : String?,
    var isTransaction : Boolean = true
) : Parcelable