package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.MoneySenderResponse
import com.a2z.app.data.model.dmt.SenderRegistrationResponse
import com.a2z.app.util.FieldMapData
import retrofit2.Response
import retrofit2.http.*

interface DMT3Service {

    @GET("dmt-three/mobile-verification")
    suspend fun searchMobileNumberDmtThree(@QueryMap data: FieldMapData): MoneySenderResponse


    @FormUrlEncoded
    @POST("dmt-three/remitter/send/otp")
    suspend fun registerSender(@FieldMap data: FieldMapData): SenderRegistrationResponse

    @FormUrlEncoded
    @POST("dmt-three/remitter/registration")
    suspend fun verifySender(@FieldMap data: FieldMapData): AppResponse


    @FormUrlEncoded
    @POST("dmt-three/remitter/send/otp")
    suspend fun resendSenderRegistrationOtp(@FieldMap data: FieldMapData): SenderRegistrationResponse

}