package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.indonepal.INActivationInitialResponse
import com.a2z.app.data.network.IndoNepalService
import com.a2z.app.data.repository.IndoNepalRepository
import com.a2z.app.util.FieldMapData
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndoNepalRepositoryImpl @Inject constructor(private val service: IndoNepalService) :
    IndoNepalRepository {
    override suspend fun mobileVerification(data: FieldMapData) = service.mobileVerification(data)
    override suspend fun fetchBeneficiary(data: FieldMapData) = service.fetchBeneficiary(data)
    override suspend fun serviceCharge(data: FieldMapData) = service.serviceCharge(data)
    override suspend fun txnOtp(data: FieldMapData) = service.txnOtp(data)
    override suspend fun senderRegistrationOtp(data: FieldMapData) =
        service.senderRegistrationOtp(data)

    override suspend fun senderRegistrationOtpVerify(data: FieldMapData) =
        service.senderRegistrationOtpVerify(data)

    override suspend fun staticData() = service.staticData()
    override suspend fun fetchDistrict(stateId: String) = service.fetchDistrict(stateId)
    override suspend fun fetchBranchList(bankName: String) = service.fetchBranchList(bankName)
    override suspend fun addBeneficiary(data: FieldMapData) = service.addBeneficiary(data)
    override suspend fun fetchActivationInitialData() = service.fetchActivationInitialData()
    override suspend fun uploadActivationDoc(documentImage: MultipartBody.Part?) = service.uploadActivationDoc(documentImage)
    override suspend fun activateIndoNepalService(data: FieldMapData) = service.activateIndoNepalService(data)
    override suspend fun submitCourierData(data: FieldMapData)=service.submitCourierData(data)
}