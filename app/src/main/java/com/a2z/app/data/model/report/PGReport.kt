package com.a2z.app.data.model.report

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class PGReportResponse(
    val status: Int,
    val count: Int,
    val page: String,
    @SerializedName("data") val reports: List<PGReport>?
) : Parcelable


@Parcelize
@Keep
data class PGReport(
    val orderId: String?,
    val txn_time: String?,
    val status: Int,
    val status_desc: String?,
    val card_number: String?,
    val Card_type: String?,
    val customer_mobile: String?,
    val amount: String?,
    val credit: String?,
    val debit: String?,
    val gst: String?,
    val report_id: String?,
    val operator_id: String?,
    val bank_ref: String?,
    val message: String?,
) : Parcelable