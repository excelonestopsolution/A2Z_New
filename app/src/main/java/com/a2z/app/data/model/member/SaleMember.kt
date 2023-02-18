package com.a2z.app.data.model.member

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class RegisterInCompleteUser(

    @SerializedName("mobile") val mobile: String,
    @SerializedName("is_mobile_verified") val isMobileVerified: String,
    @SerializedName("is_email_verified") val isEmailVerified: String,
    @SerializedName("is_pan_verified") val isPanVerified: String,
) : Parcelable


@Keep
@Parcelize
data class RegisterInCompleteUserResponse(
    val status: Int,
    val message: String,
    @SerializedName("next_page") val nextPage: Int? = null,
    @SerializedName("prev_page") val prevPage: Int? = null,
    val members: List<RegisterInCompleteUser>? = null

) : Parcelable


@Keep
@Parcelize
data class RegisterCompleteUser(

    @SerializedName("id") val id: Int,
    @SerializedName("userDetails") val userDetails: String,
    @SerializedName("email") val email: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("balance") val balance: String,
    @SerializedName("parentDetails") val parentDetails: String,
    @SerializedName("status") val status: String,
    @SerializedName("statusId") val statusId: Int,
    @SerializedName("shopName") val shopName: String,
    @SerializedName("is_pan_verified") val panVerified: String,
    @SerializedName("is_aadhaar_kyc") val aadhaarKyc: String,
    @SerializedName("is_document_kyc") val documentKyc: String,
    @SerializedName("aeps_kyc") val aepsKyc: String,
    var kycStatus: Int = 0

) : Parcelable

@Keep
@Parcelize
data class RegisterCompleteUserResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("next_page") val nextPage: Int? = null,
    @SerializedName("prev_page") val prevPage: Int? = null,
    @SerializedName("members") val listData: List<RegisterCompleteUser>? = null

) : Parcelable
