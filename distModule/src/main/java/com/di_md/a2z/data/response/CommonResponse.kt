package com.di_md.a2z.data.response

import androidx.annotation.Keep
import com.di_md.a2z.util.AppConstants
import com.google.gson.annotations.SerializedName

@Keep
data class CommonResponse(
        val status : Int=0,
        val code : Int=0,
        val message : String="",
        val verifyId : String = "",
        val state : String = ""
)



@Keep
data class AadhaarKycResponse(
        val status : Int,
        val message : String,
        @SerializedName("request_id")
        val requestId : String = AppConstants.EMPTY
)