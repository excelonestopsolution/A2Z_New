package com.a2z.app.data.network

import com.a2z.app.data.model.provider.OperatorResponse
import com.a2z.app.data.model.utility.*
import com.a2z.app.util.MapRequest
import retrofit2.http.*

interface UtilityService {

    @GET("get-recharge-provider")
    suspend fun rechargeOperators(@QueryMap data: MapRequest): OperatorResponse


    @GET("get-special-offer")
    suspend fun fetchRechargeOffer(@QueryMap data: MapRequest): ROfferResponse

    @GET("offer/prepaid/simple")
    suspend fun fetchRechargePlan(@QueryMap data: MapRequest): Any


    @GET("get-dth-customer-info")
    suspend fun fetchDthInfo(@QueryMap data: MapRequest): RechargeDthInfoResponse


    @GET("fetch")
    suspend fun fetchBillInfo(@QueryMap data: MapRequest): Any

}
