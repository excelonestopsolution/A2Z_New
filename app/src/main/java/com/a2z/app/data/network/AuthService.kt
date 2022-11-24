package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.auth.User
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

}
