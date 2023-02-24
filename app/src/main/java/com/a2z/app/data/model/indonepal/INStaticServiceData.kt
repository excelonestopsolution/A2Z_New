package com.a2z.app.data.model.indonepal

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class INStaticServiceData(
    val status : Int,
    val warning : List<String>,
    val note : List<String>,
    val image : INDownloadForm

) : Parcelable

@Keep
@Parcelize
data class INDownloadForm(
    val form : String,
    val sampleForm : String,
) : Parcelable
