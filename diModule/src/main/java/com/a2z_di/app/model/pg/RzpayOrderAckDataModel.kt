package com.a2z_di.app.model.pg

import androidx.annotation.Keep

@Keep
data class RzpayOrderAckDataModel(
    val `data`: PGData,
    val message: String,
    val status: Int
)