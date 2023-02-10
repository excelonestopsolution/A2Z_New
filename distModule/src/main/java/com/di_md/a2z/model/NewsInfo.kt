package com.di_md.a2z.model

import androidx.annotation.Keep


@Keep
data class NewsInfo(
        val status : Int,
        val retailerNews : String,
        val distributorNews : String
)