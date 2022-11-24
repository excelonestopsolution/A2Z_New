package com.a2z.app.data.network

import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import retrofit2.http.GET

interface AppService {

    @GET("user/balance")
    suspend fun fetchWalletBalance(): BalanceResponse

    @GET("user/slide")
    suspend fun fetchBanner(): BannerResponse

    @GET("get-news")
    suspend fun fetchNews(): NewsResponse


}
