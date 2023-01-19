package com.a2z.app.data.model.kyc

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AepsKycDetailResponse(
    val status: Int,
    val message: String,
    val details : AepsKycDetail?
) : Parcelable

@Keep
@Parcelize
data class AepsKycDetail(
    val agent_name: String?,
    val pan_number: String?,
    val aadhaar_number: String?,
    val merchant_login_id: String?,
) : Parcelable