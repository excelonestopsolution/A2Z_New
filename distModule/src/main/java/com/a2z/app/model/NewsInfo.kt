package com.a2z.app.model

import androidx.annotation.Keep

@Keep
data class NewsInfo(
        val status : Int,
        val retailerNews : String,
        val distributorNews : String
)