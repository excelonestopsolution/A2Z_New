package com.a2z.app.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class RegistrationResponse(
    val status: Int,
    val message: String,
    val requestId: String,
    val mobile: String,
    val email_id: String,
    val details: RegistrationDetails
) : Parcelable

@Keep
@Parcelize
data class RegistrationDetails(
    val name: String,
    val email: String,
    val pan_card: String,
    val mobile: String
) : Parcelable