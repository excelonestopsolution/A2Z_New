package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.auth.User

interface AuthRepository {
    suspend fun login(vararg  data : Pair<String,String>): User
    suspend fun verifyLoginOtp(vararg data : Pair<String,String>): AppResponse
}