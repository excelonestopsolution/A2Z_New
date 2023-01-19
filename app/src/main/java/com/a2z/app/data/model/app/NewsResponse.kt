package com.a2z.app.data.model.app

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class NewsResponse(
        val status : Int,
        val retailerNews : String,
        val distributorNews : String
) : Parcelable