package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.auth.User
import com.a2z.app.util.FieldMapData

interface AuthRepository {
    suspend fun login(vararg  data : Pair<String,String>): User
    suspend fun verifyLoginOtp(vararg data : Pair<String,String>): AppResponse
    suspend fun changePassword(data : FieldMapData): AppResponse
    suspend fun resendMPINOtp(data : FieldMapData): AppResponse
    suspend fun requestChangeMPINOtp(data : FieldMapData): AppResponse
    suspend fun generateMPIN(data : FieldMapData): AppResponse
}