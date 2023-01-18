package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.auth.ForgotPassword
import com.a2z.app.data.model.auth.User
import com.a2z.app.util.FieldMapData
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthRepository {
    suspend fun login(vararg data: Pair<String, String>): User
    suspend fun verifyLoginOtp(vararg data: Pair<String, String>): AppResponse
    suspend fun verifyLoginResendOtp(data : FieldMapData): AppResponse
    suspend fun changePassword(data: FieldMapData): AppResponse
    suspend fun resendMPINOtp(data: FieldMapData): AppResponse
    suspend fun requestChangeMPINOtp(data: FieldMapData): AppResponse
    suspend fun generateMPIN(data: FieldMapData): AppResponse
    suspend fun forgotPasswordRequestOTP(data: FieldMapData): ForgotPassword
    suspend fun forgotPasswordResendOTP(data: FieldMapData): AppResponse
    suspend fun resetForgotPassword(data: FieldMapData): AppResponse

    suspend fun forgotLoginId(data: FieldMapData): AppResponse
    suspend fun forgotLoginIdVerify(data: FieldMapData): AppResponse

}