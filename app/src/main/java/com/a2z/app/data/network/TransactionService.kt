package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.dmt.TransactionDetail
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.dmt.UpiVerifyPayment
import com.a2z.app.data.model.indonepal.INTransferResponse
import com.a2z.app.data.model.utility.BillPaymentResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.util.FieldMapData
import com.a2z.app.util.MapRequest
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface TransactionService {
    @FormUrlEncoded
    @POST("bill-payment-one")
    suspend fun billPaymentRouteOne(@FieldMap data: MapRequest): BillPaymentResponse

    @FormUrlEncoded
    @POST("bill-payment-two")
    suspend fun billPaymentRouteTwo(@FieldMap data: MapRequest): BillPaymentResponse
    @FormUrlEncoded
    @POST("recharge/new")
    suspend fun rechargeTransaction(@FieldMap data: MapRequest): RechargeTransactionResponse

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

    @POST("vpa/two/transaction")
    @FormUrlEncoded
    suspend fun upi2Transaction(@FieldMap data: FieldMapData): TransactionDetail


    @POST("vpa/verify/payment")
    @FormUrlEncoded
    suspend fun upiVerifyPayment(@FieldMap data: FieldMapData): UpiVerifyPayment

    @POST("fund-transfer_r2r")
    @FormUrlEncoded
    suspend fun r2rTransfer(
        @FieldMap data : FieldMapData
    ) : AppResponse

    @POST("a2z-settlement-request")
    @FormUrlEncoded
    suspend fun settlementTransfer(
        @FieldMap data : FieldMapData
    ) : AppResponse

    @POST("aeps/one-new")
    @FormUrlEncoded
    suspend fun aeps1Transaction(
        @FieldMap data : FieldMapData
    ) : AepsTransaction


    @POST("aeps/two-new")
    @FormUrlEncoded
    suspend fun aeps2Transaction(
        @FieldMap data : FieldMapData
    ) : AepsTransaction

    @POST("aeps/three-new")
    @FormUrlEncoded
    suspend fun aeps3Transaction(
        @FieldMap data : FieldMapData
    ) : AepsTransaction

    @POST("parent/payment-return")
    @FormUrlEncoded
    suspend fun parentPaymentFundReturn(
        @FieldMap data : FieldMapData
    ) : AppResponse

    @GET("agent-request-approve")
    suspend fun approveFundRequest(
        @QueryMap data : FieldMapData
    ) : AppResponse
    @POST("fund-transafer")
    @FormUrlEncoded
    suspend fun memberFundTransfer(
        @FieldMap data : FieldMapData
    ) : AppResponse

    @POST("indo-nepal/transaction")
    @FormUrlEncoded
    suspend fun indoNepalTransfer(
        @FieldMap data : FieldMapData
    ) : INTransferResponse
}
