package com.a2z.app.data.model.indonepal

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


@Parcelize
@Keep
data class INKycRedirect(
    val status : Int,
    val message : String,
    val url : String?
) : Parcelable