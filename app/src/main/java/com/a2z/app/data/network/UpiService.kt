package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.*
import com.a2z.app.util.FieldMapData
import retrofit2.Response
import retrofit2.http.*

interface UpiService {

    @GET("vpa/mobile/remitter")
    suspend fun searchSender(@QueryMap data: FieldMapData): Response<MoneySenderResponse>

    @GET("vpa/beneficiary")
    suspend fun beneficiaryList(@QueryMap data: FieldMapData): BeneficiaryListResponse

    @FormUrlEncoded
    @POST("vpa/validate")
    suspend fun accountValidation(@FieldMap data: FieldMapData): AccountVerify


    @FormUrlEncoded
    @POST("vpa/beneficiary/add")
    suspend fun addBeneficiary(@FieldMap data: FieldMapData): AppResponse


    @GET("vpa/bank")
    suspend fun vpaList(): VpaBankExtensionResponse

    @GET("vpa/validation/charge")
    suspend fun verificationCharge(@QueryMap data : FieldMapData): VpaVerificationChargeResponse


   @GET("vpa/checkdown")
    suspend fun bankDownCheck(@QueryMap data : FieldMapData): BankDownCheckResponse


}