package com.a2z.app.data.model.fund

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PaymentReturnDetailResponse(
    val status: Int,
    val message: String,
    val data: PaymentReturnDetail?,
) : Parcelable

@Keep
@Parcelize
data class PaymentReturnDetail(
    val id: String?,
    val prefix: String?,
    val name: String?,
    val mobile: String?,
    val shopName: String?,
    val encReceiverId: String,
) : Parcelable