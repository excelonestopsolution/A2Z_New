package com.a2z.app.data.model.kyc

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AadhaarKycResponse(
    val status: Int,
    val message: String,
    @SerializedName("request_id")
    val requestId: String?
) : Parcelable