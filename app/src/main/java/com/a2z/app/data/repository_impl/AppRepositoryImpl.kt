package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.FlightHotelRedirectUrlResponse
import com.a2z.app.data.model.PanAutoLoginResponse
import com.a2z.app.data.model.PanCardServiceNoteResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.dmt.BankDownResponse
import com.a2z.app.data.model.indonepal.INRequestStatus
import com.a2z.app.data.model.kyc.KycInfoResponse
import com.a2z.app.data.model.report.CommissionSchemeDetailResponse
import com.a2z.app.data.model.report.CommissionSchemeListResponse
import com.a2z.app.data.network.AppService
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.util.FieldMapData
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

    override suspend fun fetchBankDown(): BankDownResponse {
        return appService.fetchBankDown()
    }

    override suspend fun fetchQRCode(): QRCodeResponse {
        return appService.fetchQRCode()
    }

    override suspend fun schemeList(): CommissionSchemeListResponse {
        return appService.schemeList()
    }

    override suspend fun schemeDetail(data: FieldMapData): CommissionSchemeDetailResponse {
        return appService.schemeDetail(data)
    }

    override suspend fun flightHotelRedirectUrl(data : FieldMapData): FlightHotelRedirectUrlResponse {
        return appService.flightHotelRedirectUrl(data)
    }

    override suspend fun panServiceNote() = appService.panServiceNote()
    override suspend fun panServiceActivate(data : FieldMapData) = appService.panServiceActivate(data)
    override suspend fun panAutoLogin(data: FieldMapData) = appService.panAutoLogin(data)
    override suspend fun mobileEmailVerifyRequestOtp(data: FieldMapData) = appService.mobileEmailVerifyRequestOtp(data)
    override suspend fun mobileEmailVerifyOtp(data: FieldMapData)=appService.mobileEmailVerifyOtp(data)
    override suspend fun kycCheck() = appService.kycCheck()
    override suspend fun checkIndoNepalStatus() = appService.checkIndoNepalStatus()
}