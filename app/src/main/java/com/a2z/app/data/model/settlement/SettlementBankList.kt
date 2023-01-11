package com.a2z.app.data.model.settlement

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class SettlementBankListResponse(
    val status: Int,
    val message: String,
    val data: SettlementBankResponseData?
) : Parcelable

@Parcelize
@Keep
data class SettlementBankResponseData(
    @SerializedName("bank_list") val bankList: ArrayList<SettlementBank>,
    @SerializedName("information") val information: SettlementBankTextInfo,

    ) : Parcelable


@Parcelize
@Keep
data class SettlementBankTextInfo(
    @SerializedName("line_one") val lineOne: SettlementBankTextInfoLineOne,
    @SerializedName("line_two") val lineTwo: String,
) : Parcelable

@Parcelize
@Keep
data class SettlementBankTextInfoLineOne(
    @SerializedName("string_first") val first: String?,
    @SerializedName("user_name") val username: String?,
    @SerializedName("jointer") val join: String?,
    @SerializedName("shop_name") val shopName: String?,
) : Parcelable

@Parcelize
@Keep
data class SettlementBank(
    @SerializedName("id") val id: String?,
    @SerializedName("bank_name") val bankName: String?,
    @SerializedName("ifsc") val ifscCode: String?
) : Parcelable