package com.a2z.app.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class FlightHotelRedirectUrlResponse(
    val status : Int,
    val message : String?,
    val url : String?
) : Parcelable