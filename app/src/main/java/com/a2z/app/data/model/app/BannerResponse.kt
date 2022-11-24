package com.a2z.app.data.model.app

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Slider(
    val image: String
) : Parcelable

@Keep
@Parcelize
data class BannerResponse(
    val status: Int,
    val message: String,
    @SerializedName("data") val sliders: List<Slider>?
) : Parcelable