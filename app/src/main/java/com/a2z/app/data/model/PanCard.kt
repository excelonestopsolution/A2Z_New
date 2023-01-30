package com.a2z.app.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PanCardServiceNoteResponse(
    val status: Int,
    val note: List<String>,
    val image: PanCardImage,
    val serviceActivationMessage: List<String>,
) : Parcelable

@Parcelize
@Keep
data class PanCardImage(
    val utiImage: String,
    val panImage: String,
) : Parcelable

@Parcelize
@Keep
data class PanAutoLoginResponse(
    val status: Int,
    val message: String,
    val url: String?
) : Parcelable