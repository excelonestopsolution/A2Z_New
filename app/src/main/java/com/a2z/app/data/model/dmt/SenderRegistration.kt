package com.a2z.app.data.model.dmt

data class SenderRegistrationResponse(
    val status: Int,
    val message: String,
    val state: String? = null,
)