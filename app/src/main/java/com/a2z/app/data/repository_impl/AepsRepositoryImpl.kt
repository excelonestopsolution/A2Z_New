package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.settlement.SettlementAddedBankListResponse
import com.a2z.app.data.model.settlement.SettlementBankListResponse
import com.a2z.app.data.network.AepsService
import com.a2z.app.data.repository.AepsRepository
import com.a2z.app.util.FieldMapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AepsRepositoryImpl @Inject constructor(private val service: AepsService) :
    AepsRepository {
    override suspend fun fetchBankList(): AepsBankListResponse {
        return service.fetchBankList()
    }

    override suspend fun fetchSettlementAddedBankList(): SettlementAddedBankListResponse {
        return service.fetchSettlementAddedBankList()
    }

    override suspend fun fetchSettlementBank(): SettlementBankListResponse {
        return service.fetchSettlementBank()
    }

    override suspend fun addSettlementBank(data: FieldMapData): AppResponse {
        return service.addSettlementBank(data)
    }

}