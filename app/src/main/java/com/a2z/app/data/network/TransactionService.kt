package com.a2z.app.data.network

import com.a2z.app.data.model.utility.BillPaymentResponse
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
}
