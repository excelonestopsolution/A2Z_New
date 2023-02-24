package com.a2z.app.data.model.indonepal

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class INRequestStatusResponse(
    val status : Int,
    val message : String,
    val data : INRequestStatus?
) : Parcelable

@Keep
@Parcelize
data class INRequestStatus(
    val user_id : String?,
    val service_status : String?,
    val document_status : String?,
    val document_image : String?,
    val courier_name : String?,
    val courier_date : String?,
    val courier_status : String?,
    val status_id : String?,
    val remark : String?,
) : Parcelable