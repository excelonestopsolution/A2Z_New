package com.a2z.app.data.model.report

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ComplainTypeListResponse(
    val status: Int,
    val message: String,
    val data: List<ComplainType>? = null
) : Parcelable

@Keep
@Parcelize
data class ComplainType(
    val id: String,
    val name: String
) : Parcelable

@Keep
@Parcelize
data class ComplainListResponse(
    val status : Int,
    val message : String?,
    val count : Int,
    val page : String,
    val complains : List<Complaint>?
) : Parcelable

@Parcelize
data class Complaint(
    val id : String,
    val user_id : String,
    val created_at : String,
    val issue_type : String,
    val txn_id : String,
    val looking_by : String,
    val approved_by : String,
    val status_id : String,
    val status : String,
    val approved_date : String,
    val remark : String,
    val current_status_remark : String,
) : Parcelable