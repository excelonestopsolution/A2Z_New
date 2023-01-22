package com.a2z.app.data.model.report

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


@Parcelize
@Keep
data class FundReportResponse(

    val status : Int,
    val message : String,
    val reports : List<FundReport>?
) : Parcelable

@Parcelize
@Keep
data class FundReport(
    val id : String?,
    val created_at : String?,
    val username : String?,
    val deposit_date : String?,
    val bank_name : String?,
    val account_number : String?,
    val wallet_amount : String?,
    val request_for : String?,
    val bank_ref : String?,
    val payment_mode : String?,
    val branch_name : String?,
    val request_remark : String?,
    val approval_remark : String?,
    val update_remark : String?,
    val status : String?,
    val status_id : Int?,
) : Parcelable