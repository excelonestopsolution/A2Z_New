package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.kyc.AadhaarKycResponse
import com.a2z.app.data.model.kyc.AepsKycDetailResponse
import com.a2z.app.data.network.KycService
import com.a2z.app.data.repository.KycRepository
import com.a2z.app.util.FieldMapData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KycRepositoryImpl @Inject constructor(private val service: KycService) :
    KycRepository {


    override suspend fun documentKycDetails() = service.documentKycDetails()
    override suspend fun registerUserUploadedFilesDetails(userId: String) =
        service.registerUserUploadedFilesDetails(userId)


    override suspend fun uploadDocs(
        panFilePart: MultipartBody.Part?,
        profileFilePart: MultipartBody.Part?,
        aadhaarFrontPart: MultipartBody.Part?,
        aadhaarBackPart: MultipartBody.Part?,
        shopPart: MultipartBody.Part?,
        cancelChequePart: MultipartBody.Part?,
        sealChequePart: MultipartBody.Part?,
        gstPart: MultipartBody.Part?
    ): AppResponse {
        return service.uploadDocs(
            panFilePart = panFilePart,
            profileFilePart = profileFilePart,
            aadhaarFrontPart = aadhaarFrontPart,
            aadhaarBackPart = aadhaarBackPart,
            shopPart = shopPart,
            cancelChequePart = cancelChequePart,
            sealChequePart = sealChequePart,
            gstPart = gstPart,
        )
    }


    override suspend fun registerUserUploadDocs(
        panFilePart: MultipartBody.Part?,
        profileFilePart: MultipartBody.Part?,
        aadhaarFrontPart: MultipartBody.Part?,
        aadhaarBackPart: MultipartBody.Part?,
        shopPart: MultipartBody.Part?,
        cancelChequePart: MultipartBody.Part?,
        sealChequePart: MultipartBody.Part?,
        gstPart: MultipartBody.Part?,
        userId: RequestBody,
    ) = service.registerUserUploadDocs(
        panFilePart = panFilePart,
        profileFilePart = profileFilePart,
        aadhaarFrontPart = aadhaarFrontPart,
        aadhaarBackPart = aadhaarBackPart,
        shopPart = shopPart,
        cancelChequePart = cancelChequePart,
        sealChequePart = sealChequePart,
        gstPart = gstPart,
        userId = userId,
    )

    override suspend fun aepsKycDetail(): AepsKycDetailResponse {
        return service.aepsKycDetail()
    }

    override suspend fun aepsKycRequestOtp(data: FieldMapData): AppResponse {
        return service.aepsKycRequestOtp(data)
    }

    override suspend fun aepsKycVerifyOtp(data: FieldMapData): AppResponse {
        return service.aepsKycVerifyOtp(data)
    }

    override suspend fun aepsKyc(data: FieldMapData): AppResponse {
        return service.aepsKyc(data)
    }

    override suspend fun aadhaarKycRequestOtp(data: FieldMapData): AadhaarKycResponse {
        return service.aadhaarKycRequestOtp(data)
    }

    override suspend fun aahdaarKycVerifyOtp(data: FieldMapData): AadhaarKycResponse {
        return service.aahdaarKycVerifyOtp(data)
    }

    override suspend fun registerUserPostAadharKyc(data: FieldMapData): AadhaarKycResponse {
        return service.registerUserPostAadharKyc(data)
    }

    override suspend fun registerUserPostAadharKycVerify(data: FieldMapData): AadhaarKycResponse {
        return service.registerUserPostAadharKycVerify(data)
    }


}