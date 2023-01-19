package com.a2z.app.data.model.dmt

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class SenderRegistrationResponse(
    val status: Int,
    val message: String,
    val state: String? = null,
) : Parcelable