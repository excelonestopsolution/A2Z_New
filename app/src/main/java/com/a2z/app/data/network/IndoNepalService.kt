package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.indonepal.*
import com.a2z.app.util.FieldMapData
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    suspend fun txnOtp(@FieldMap data: FieldMapData): INCommonOtpResponse

    @POST("indo-nepal/sender-otp")
    @FormUrlEncoded
    suspend fun senderRegistrationOtp(@FieldMap data: FieldMapData): INCommonOtpResponse

    @POST("indo-nepal/sender-registration")
    @FormUrlEncoded
    suspend fun senderRegistrationOtpVerify(@FieldMap data: FieldMapData): AppResponse

    @GET("indo-nepal/static-data")
    suspend fun staticData(): INStaticData

    @GET("indo-nepal/state-distic")
    suspend fun fetchDistrict(@Query("stateId") stateId: String): INDistrictResponse

    @POST("indo-nepal/get-bank-branches")
    @FormUrlEncoded
    suspend fun fetchBranchList(@Field("bankName") stateId: String): INBranchResponse


    @POST("indo-nepal/create-receiver")
    @FormUrlEncoded
    suspend fun addBeneficiary(@FieldMap data : FieldMapData): AppResponse

    @GET("indo-nepal/user-data")
    suspend fun fetchActivationInitialData(): INActivationInitialResponse

    @Multipart
    @POST("indo-nepal/upload-document")
    suspend fun uploadActivationDoc(
        @Part documentImage: MultipartBody.Part? = null,
    ): AppResponse

    @POST("indo-nepal/service-activation")
    @FormUrlEncoded
    suspend fun activateIndoNepalService(@FieldMap data : FieldMapData): AppResponse

    @POST("indo-nepal/upload-data")
    @FormUrlEncoded
    suspend fun submitCourierData(@FieldMap data : FieldMapData): AppResponse


}
