package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.matm.*
import com.a2z.app.data.network.AepsService
import com.a2z.app.data.network.AppService
import com.a2z.app.data.network.MatmService
import com.a2z.app.data.repository.AepsRepository
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.data.repository.MatmRepository
import com.a2z.app.util.FieldMapData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatmRepositoryImpl @Inject constructor(private val service: MatmService) :
    MatmRepository {
    override suspend fun salemAmountLimit(): MaPosAmountLimitResponse {
        return service.salemAmountLimit()
    }

    override suspend fun initiateMPosTransaction(data: FieldMapData): MatmInitiateResponse {
        return service.initiateMPosTransaction(data)
    }

    override suspend fun initiateMatmTransaction(data: FieldMapData): MatmInitiateResponse {
        return service.initiateMatmTransaction(data)
    }

    override suspend fun postResultData(data: String): MatmPostResponse {
        return service.postResultData(data)
    }

    override suspend fun checkStatus(orderId: String): MatmTransactionResponse {
        return service.checkStatus(orderId)
    }

    override suspend fun orderNow(): AppResponse {
        return service.orderNow()
    }

    override suspend fun fetchOrderInfo(): MatmServiceInformationfoResponse {
        return service.fetchOrderInfo()
    }

    override suspend fun requestOtp(): AppResponse {
        return service.requestOtp()
    }

    override suspend fun verifyOtp(otp: String): AppResponse {
        return service.verifyOtp(otp)
    }

    override suspend fun uploadDetail(
        panCardFilePart: MultipartBody.Part?,
        addressProofFilePart: MultipartBody.Part?,
        shopInsideFilePart: MultipartBody.Part?,
        shopOutsideFilePart: MultipartBody.Part?,
        businessLegalityFilePart: MultipartBody.Part?,
        businessAddressFilePart: MultipartBody.Part?,
        gstNumberBodyPart: RequestBody?,
        shopAddressBodyPart: RequestBody?,
        mobileBodyPart: RequestBody?,
        emailBodyPart: RequestBody?,
        shopNameBodyPart: RequestBody?,
        courierAddressBodyPart: RequestBody?,
        landmarkBodyPart: RequestBody?,
        pinCodeBodyPart: RequestBody?,
        nameBodyPart: RequestBody?,
        cityBodyPart: RequestBody?,
        aadhaarBodyPart: RequestBody?,
        panBodyPart: RequestBody?,
        matmReceivedBodyPart: RequestBody?,
        latitudeBodyPart: RequestBody?,
        longitudeBodyPart: RequestBody?,
        businessLegalityTypeBodyPart: RequestBody?,
        businessAddressTypeBodyPart: RequestBody?
    ): AppResponse {
       return  service.uploadDetail(
            panCardFilePart,
            addressProofFilePart,
            shopInsideFilePart,
            shopOutsideFilePart,
            businessLegalityFilePart,
            businessAddressFilePart,
            gstNumberBodyPart,
            shopAddressBodyPart,
            mobileBodyPart,
            emailBodyPart,
            shopNameBodyPart,
            courierAddressBodyPart,
            landmarkBodyPart,
            pinCodeBodyPart,
            nameBodyPart,
            cityBodyPart,
            aadhaarBodyPart,
            panBodyPart,
            matmReceivedBodyPart,
            latitudeBodyPart,
            longitudeBodyPart,
            businessLegalityTypeBodyPart,
            businessAddressTypeBodyPart,
        )
    }

    override suspend fun getDocTypeList(): MposDocTypeResponse {
        return service.getDocTypeList()
    }


}