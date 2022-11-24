package com.a2z.app.data.model.utility

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class RechargeTransactionResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("statusDescription") val statusDescription: String?,
    @SerializedName("payid") val payId: String?,
    @SerializedName("operator_ref") val operatorRef: String?,
    @SerializedName("txnTime") val dateTime: String?,
    @SerializedName("operator") val operatorName: String?,
    @SerializedName("amount") val amount: String?,
    @SerializedName("recordId") val recordId: String?,
    var mobileNumber: String? = null

) : Parcelable