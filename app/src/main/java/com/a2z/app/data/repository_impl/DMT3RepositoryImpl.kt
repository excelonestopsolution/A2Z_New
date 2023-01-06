package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.MoneySenderResponse
import com.a2z.app.data.model.dmt.SenderRegistrationResponse
import com.a2z.app.data.network.DMT3Service
import com.a2z.app.data.repository.DMT3Repository
import com.a2z.app.util.FieldMapData
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DMT3RepositoryImpl @Inject constructor(private val service: DMT3Service)  : DMT3Repository{

    override suspend fun searchMobileNumberDmtThree(data: FieldMapData): MoneySenderResponse {
        return service.searchMobileNumberDmtThree(data)
    }

    override suspend fun registerSender(data: FieldMapData): SenderRegistrationResponse {
        return service.registerSender(data)
    }

    override suspend fun verifySender(data: FieldMapData): AppResponse {
        return service.verifySender(data)
    }

    override suspend fun resendSenderRegistrationOtp(data: FieldMapData): SenderRegistrationResponse {
        return service.resendSenderRegistrationOtp(data)
    }

}