package com.a2z_di.app.model

import androidx.annotation.Keep

@Keep
data class NewsInfo(
        val status : Int,
        val retailerNews : String,
        val distributorNews : String
)