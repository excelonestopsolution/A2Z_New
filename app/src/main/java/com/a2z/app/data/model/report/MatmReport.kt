package com.a2z.app.data.model.report

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class MatmReportResponse(
    val status : Int,
    val message : String,
    val data : List<MatmReport>?
) : Parcelable

@Parcelize
@Keep
data class MatmReport(
    val status: Int?,
    val status_desc: String?,
    val message: String?,
    val record_id: String?,
    val service_name: String?,
    val customer_number: String?,
    val txn_id: String?,
    val order_id: String?,
    val transaction_type: String?,
    val card_type: String?,
    val credit_debit_card_type: String?,
    val available_amount: String?,
    val transaction_amount: String?,
    val is_pin_verified: String?,
    val transaction_mode: String?,
    val bank_ref: String?,
    val txn_time: String?,
    val is_print: Boolean?,
    val is_check_status: Boolean?,
    val is_complain: Boolean?,
    val tds: String?,
    val credit_charge: String?,
    val debit_charge: String?,
    val gst: String?,
) : Parcelable