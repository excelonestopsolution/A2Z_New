package com.a2z.app.data.model.indonepal

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class INActivationInitialResponse(
    val status : Int,
    val message : String,
    val data : INActivationInitialData
) : Parcelable


@Keep
@Parcelize
data class INActivationInitialData(
    val user_id: String?,
    val service_status: String?,
    val document_status: String?,
    val document_image: String?,
    val courier_name: String?,
    val courier_date: String?,
    val courier_status: String?,
    val status_id: String?,
    val remark: String?,
) : Parcelable