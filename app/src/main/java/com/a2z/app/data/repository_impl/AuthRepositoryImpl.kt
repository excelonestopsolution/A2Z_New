package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.network.AuthService
import com.a2z.app.data.repository.AuthRepository
import com.a2z.app.util.FieldMapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) :
    AuthRepository {
    override suspend fun login(vararg data: Pair<String, String>) = authService.login(data.toMap())
    override suspend fun verifyLoginOtp(vararg data: Pair<String, String>) =
        authService.verifyLoginOtp(data.toMap())

    override suspend fun changePassword(data: FieldMapData): AppResponse {
       return  authService.changePassword(data)
    }

    override suspend fun resendMPINOtp(data: FieldMapData): AppResponse {
        return authService.resendMPINOtp(data)
    }

    override suspend fun requestChangeMPINOtp(data: FieldMapData): AppResponse {
        return authService.requestChangeMPINOtp(data)
    }

    override suspend fun generateMPIN(data: FieldMapData): AppResponse {
        return authService.generateMPIN(data)
    }
}