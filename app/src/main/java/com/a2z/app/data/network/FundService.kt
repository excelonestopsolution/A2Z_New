package com.a2z.app.data.network

import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.fund.FundRequestBankListResponse
import com.a2z.app.data.model.fund.PaymentGatewayInitiateResponse
import com.a2z.app.data.model.fund.PaymentReturnDetailResponse
import com.a2z.app.data.model.fund.UpiPaymentInitiateResponse
import com.a2z.app.data.model.r2r.R2RSearchRetailerResponse
import com.a2z.app.util.FieldMapData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface FundService {

    @GET("get-bank-details")
    suspend fun fetchFundBankList(
        @Query("type") type: String
    ): FundRequestBankListResponse

    @Multipart
    @POST("fund-request-save-new")
    suspend fun submitRequest(
        @Part slipFile: MultipartBody.Part?,
        @Part("amount") amount: RequestBody,
        @Part("requestTo") requestTo: RequestBody,
        @Part("paymentDate") paymentDate: RequestBody,
        @Part("paymentMode") paymentMode: RequestBody,
        @Part("refNumber") refNumber: RequestBody,
        @Part("onlineMode") onlineMode: RequestBody,
        @Part("bankId") bankId: RequestBody?,
        @Part("bankName") bankName: RequestBody?,
        @Part("remark") remark: RequestBody
    ): AppResponse

    //r2r

    @GET("get-retailer-detail")
    suspend fun searchR2RRetailer(
        @Query("SEARCH_TYPE") searchType: String,
        @Query("INPUT") searchInput: String
    ): R2RSearchRetailerResponse


    @GET("parent/payment-return")
    suspend fun fetchParentPaymentReturnDetail(): PaymentReturnDetailResponse


    @FormUrlEncoded
    @POST("generate-qr-code")
    suspend fun initiateUpiPayment(@FieldMap data: FieldMapData): UpiPaymentInitiateResponse


    @FormUrlEncoded
    @POST("pg/create-order")
    suspend fun initiatePaymentGatewayRequest(@FieldMap data: FieldMapData): PaymentGatewayInitiateResponse


}
