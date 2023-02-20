package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.indonepal.*
import com.a2z.app.data.model.settlement.SettlementAddedBankListResponse
import com.a2z.app.data.model.settlement.SettlementBankListResponse
import com.a2z.app.util.FieldMapData
import retrofit2.http.*

interface IndoNepalService {

    @GET("indo-nepal/mobile-verification")
    suspend fun mobileVerification(@QueryMap data: FieldMapData): INMobileVerificationResponse


    @POST("indo-nepal/get-receiver")
    @FormUrlEncoded
    suspend fun fetchBeneficiary(@FieldMap data: FieldMapData): INBeneficiaryResponse


    @POST("indo-nepal/service-charge")
    @FormUrlEncoded
    suspend fun serviceCharge(@FieldMap data: FieldMapData): INServiceChargeResponse


    @POST("indo-nepal/txn-otp")
    @FormUrlEncoded
    suspend fun txnOtp(@FieldMap data: FieldMapData): INTxnOtpResponse


}
