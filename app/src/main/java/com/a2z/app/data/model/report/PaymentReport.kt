package com.a2z.app.data.model.report

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class PaymentReportResponse(
    val status : Int,
    val message : String?,
    @SerializedName("result")val reports : List<PaymentReport>?
) : Parcelable


@Keep
@Parcelize
data class PaymentReport(
    val date : String?,
    val id : String?,
    val request_to : String?,
    val bank_name : String?,
    val mode : String?,
    val branch_code : String?,
    val deposit_date : String?,
    val amount : String?,
    val deposit_slip : String?,
    val customer_remark : String?,
    val ref_id : String?,
    val updated_remark : String?,
    val remark : String?,
    val status : String?,
    val status_id : String?,
) : Parcelable