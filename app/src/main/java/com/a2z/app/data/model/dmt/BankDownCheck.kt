package com.a2z.app.data.model.dmt

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class BankDownCheckResponse(
         val status: Int,
         val message: String,
        @SerializedName("data")  val bankDownCheck: BankDownCheck? = null
) : Parcelable

@Parcelize
@Keep
data class BankDownCheck(
        @SerializedName("is_txn_allowed") val isTxnAllowed: Boolean? = null,
        @SerializedName("is_imps_allowed") val isImpsAllowed: Boolean? = null,
        @SerializedName("message") val message: String? = null,
        @SerializedName("ifsc") val ifsc: String? = null,
        @SerializedName("account_digit") val accountDigit: Int? = null
) : Parcelable