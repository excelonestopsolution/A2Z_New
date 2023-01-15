package com.a2z.app.data.network

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

interface MatmService {

    @GET("matm/mpos/amount-range")
    suspend fun salemAmountLimit(): MaPosAmountLimitResponse

    @FormUrlEncoded
    @POST("matm/mpos/transaction")
    suspend fun initiateMPosTransaction(@FieldMap data : FieldMapData): MatmInitiateResponse

    @FormUrlEncoded
    @POST("matm/get-txn-data")
    suspend fun initiateMatmTransaction(@FieldMap data : FieldMapData): MatmInitiateResponse

    @FormUrlEncoded
    @POST("matm/update/txn-data")
    suspend fun postResultData(
        @Field("app_response") data: String,
    ): MatmPostResponse

    @GET("matm/checkstatus")
    suspend fun checkStatus(@Query("recordId") orderId: String): MatmTransactionResponse

    @GET("matm/serviceactivation")
    suspend fun orderNow(): AppResponse

    @GET("matm/userdata")
    suspend fun fetchOrderInfo(): MatmServiceInformationfoResponse

    @GET("matm/send/otp")
    suspend fun requestOtp(): AppResponse

    @FormUrlEncoded
    @POST("matm/verify/otp")
    suspend fun verifyOtp(
        @Field("otp") otp: String,
    ): AppResponse

    @Multipart
    @POST("matm/uploadDocument")
    suspend fun uploadDetail(
        @Part panCardFilePart: MultipartBody.Part? = null,
        @Part addressProofFilePart: MultipartBody.Part? = null,
        @Part shopInsideFilePart: MultipartBody.Part? = null,
        @Part shopOutsideFilePart: MultipartBody.Part? = null,
        @Part businessLegalityFilePart: MultipartBody.Part? = null,
        @Part businessAddressFilePart: MultipartBody.Part? = null,
        @Part("gstNumber") gstNumberBodyPart: RequestBody? = null,
        @Part("shopAddress") shopAddressBodyPart: RequestBody? = null,
        @Part("mobile") mobileBodyPart: RequestBody? = null,
        @Part("email") emailBodyPart: RequestBody? = null,
        @Part("shopName") shopNameBodyPart: RequestBody? = null,
        @Part("courierAddress") courierAddressBodyPart: RequestBody? = null,
        @Part("landMark") landmarkBodyPart: RequestBody? = null,
        @Part("pinCode") pinCodeBodyPart: RequestBody? = null,
        @Part("name") nameBodyPart: RequestBody? = null,
        @Part("city") cityBodyPart: RequestBody? = null,
        @Part("aadhaarNumber") aadhaarBodyPart: RequestBody? = null,
        @Part("panNumber") panBodyPart: RequestBody? = null,
        @Part("is_mAtm_received") matmReceivedBodyPart: RequestBody? = null,
        @Part("lat") latitudeBodyPart: RequestBody? = null,
        @Part("long") longitudeBodyPart: RequestBody? = null,
        @Part("business_proof_type") businessLegalityTypeBodyPart: RequestBody? = null,
        @Part("business_address_proof_type") businessAddressTypeBodyPart: RequestBody? = null,
    ): AppResponse

    @GET("matm/getdocument-type")
    suspend fun getDocTypeList(): MposDocTypeResponse


}
