package com.a2z.app.data.model.report

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class DTReportResponse(
    val status : Int,
    val message : String,
    val reports : List<DTReport>?
) : Parcelable


@Parcelize
@Keep
data class DTReport(
    val created_at: String?,
    val order_id: String?,
    val wallet: String?,
    val number: String?,
    val username: String?,
    val user_id: String?,
    val transfer_to_from: String?,
    val firm_name: String?,
    val ref_id: String?,
    val description: String?,
    val opening_bal: String?,
    val credit_amount: String?,
    val closing_bal: String?,
    val bank_charge: String?,
    val remark: String?,
    val status: String?,
) : Parcelable