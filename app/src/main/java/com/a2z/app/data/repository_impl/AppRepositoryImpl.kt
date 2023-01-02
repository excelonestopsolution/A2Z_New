package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.network.AppService
import com.a2z.app.data.repository.AppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(private val appService: AppService) :
    AppRepository {
    override suspend fun fetchWalletBalance(): BalanceResponse {
        return appService.fetchWalletBalance()
    }

    override suspend fun fetchBanner(): BannerResponse {
        return appService.fetchBanner()
    }

    override suspend fun fetchNews(): NewsResponse {
        return appService.fetchNews()
    }

    override suspend fun fetchQRCode(): QRCodeResponse {
        return appService.fetchQRCode()
    }
}