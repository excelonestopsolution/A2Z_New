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