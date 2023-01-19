package com.a2z.app.data.model.dmt

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DmtCommission(
    @SerializedName("txnAmount") val txnAmount: String? = null,
    @SerializedName("total") val total: String? = null,
    @SerializedName("charge") val charge: String? = null,
    val serialNumber: Int
) : Parcelable


@Keep
@Parcelize
data class DmtCommissionResponse(

    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("totalAmount") var totalAmount: String? = null,
    @SerializedName("result") var commissions: ArrayList<DmtCommission>? = null

) : Parcelable