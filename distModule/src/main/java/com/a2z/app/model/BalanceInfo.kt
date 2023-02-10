package com.a2z.app.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BalanceResponse(
        val status : Int,
        val message : String,
        @SerializedName("details")
        val balance : Balance

)

@Keep
data class Balance(
        @SerializedName("user_balance")
        val userBalance : String
)