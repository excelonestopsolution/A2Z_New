package com.a2z.app.data.model.dmt


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class BankListResponse(

    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: ArrayList<Bank>? = null

) : Parcelable


@Parcelize
@Keep
data class Bank(

    @SerializedName("bank_name") var bankName: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("ifsc") var ifsc: String? = null,
    @SerializedName("account_digit") var accountDigit: Int? = null,

) : Parcelable