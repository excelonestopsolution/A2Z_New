package com.a2z.app.model.pg

import androidx.annotation.Keep
@Keep
data class PGData(
    val ackno: String,
    val amount: String,
    val customer_mobile: String
)