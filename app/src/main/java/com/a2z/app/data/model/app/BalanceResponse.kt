package com.a2z.app.data.model.app

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class BalanceResponse(
    val status : Int,
    val message : String,
    @SerializedName("details")
    val balance : Balance

) : Parcelable

@Keep
@Parcelize
data class Balance(
    @SerializedName("user_balance")
    val userBalance : String
) : Parcelable