package com.a2z.app.data.network

import com.a2z.app.data.model.provider.OperatorResponse
import com.a2z.app.data.model.utility.BillFetchInfoResponse
import com.a2z.app.data.model.utility.RechargeDthInfoResponse
import com.a2z.app.data.model.utility.RechargeOfferResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.util.MapRequest
import retrofit2.http.*

interface UtilityService {

    @GET("get-recharge-provider")
    suspend fun rechargeOperators(@QueryMap data: MapRequest): OperatorResponse


    @GET("get-special-offer")
    suspend fun fetchRechargeOffer(@QueryMap data: MapRequest): RechargeOfferResponse

    @FormUrlEncoded
    @POST("make-recharge")
    suspend fun rechargeTransaction(@FieldMap data: MapRequest): RechargeTransactionResponse

    @GET("get-dth-customer-info")
    suspend fun fetchDthInfo(@QueryMap data: MapRequest): RechargeDthInfoResponse


    @GET("fetch")
    suspend fun fetchBillInfo(@QueryMap data: MapRequest): BillFetchInfoResponse

}
