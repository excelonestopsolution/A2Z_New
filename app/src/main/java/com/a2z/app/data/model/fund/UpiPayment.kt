package com.a2z.app.data.model.fund

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UpiPaymentInitiateResponse(
    val status : Int,
    val message : String,
    val refId : String?
) : Parcelable