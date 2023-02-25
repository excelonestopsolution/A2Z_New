package com.a2z.app.data.model.indonepal

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class INTransferResponse(
    val status : Int,
    val message : String,
    val data : INTransferData?,
) : Parcelable


@Keep
@Parcelize
data class INTransferData(
    val report_id : String?,
    var status : Int,
    var message : String?,
    val status_desc : String?,
    val txn_time : String?,
    val slip_type : String?,
    val amount : String?,
    val outlet_name : String?,
    val outlet_address : String?,
    val outlet_number : String?,
    val name : String?,
    val bank_name : String?,
    val number : String?,
    val provider : String?,
    val service_name : String?,
    val sender_name : String?,
    val api_id : String?,
    val sender_number : String?,
    val bank_ref : String?,
    val txn_id : String?,
    val debit_charge : String?,
    var isTransaction : Boolean = true
) : Parcelable