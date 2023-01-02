package com.a2z.app.data.repository

import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse

interface AppRepository {
    suspend fun fetchWalletBalance(): BalanceResponse
    suspend fun fetchBanner(): BannerResponse
    suspend fun fetchNews(): NewsResponse
    suspend fun fetchQRCode(): QRCodeResponse
}