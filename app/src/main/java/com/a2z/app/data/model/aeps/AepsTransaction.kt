package com.a2z.app.data.model.aeps

import android.os.Parcelable
import androidx.annotation.Keep
import com.a2z.app.data.model.dmt.MiniStatement
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AepsTransaction(
    val status: Int,
    val message: String,
    val record_id: String?,
    val status_desc: String?,
    val service_name: String?,
    val txn_id: String?,
    val order_id: String?,
    val transaction_type: String?,
    val aadhaar_number: String?,
    val available_amount: String?,
    val transaction_amount: String?,
    val bank_ref: String?,
    val txn_time: String?,
    val bank_name: String?,
    val customer_number: String?,
    val shop_name: String?,
    val retailer_number: String?,
    val pay_type: String?,
    val statement: ArrayList<MiniStatement>?,
    var isTransaction: Boolean = true,
    var isLedgerReport : Boolean = false
) : Parcelable

@Keep
@Parcelize
data class AepsStatement(
    val date : String?,
    val narration : String?,
    val txnType : String?,
    val amount : String?,
) : Parcelable