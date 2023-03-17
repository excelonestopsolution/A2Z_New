package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.aeps.AepsLimitResponse
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.settlement.SettlementAddedBankListResponse
import com.a2z.app.data.model.settlement.SettlementBankListResponse
import com.a2z.app.util.FieldMapData
import retrofit2.http.*

interface AepsService {

    @GET("aeps/bank/list")
    suspend fun fetchBankList(): AepsBankListResponse


    @GET("get-my-bank-details")
    suspend fun fetchSettlementAddedBankList(): SettlementAddedBankListResponse


    @GET("aeps/settlement-bank")
    suspend fun fetchSettlementBank(): SettlementBankListResponse

    @POST("aeps/add-bank-details")
    @FormUrlEncoded
    suspend fun addSettlementBank(@FieldMap data: FieldMapData): AppResponse


    @POST("aeps/three/table-check-status")
    @FormUrlEncoded
    suspend fun tableCheckStatus(@FieldMap data: FieldMapData): AepsTransaction


    @POST("aeps/three/checkstatus")
    @FormUrlEncoded
    suspend fun bankCheckStatus(@FieldMap data: FieldMapData): AepsTransaction

    @GET("aeps/bank/limit")
    suspend fun aepsLimit(@QueryMap data: FieldMapData): AepsLimitResponse


}
