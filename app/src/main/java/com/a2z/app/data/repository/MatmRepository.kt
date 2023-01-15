package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.matm.*
import com.a2z.app.util.FieldMapData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface MatmRepository {
    suspend fun salemAmountLimit()  : MaPosAmountLimitResponse
    suspend fun initiateMPosTransaction(data : FieldMapData)  : MatmInitiateResponse
    suspend fun initiateMatmTransaction(data : FieldMapData)  : MatmInitiateResponse
    suspend fun postResultData(data : String)  : MatmPostResponse
    suspend fun checkStatus(orderId : String)  : MatmTransactionResponse
    suspend fun orderNow(): AppResponse

    suspend fun fetchOrderInfo(): MatmServiceInformationfoResponse

    suspend fun requestOtp(): AppResponse

    suspend fun verifyOtp(otp: String, ): AppResponse

    suspend fun uploadDetail(
        panCardFilePart: MultipartBody.Part? = null,
        addressProofFilePart: MultipartBody.Part? = null,
        shopInsideFilePart: MultipartBody.Part? = null,
        shopOutsideFilePart: MultipartBody.Part? = null,
        businessLegalityFilePart: MultipartBody.Part? = null,
        businessAddressFilePart: MultipartBody.Part? = null,
         gstNumberBodyPart: RequestBody? = null,
         shopAddressBodyPart: RequestBody? = null,
         mobileBodyPart: RequestBody? = null,
         emailBodyPart: RequestBody? = null,
         shopNameBodyPart: RequestBody? = null,
         courierAddressBodyPart: RequestBody? = null,
         landmarkBodyPart: RequestBody? = null,
         pinCodeBodyPart: RequestBody? = null,
         nameBodyPart: RequestBody? = null,
         cityBodyPart: RequestBody? = null,
         aadhaarBodyPart: RequestBody? = null,
         panBodyPart: RequestBody? = null,
         matmReceivedBodyPart: RequestBody? = null,
         latitudeBodyPart: RequestBody? = null,
         longitudeBodyPart: RequestBody? = null,
         businessLegalityTypeBodyPart: RequestBody? = null,
         businessAddressTypeBodyPart: RequestBody? = null,
    ): AppResponse

    suspend fun getDocTypeList(): MposDocTypeResponse
}