package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.FlightHotelRedirectUrlResponse
import com.a2z.app.data.model.PanAutoLoginResponse
import com.a2z.app.data.model.PanCardServiceNoteResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.dmt.BankDownResponse
import com.a2z.app.data.model.report.CommissionSchemeDetailResponse
import com.a2z.app.data.model.report.CommissionSchemeListResponse
import com.a2z.app.util.FieldMapData
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface AppRepository {
    suspend fun fetchWalletBalance(): BalanceResponse
    suspend fun fetchBanner(): BannerResponse
    suspend fun fetchNews(): NewsResponse
    suspend fun fetchBankDown(): BankDownResponse
    suspend fun fetchQRCode(): QRCodeResponse

    suspend fun schemeList() : CommissionSchemeListResponse

    suspend fun schemeDetail(data : FieldMapData) : CommissionSchemeDetailResponse

    suspend fun flightHotelRedirectUrl(data : FieldMapData) : FlightHotelRedirectUrlResponse
    suspend fun panServiceNote() : PanCardServiceNoteResponse
    suspend fun panServiceActivate(data : FieldMapData) : AppResponse
    suspend fun panAutoLogin(data : FieldMapData) : PanAutoLoginResponse



}