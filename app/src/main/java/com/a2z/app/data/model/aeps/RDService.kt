package com.a2z.app.data.model.aeps

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RDService(
    val deviceName : String,
    val packageName : String
) : Parcelable