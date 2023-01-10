package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.matm.MaPosAmountLimitResponse
import com.a2z.app.data.model.matm.MatmInitiateResponse
import com.a2z.app.data.model.matm.MatmPostResponse
import com.a2z.app.data.model.matm.MatmTransactionResponse
import com.a2z.app.data.network.AepsService
import com.a2z.app.data.network.AppService
import com.a2z.app.data.network.MatmService
import com.a2z.app.data.repository.AepsRepository
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.data.repository.MatmRepository
import com.a2z.app.util.FieldMapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatmRepositoryImpl @Inject constructor(private val service: MatmService) :
    MatmRepository {
    override suspend fun salemAmountLimit(): MaPosAmountLimitResponse {
        return service.salemAmountLimit()
    }

    override suspend fun initiateMPosTransaction(data: FieldMapData): MatmInitiateResponse {
        return service.initiateMPosTransaction(data)
    }

    override suspend fun initiateMatmTransaction(data: FieldMapData): MatmInitiateResponse {
        return service.initiateMatmTransaction(data)
    }

    override suspend fun postResultData(data: String): MatmPostResponse {
        return service.postResultData(data)
    }

    override suspend fun checkStatus(orderId: String): MatmTransactionResponse {
        return service.checkStatus(orderId)
    }


}