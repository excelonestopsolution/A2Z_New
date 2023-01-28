package com.a2z.app.data.model.fund

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class PaymentGatewayInitiateResponse(
    val status: Int,
    val message: String,
    val data: PaymentGatewayInitiateData?
) : Parcelable


@Keep
@Parcelize
data class PaymentGatewayInitiateData(
    val amount: String,
    @SerializedName("customer_mobile") val mobile: String,
    @SerializedName("ackno") val ackNo: String,
    @SerializedName("key") val key: String,
    @SerializedName("secretKey") val secretKey: String
) : Parcelable