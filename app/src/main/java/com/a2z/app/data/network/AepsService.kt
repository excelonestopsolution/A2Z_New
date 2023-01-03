package com.a2z.app.data.network

import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import retrofit2.Response
import retrofit2.http.GET

interface AepsService {

    @GET("aeps/bank/list")
    suspend fun fetchBankList(): AepsBankListResponse



}
