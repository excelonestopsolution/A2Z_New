package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.network.KycService
import com.a2z.app.data.repository.KycRepository
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Part
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KycRepositoryImpl @Inject constructor(private val service: KycService) :
    KycRepository {


    override suspend fun documentKycDetails() = service.documentKycDetails()
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


}