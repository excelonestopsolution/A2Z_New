package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.kyc.DocumentKycResponse
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

}
