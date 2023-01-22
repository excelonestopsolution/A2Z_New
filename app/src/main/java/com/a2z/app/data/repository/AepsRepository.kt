package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.settlement.SettlementAddedBankListResponse
import com.a2z.app.data.model.settlement.SettlementBankListResponse
import com.a2z.app.util.FieldMapData

interface AepsRepository {
    suspend fun fetchBankList(): AepsBankListResponse

    //aeps settlement
    suspend fun fetchSettlementAddedBankList() : SettlementAddedBankListResponse
    suspend fun fetchSettlementBank() : SettlementBankListResponse
    suspend fun addSettlementBank(data : FieldMapData) : AppResponse
    suspend fun tableCheckStatus(data : FieldMapData) : AepsTransaction
    suspend fun bankCheckStatus(data : FieldMapData) : AepsTransaction
}