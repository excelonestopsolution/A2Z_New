package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.auth.User
import com.a2z.app.util.FieldMapData
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST("agentLogin")
    suspend fun login(@FieldMap data: Map<String, String>): User


    @FormUrlEncoded
    @POST("verify-device")
    suspend fun verifyLoginOtp(@FieldMap data: Map<String, String>): AppResponse

    @FormUrlEncoded
    @POST("change-password")
    suspend fun changePassword(@FieldMap data: FieldMapData): AppResponse

    @FormUrlEncoded
    @POST("resend-device-otp")
    suspend fun resendMPINOtp(@FieldMap data: FieldMapData): AppResponse


    @FormUrlEncoded
    @POST("generate-new-pin")
    suspend fun requestChangeMPINOtp(@FieldMap data: FieldMapData): AppResponse


    @FormUrlEncoded
    @POST("get-generated-pin")
    suspend fun generateMPIN(@FieldMap data: FieldMapData): AppResponse

}
