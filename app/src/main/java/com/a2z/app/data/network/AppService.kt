package com.a2z.app.data.network

import com.a2z.app.data.model.FlightHotelRedirectUrlResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.dmt.BankDownResponse
import com.a2z.app.data.model.report.CommissionSchemeDetailResponse
import com.a2z.app.data.model.report.CommissionSchemeListResponse
import com.a2z.app.util.FieldMapData
import retrofit2.Response
import retrofit2.http.GET
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


    @GET("travel/flight-hotel")
    suspend fun flightHotelRedirectUrl(): FlightHotelRedirectUrlResponse


}
