package com.a2z.app.data.model

import androidx.annotation.Keep
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class AgreementInitialInfoResponse(
    val status: Int,
    val message: String?,
    val data: AgreementInitialInfo?
) : Parcelable

@Parcelize
@Keep
data class AgreementInitialInfo(
    @SerializedName("agreement_url")
    val agreementUrl: String?,
    val name: String?,
    val email: String?,
    val mobile: String?
) : Parcelable


@Parcelize
@Keep
data class AgreementStartResponse(
    val status: Int,
    val message: String,
    val url: String,
    @SerializedName("order_id")
    val orderId: String

) : Parcelable