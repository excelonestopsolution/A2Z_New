package com.di_md.a2z.model.pg

import androidx.annotation.Keep


@Keep
data class RzpayOrderAckDataModel(
    val `data`: PGData,
    val message: String,
    val status: Int
)