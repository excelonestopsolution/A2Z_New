package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.fund.FundRequestBankListResponse
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

}
