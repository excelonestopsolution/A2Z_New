package com.a2z.app.data.repository

import com.a2z.app.data.model.provider.OperatorResponse
import com.a2z.app.data.model.utility.*
import com.a2z.app.util.PairRequest

interface UtilityRepository {
    suspend fun rechargeOperators(vararg data: Pair<String, String>): OperatorResponse
    suspend fun fetchRechargeOffer(vararg data: PairRequest): ROfferResponse
    suspend fun fetchRechargePlan(vararg data: PairRequest): Any
    suspend fun fetchDthInfo(vararg data: PairRequest): RechargeDthInfoResponse
    suspend fun fetchBillInfo(vararg data: PairRequest): BillFetchInfoResponse
}