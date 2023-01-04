package com.a2z.app.data.model.dmt

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class BankDownResponse(
        val status: Int,
        val message: String,
        @SerializedName("bank_string") val bankString : String ? = null,
        @SerializedName("bank_array") val bankList: List<BankDownBank>? = null
) : Parcelable


@Keep
@Parcelize
data class BankDownBank(
        @SerializedName("bank_name") val bankName: String,
        @SerializedName("updated_at") val updatedAt: String,
) : Parcelable