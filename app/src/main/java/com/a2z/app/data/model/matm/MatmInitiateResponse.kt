package com.a2z.app.data.model.matm

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MatmInitiateResponse(
    val status : Int,
    val message : String,
    val data : MatmInitiate?
) : Parcelable