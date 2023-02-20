package com.a2z.app.data.repository

import com.a2z.app.data.model.indonepal.*
import com.a2z.app.util.FieldMapData

interface IndoNepalRepository {
    suspend fun mobileVerification(data : FieldMapData): INMobileVerificationResponse
    suspend fun fetchBeneficiary(data : FieldMapData): INBeneficiaryResponse
    suspend fun serviceCharge(data : FieldMapData): INServiceChargeResponse
    suspend fun txnOtp(data : FieldMapData): INCommonOtpResponse
    suspend fun senderRegistrationOtp(data : FieldMapData): INCommonOtpResponse
    suspend fun staticData() : Any

}