package com.a2z.app.data.model.matm

data class MposDocTypeResponse(
    val status: Int,
    val businessLegalityProofType: List<String>?,
    val businessAddressProofType: List<String>?,
)