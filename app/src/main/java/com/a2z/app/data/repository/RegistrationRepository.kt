package com.a2z.app.data.repository

import com.a2z.app.data.model.RegistrationResponse
import com.a2z.app.util.FieldMapData
import retrofit2.http.*

interface RegistrationRepository {

    suspend fun postMobileNumber(
        mobileNumber: String,
        isSelf: String,
    ): RegistrationResponse

    suspend fun mobileNumberVerify(
        requestId: String,
        otp: String,
        isSelf: String,
    ): RegistrationResponse


    suspend fun postEmailId(
        email: String,
        requestId: String,
        isSelf: String,
    ): RegistrationResponse


    suspend fun verifyEmailId(
        email: String,
        requestId: String,
        isSelf: String,
    ): RegistrationResponse


    suspend fun postPanNumber(
        requestId: String,
        panNumber: String,
        isSelf: String,
    ): RegistrationResponse


    suspend fun selfRegister(
        requestId: String,
        password: String,
        confirmPassword: String,
        outletName: String,
        outletAddress: String,
    ): RegistrationResponse


    suspend fun registerFromDistributor(data: FieldMapData): RegistrationResponse


    suspend fun resendOtp(
        requestId: String,
        type: String,
        isSelf: String,
    ): RegistrationResponse


    suspend fun fetchCreateRole(): RegistrationResponse


    suspend fun fetchMappingUserList(
        url: String,
        data: FieldMapData
    ): RegistrationResponse


    suspend fun fetchCompletedUserList(
        data: FieldMapData
    ): RegistrationResponse


    suspend fun fetchInCompletedUserList(
        data: FieldMapData
    ): RegistrationResponse

}