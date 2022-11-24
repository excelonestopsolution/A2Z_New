package com.a2z.app.data.model.utility

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RechargeDthInfoResponse(
    val status: Int,
    val message: String,
    @SerializedName("planInfo") val info : RechargeDthInfo
) : Parcelable

@Keep
@Parcelize
data class RechargeDthInfo(
  @SerializedName("Balance")  val balance : String?,
  @SerializedName("customerName")  val customerName : String?,
  @SerializedName("NextRechargeDate")  val nextRechargeDate : String?,
  @SerializedName("status")  val status : String?,
  @SerializedName("MonthlyRecharge")  val monthlyRecharge : String?,
) : Parcelable