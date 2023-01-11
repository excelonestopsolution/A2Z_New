package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.settlement.SettlementAddedBankListResponse
import com.a2z.app.data.model.settlement.SettlementBankListResponse
import com.a2z.app.util.FieldMapData
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

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


}
