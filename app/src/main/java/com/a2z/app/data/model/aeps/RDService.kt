package com.a2z.app.data.model.aeps

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RDService(
    val deviceName : String,
    val packageName : String
) : Parcelable