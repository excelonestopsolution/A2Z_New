package com.a2z.app.data.model.auth

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ForgotPassword(
    val status : Int,
    val message : String,
    val token : String?
) : Parcelable