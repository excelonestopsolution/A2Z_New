package com.a2z.app.data.repository

import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.matm.MaPosAmountLimitResponse
import com.a2z.app.data.model.matm.MatmInitiateResponse
import com.a2z.app.data.model.matm.MatmPostResponse
import com.a2z.app.data.model.matm.MatmTransactionResponse
import com.a2z.app.util.FieldMapData

interface MatmRepository {
    suspend fun salemAmountLimit()  : MaPosAmountLimitResponse
    suspend fun initiateMPosTransaction(data : FieldMapData)  : MatmInitiateResponse
    suspend fun initiateMatmTransaction(data : FieldMapData)  : MatmInitiateResponse
    suspend fun postResultData(data : String)  : MatmPostResponse
    suspend fun checkStatus(orderId : String)  : MatmTransactionResponse
}