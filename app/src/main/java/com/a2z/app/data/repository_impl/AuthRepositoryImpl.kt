package com.a2z.app.data.repository_impl

import com.a2z.app.data.network.AuthService
import com.a2z.app.data.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) :
    AuthRepository {
    override suspend fun login(vararg data: Pair<String, String>) = authService.login(data.toMap())
    override suspend fun verifyLoginOtp(vararg data: Pair<String, String>) =
        authService.verifyLoginOtp(data.toMap())
}