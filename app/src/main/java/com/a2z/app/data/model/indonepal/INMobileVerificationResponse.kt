package com.a2z.app.data.model.indonepal

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class INMobileVerificationResponse(
    val status : Int,
    val message : String,
    @SerializedName("verifie")
    val verify : Int,
    val data : INSender?
) : Parcelable


@Keep
@Parcelize
data class INSender(
    val mobile : String?,
    val customerId : String?,
    val employer : String?,
    val nationality : String?,
    val incomeSource : String?,
    val status : String?,
    val approveStatus : List<String>?,
    val approveComment : List<String>?,
    val dob : String?,
    val gender : String?,
    val name : String?,
    val address : String?,
    val city : String?,
    val state : String?,
    val district : String?,
    val ekyc_status : String?,
    val onboarding_status : String?,
    val idType : String?,
    val idNumber : String?,
    val dayLimit : String?,
    val monthLimit : String?,
    val yearLimit : String?,
    val availableDayLimit : String?,
    val availableMonthLimit : String?,
    val availableYearLimit : String?,
    val totalDayLimit : String?,
    val totalMonthLimit : String?,
    val totalYearLimit : String?,
) : Parcelable