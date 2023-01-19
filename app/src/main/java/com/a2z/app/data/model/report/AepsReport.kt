package com.a2z.app.data.model.report

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AepsReportResponse(
    val status : Int,
    val message : String,
    val data : List<AepsReport>?
) : Parcelable

@Keep
@Parcelize
data class AepsReport(
    val id : String?,
    val txnid : String?,
    val user_id : String?,
    val number : String?,
    val status_id : Int?,
    val status : String?,
    val fail_msg : String?,
    val opening_balance : String?,
    val amount : String?,
    val tds : String?,
    val credit_charge : String?,
    val debit_charge : String?,
    val total_balance : String?,
    val txn_type : String?,
    val bank_name : String?,
    val customer_number : String?,
    val ackno : String?,
    val bank_ref : String?,
    val api_name : String?,
    val mode : String?,
    val order_id : String?,
    val report_id : String?,
    val ip_address : String?,
    val created_at : String?,
    val transaction_type_id : String?,
    val is_check_status : Boolean?,
    val is_print : Boolean?,
    val is_complain : Boolean?,
) : Parcelable