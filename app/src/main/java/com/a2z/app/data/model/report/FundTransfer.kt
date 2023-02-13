package com.a2z.app.data.model.report

import android.os.Parcelable
import com.google.common.collect.Lists
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class FundTransferReportResponse(
    val status: Int,
    val message: String?,
    @SerializedName("result") val reports: List<FundTransferReport>?
) : Parcelable

@Keep
@Parcelize
data class FundTransferReport(
    val date: String?,
    val request_date: String?,
    val update_date: String?,
    val order_id: String?,
    val wallet: String?,
    val user: String?,
    val transfer_to_from: String?,
    val firm_name: String?,
    val ref_id: String?,
    val description: String?,
    val bank_ref: String?,
    val agent_remark: String?,
    val opening_bal: String?,
    val credit_amount: String?,
    val closing_bal: String?,
    val bank_charge: String?,
    val remark: String?,
    val status: String?,
    val status_id: String?,
) : Parcelable