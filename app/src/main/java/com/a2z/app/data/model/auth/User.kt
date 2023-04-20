package com.a2z.app.data.model.auth

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class User(
    @SerializedName("status") val status: Int,
    @SerializedName("changePin") val changePin: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("token") val token: String,
    @SerializedName("name") val name: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("email") val email: String,
    @SerializedName("aeps_kyc") val aepsKyc: Int,
    @SerializedName("user_kyc") val userKyc: Int,
    @SerializedName("is_offline_kyc") val isOfflineKyc: Int,
    @SerializedName("is_pan_verified") val isPanVerified: Int,
    @SerializedName("is_mobile_verified") val isMobileVerified: Int,
    @SerializedName("is_email_verified") val isEmailVerified: Int,
    @SerializedName("user_balance") var userBalance: String,
    @SerializedName("otp_number") val otpNumber: Int,
    @SerializedName("profile_picture") val profilePicture: String,
    @SerializedName("role_id") val roleId: Int,
    @SerializedName("role_title") val roleTitle: String,
    @SerializedName("message") val message: String,
    @SerializedName("shop_name") val shopName: String,
    @SerializedName("address") val address: String,
    @SerializedName("is_user_has_active_settlemnet_account") val isUserHasActiveSettlementAccount: Int,
    @SerializedName("shop_address") val shopAddress: String,
    @SerializedName("joining_date") val joiningDate: String,
    @SerializedName("last_update") val lastUpdate: String,
    @SerializedName("state_id") val stateId: Int,
    @SerializedName("pincode") val pinCode: String,
    @SerializedName("adhaar") val aadhaarNumber: String,
    @SerializedName("pancard_img") val panCardImage: String,
    @SerializedName("adhaar_img") val aadhaarImage: String,
    @SerializedName("adhaar_back_img") val aadhaarBackImage: String,
    @SerializedName("kyc_status") val kycStatus: String,
    @SerializedName("popup") val popup: String,
    @SerializedName("is_aadhaar_kyc") val isAadhaarKyc: Int,
    @SerializedName("is_video_kyc") val isVideoKyc: Int,
    @SerializedName("is_pan_card_activated") val isPanCardActivated: Int,
    @SerializedName("matm") val matm: String,
    @SerializedName("mpos") val mpos: String,
    @SerializedName("indoNepal") val indoNepal: String,
    @SerializedName("aeps_drivers") val aepsDrivers : List<AepsDriver>

    ) : Parcelable