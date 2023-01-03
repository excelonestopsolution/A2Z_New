package com.a2z.app.data.repository

import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.matm.MaPosAmountLimitResponse

interface MatmRepository {
    suspend fun salemAmountLimit()  : MaPosAmountLimitResponse
}