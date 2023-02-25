package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.indonepal.*
import com.a2z.app.util.FieldMapData
import okhttp3.MultipartBody

interface IndoNepalRepository {
    suspend fun mobileVerification(data : FieldMapData): INMobileVerificationResponse
    suspend fun fetchBeneficiary(data : FieldMapData): INBeneficiaryResponse
    suspend fun serviceCharge(data : FieldMapData): INServiceChargeResponse
    suspend fun txnOtp(data : FieldMapData): INCommonOtpResponse
    suspend fun senderRegistrationOtp(data : FieldMapData): INCommonOtpResponse
    suspend fun senderRegistrationOtpVerify(data : FieldMapData): AppResponse
    suspend fun staticData() : INStaticData

    suspend fun fetchDistrict(stateId : String) : INDistrictResponse
    suspend fun fetchBranchList(bankName : String) : INBranchResponse
    suspend fun addBeneficiary(data : FieldMapData) : AppResponse

    suspend fun fetchActivationInitialData() : INActivationInitialResponse

    suspend fun uploadActivationDoc(documentImage: MultipartBody.Part?): AppResponse
    suspend fun activateIndoNepalService(data : FieldMapData): AppResponse
    suspend fun submitCourierData(data : FieldMapData): AppResponse
    suspend fun verifyTransaction(param: FieldMapData) : AppResponse
    suspend fun kycRedirectUrl(param: FieldMapData) : INKycRedirect
    suspend fun onboardSender(param: FieldMapData) : AppResponse

}