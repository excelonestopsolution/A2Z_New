package com.a2z.app.dist.data.service

import com.a2z.app.model.pg.RzpayOrderAckDataModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PaymentGatrewayAndTicket {

    @FormUrlEncoded
    @POST("pg/create-order")
    suspend fun  getOrderAckNumber(
        @Field("amount") amount: Long,
        @Field("customer_mobile") mobile: Long,
    ): Response<RzpayOrderAckDataModel>

}