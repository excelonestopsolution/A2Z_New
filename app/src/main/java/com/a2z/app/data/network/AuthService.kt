package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.auth.ForgotPassword
import com.a2z.app.data.model.auth.User
import com.a2z.app.util.FieldMapData
import retrofit2.Response
import retrofit2.http.Field
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
    @POST("resend-device-otp")
    suspend fun verifyLoginResendOtp(@FieldMap data: FieldMapData): AppResponse

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

   @FormUrlEncoded
    @POST("forget-password")
    suspend fun forgotPasswordRequestOTP(@FieldMap data: FieldMapData): ForgotPassword


    @FormUrlEncoded
    @POST("resend-device-otp")
    suspend fun forgotPasswordResendOTP(@FieldMap data: FieldMapData): AppResponse

  @FormUrlEncoded
    @POST("store-password")
    suspend fun resetForgotPassword(@FieldMap data: FieldMapData): AppResponse

    @FormUrlEncoded
    @POST("login-id/forget/send-otp")
    suspend fun forgotLoginId(@FieldMap data: FieldMapData): AppResponse


    @FormUrlEncoded
    @POST("login-id/forget/verify-otp")
    suspend fun forgotLoginIdVerify(@FieldMap data: FieldMapData): AppResponse


}
