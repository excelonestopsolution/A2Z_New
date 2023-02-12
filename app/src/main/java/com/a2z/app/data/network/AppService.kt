package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.FlightHotelRedirectUrlResponse
import com.a2z.app.data.model.PanAutoLoginResponse
import com.a2z.app.data.model.PanCardServiceNoteResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.dmt.BankDownResponse
import com.a2z.app.data.model.kyc.KycInfoResponse
import com.a2z.app.data.model.report.CommissionSchemeDetailResponse
import com.a2z.app.data.model.report.CommissionSchemeListResponse
import com.a2z.app.util.FieldMapData
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface AppService {

    @GET("user/balance")
    suspend fun fetchWalletBalance(): BalanceResponse

    @GET("user/slide")
    suspend fun fetchBanner(): BannerResponse

    @GET("get-news")
    suspend fun fetchNews(): NewsResponse

    @GET("user/static/upi")
    suspend fun fetchQRCode(): QRCodeResponse

    @GET("a2z/plus/wallet/bank-down")
    suspend fun fetchBankDown(): BankDownResponse

    @GET("user/scheme/list")
    suspend fun schemeList(): CommissionSchemeListResponse

    @GET("user/scheme")
    suspend fun schemeDetail(@QueryMap data: FieldMapData): CommissionSchemeDetailResponse


    @FormUrlEncoded
    @POST("travel/flight-hotel")
    suspend fun flightHotelRedirectUrl(@FieldMap data: FieldMapData): FlightHotelRedirectUrlResponse

    @GET("utipan-card/activation/static-message")
    suspend fun panServiceNote(): PanCardServiceNoteResponse


    @FormUrlEncoded
    @POST("utipan-card/activation")
    suspend fun panServiceActivate(@FieldMap data: FieldMapData): AppResponse


    @FormUrlEncoded
    @POST("utipan-card/autologin")
    suspend fun panAutoLogin(@FieldMap data: FieldMapData): PanAutoLoginResponse

    @FormUrlEncoded
    @POST("user/send-verification-otp")
    suspend fun mobileEmailVerifyRequestOtp(@FieldMap data: FieldMapData): AppResponse

   @FormUrlEncoded
    @POST("user/validate-verification-otp")
    suspend fun mobileEmailVerifyOtp(@FieldMap data: FieldMapData): AppResponse

    @GET("user/kyc/check")
    suspend fun kycCheck(): KycInfoResponse

}
