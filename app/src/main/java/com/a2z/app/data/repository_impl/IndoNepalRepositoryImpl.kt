package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.indonepal.INCommonOtpResponse
import com.a2z.app.data.network.IndoNepalService
import com.a2z.app.data.repository.IndoNepalRepository
import com.a2z.app.util.FieldMapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndoNepalRepositoryImpl @Inject constructor(private val service: IndoNepalService) :
    IndoNepalRepository {
    override suspend fun mobileVerification(data : FieldMapData) = service.mobileVerification(data)
    override suspend fun fetchBeneficiary(data: FieldMapData) = service.fetchBeneficiary(data)
    override suspend fun serviceCharge(data: FieldMapData) = service.serviceCharge(data)
    override suspend fun txnOtp(data: FieldMapData) = service.txnOtp(data)
    override suspend fun senderRegistrationOtp(data: FieldMapData) = service.senderRegistrationOtp(data)
    override suspend fun staticData() = service.staticData()
}