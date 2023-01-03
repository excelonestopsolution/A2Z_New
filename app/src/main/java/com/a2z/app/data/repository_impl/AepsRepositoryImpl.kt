package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.network.AepsService
import com.a2z.app.data.network.AppService
import com.a2z.app.data.repository.AepsRepository
import com.a2z.app.data.repository.AppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AepsRepositoryImpl @Inject constructor(private val service: AepsService) :
    AepsRepository {
    override suspend fun fetchBankList(): AepsBankListResponse {
        return service.fetchBankList()
    }

}