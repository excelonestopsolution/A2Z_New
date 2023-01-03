package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.matm.MaPosAmountLimitResponse
import com.a2z.app.data.network.AepsService
import com.a2z.app.data.network.AppService
import com.a2z.app.data.network.MatmService
import com.a2z.app.data.repository.AepsRepository
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.data.repository.MatmRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatmRepositoryImpl @Inject constructor(private val service: MatmService) :
    MatmRepository {
    override suspend fun salemAmountLimit(): MaPosAmountLimitResponse {
        return service.salemAmountLimit()
    }


}