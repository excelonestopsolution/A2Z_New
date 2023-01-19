package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.kyc.AadhaarKycResponse
import com.a2z.app.data.model.kyc.AepsKycDetailResponse
import com.a2z.app.data.model.kyc.DocumentKycResponse
import com.a2z.app.util.FieldMapData
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface KycService {

    @GET("document/list")
    suspend fun documentKycDetails(): DocumentKycResponse


    @Multipart
    @POST("document/upload")
    suspend fun uploadDocs(
        @Part panFilePart: MultipartBody.Part?,
        @Part profileFilePart: MultipartBody.Part?,
        @Part aadhaarFrontPart: MultipartBody.Part?,
        @Part aadhaarBackPart: MultipartBody.Part?,
        @Part shopPart: MultipartBody.Part?,
        @Part cancelChequePart: MultipartBody.Part?,
        @Part sealChequePart: MultipartBody.Part?,
        @Part gstPart: MultipartBody.Part?,
    ): AppResponse



    @GET("aeps/user-kyc-details")
    suspend fun aepsKycDetail(): AepsKycDetailResponse

    @GET("aeps/kyc/send-otp")
    suspend fun aepsKycRequestOtp(
        @QueryMap data : FieldMapData
    ): AppResponse


    @FormUrlEncoded
    @POST("aeps/kyc/otp/validate")
    suspend fun aepsKycVerifyOtp(
       @FieldMap data : FieldMapData
    ): AppResponse

    @FormUrlEncoded
    @POST("aeps/kyc/verify-user")
    suspend fun aepsKyc(
        @FieldMap data : FieldMapData
    ): AppResponse


    @FormUrlEncoded
    @POST("aadhaar/kyc/send-otp")
    suspend fun aadhaarKycRequestOtp(
        @FieldMap data : FieldMapData
    ): AadhaarKycResponse

    @FormUrlEncoded
    @POST("aadhaar/kyc/verify-otp")
    suspend fun aahdaarKycVerifyOtp(
        @FieldMap data : FieldMapData
    ): AadhaarKycResponse

}
