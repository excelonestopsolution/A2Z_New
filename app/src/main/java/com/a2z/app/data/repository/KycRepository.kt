package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.kyc.AadhaarKycResponse
import com.a2z.app.data.model.kyc.AepsKycDetailResponse
import com.a2z.app.data.model.kyc.DocumentKycResponse
import com.a2z.app.util.FieldMapData
import okhttp3.MultipartBody
import retrofit2.Response

interface KycRepository {

    suspend fun documentKycDetails(): DocumentKycResponse

    suspend fun uploadDocs(
        panFilePart: MultipartBody.Part?,
        profileFilePart: MultipartBody.Part?,
        aadhaarFrontPart: MultipartBody.Part?,
        aadhaarBackPart: MultipartBody.Part?,
        shopPart: MultipartBody.Part?,
        cancelChequePart: MultipartBody.Part?,
        sealChequePart: MultipartBody.Part?,
        gstPart: MultipartBody.Part?,
    ): AppResponse

    suspend fun aepsKycDetail() : AepsKycDetailResponse
    suspend fun aepsKycRequestOtp(data : FieldMapData) : AppResponse
    suspend fun aepsKycVerifyOtp(data : FieldMapData) : AppResponse
    suspend fun aepsKyc(data : FieldMapData) : AppResponse
    suspend fun aadhaarKycRequestOtp(data : FieldMapData) : AadhaarKycResponse
    suspend fun aahdaarKycVerifyOtp(data : FieldMapData) : AadhaarKycResponse
}