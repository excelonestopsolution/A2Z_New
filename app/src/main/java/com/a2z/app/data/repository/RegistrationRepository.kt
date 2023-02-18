package com.a2z.app.data.repository

import com.a2z.app.data.model.auth.RegistrationResponse
import com.a2z.app.data.model.auth.RegistrationRoleResponse
import com.a2z.app.data.model.auth.RegistrationRoleUserResponse
import com.a2z.app.util.FieldMapData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url

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

    suspend fun fetchMappingUserList(
        url: String,
        data: FieldMapData
    ): RegistrationRoleUserResponse


    suspend fun fetchCompletedUserList(
        data: FieldMapData
    ): RegistrationResponse


    suspend fun fetchInCompletedUserList(
        data: FieldMapData
    ): RegistrationResponse


    suspend fun fetchCreateRole(): RegistrationRoleResponse


}