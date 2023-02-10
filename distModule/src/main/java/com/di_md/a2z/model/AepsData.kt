package com.di_md.a2z.model

import androidx.annotation.Keep


@Keep
data class AepsData(
        val id : String = "",
        val transactionTypeId : String = "",
        val txnid : String = "",
        val user_id : String = "",
        val number : String = "",
        val status_id : String = "",
        val status : String = "",
        val fail_msg : String = "",
        val opening_balance : String = "",
        val amount : String = "",
        val tds : String = "",
        val credit_charge : String = "",
        val debit_charge : String = "",
        val total_balance : String = "",
        val type : String = "",
        val txn_type : String = "",
        val bankName : String = "",
        val customer_number : String = "",
        val ackno : String = "",
        val bank_ref : String = "",
        val apiName : String = "",
        val mode : String = "",
        val report_id : String = "",
        val ip_address : String = "",
        val created_at : String = "",
        val orderId : String = "",
        var isHide : Boolean = true,
        val is_check_status : Boolean = true,
        val is_print : Boolean = true,
        val is_complain : Boolean = true,
)