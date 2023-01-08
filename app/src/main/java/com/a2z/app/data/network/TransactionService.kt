package com.a2z.app.data.network

import com.a2z.app.data.model.dmt.TransactionDetail
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.utility.BillPaymentResponse
import com.a2z.app.util.FieldMapData
import com.a2z.app.util.MapRequest
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TransactionService {
    @FormUrlEncoded
    @POST("bill-payment-one")
    suspend fun billPaymentRouteOne(@FieldMap data: MapRequest): BillPaymentResponse

    @FormUrlEncoded
    @POST("bill-payment-two")
    suspend fun billPaymentRouteTwo(@FieldMap data: MapRequest): BillPaymentResponse

    @POST("a2z/plus/wallet/transaction")
    @FormUrlEncoded
    suspend fun wallet1Transaction(@FieldMap data: FieldMapData): TransactionDetailResponse

    @POST("a2z/plus/wallet-two/transaction")
    @FormUrlEncoded
    suspend fun wallet2Transaction(@FieldMap data: FieldMapData): TransactionDetailResponse

    @POST("a2z/plus/wallet-three/transaction")
    @FormUrlEncoded
    suspend fun wallet3Transaction(@FieldMap data: FieldMapData): TransactionDetailResponse

    @POST("dmt-three/transaction")
    @FormUrlEncoded
    suspend fun dmt3Transaction(@FieldMap data: FieldMapData): TransactionDetailResponse


    @POST("vpa/payment")
    @FormUrlEncoded
    suspend fun upiTransaction(@FieldMap data: FieldMapData): TransactionDetail

}
