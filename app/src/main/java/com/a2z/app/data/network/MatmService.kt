package com.a2z.app.data.network

import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.matm.MaPosAmountLimitResponse
import com.a2z.app.data.model.matm.MatmInitiateResponse
import com.a2z.app.data.model.matm.MatmPostResponse
import com.a2z.app.data.model.matm.MatmTransactionResponse
import com.a2z.app.util.FieldMapData
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

}
