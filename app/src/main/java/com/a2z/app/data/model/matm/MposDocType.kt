package com.a2z.app.data.model.matm

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MposDocTypeResponse(
    val status: Int,
    val businessLegalityProofType: List<String>?,
    val businessAddressProofType: List<String>?,
) : Parcelable