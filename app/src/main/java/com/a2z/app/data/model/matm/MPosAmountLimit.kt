package com.a2z.app.data.model.matm

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MaPosAmountLimitResponse(
    val status: Int,
    val message: String,
    val minAmount: String? = null,
    val maxAmount: String? = null

) : Parcelable