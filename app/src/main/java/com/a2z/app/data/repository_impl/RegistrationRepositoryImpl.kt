package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.auth.RegistrationResponse
import com.a2z.app.data.model.auth.RegistrationRoleResponse
import com.a2z.app.data.model.auth.RegistrationRoleUserResponse
import com.a2z.app.data.network.RegistrationService
import com.a2z.app.data.repository.RegistrationRepository
import com.a2z.app.util.FieldMapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationRepositoryImpl @Inject constructor(
    private val service: RegistrationService
) : RegistrationRepository {
    override suspend fun postMobileNumber(
        mobileNumber: String,
        isSelf: String
    ): RegistrationResponse {
        return service.postMobileNumber(mobileNumber, isSelf)
    }

    override suspend fun mobileNumberVerify(
        requestId: String,
        otp: String,
        isSelf: String
    ): RegistrationResponse {
        return service.mobileNumberVerify(requestId, otp, isSelf)
    }

    override suspend fun postEmailId(
        email: String,
        requestId: String,
        isSelf: String
    ): RegistrationResponse {
        return service.postEmailId(email, requestId, isSelf)
    }

    override suspend fun verifyEmailId(
        email: String,
        requestId: String,
        isSelf: String
    ): RegistrationResponse {
        return service.verifyEmailId(email, requestId, isSelf)
    }

    override suspend fun postPanNumber(
        requestId: String,
        panNumber: String,
        isSelf: String
    ): RegistrationResponse {
        return service.postPanNumber(requestId, panNumber, isSelf)
    }

    override suspend fun selfRegister(
        requestId: String,
        password: String,
        confirmPassword: String,
        outletName: String,
        outletAddress: String
    ): RegistrationResponse {
        return service.selfRegister(requestId, password, confirmPassword, outletName, outletAddress)
    }

    override suspend fun registerFromDistributor(data: FieldMapData): RegistrationResponse {
        return service.registerFromDistributor(data)
    }

    override suspend fun resendOtp(
        requestId: String,
        type: String,
        isSelf: String
    ): RegistrationResponse {
        return service.resendOtp(requestId,type,isSelf)
    }

    override suspend fun fetchCreateRole()= service.fetchCreateRole()


    override suspend fun fetchMappingUserList(
        url: String,
        data: FieldMapData
    ): RegistrationRoleUserResponse {
        return service.fetchMappingUserList(url,data)
    }

    override suspend fun fetchCompletedUserList(data: FieldMapData): RegistrationResponse {
       return service.fetchCompletedUserList(data)
    }

    override suspend fun fetchInCompletedUserList(data: FieldMapData): RegistrationResponse {
        return service.fetchInCompletedUserList(data)
    }
}