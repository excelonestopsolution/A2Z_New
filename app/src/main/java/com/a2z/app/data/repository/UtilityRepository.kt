package com.a2z.app.data.repository

import com.a2z.app.data.model.provider.OperatorResponse
import com.a2z.app.data.model.utility.BillFetchInfoResponse
import com.a2z.app.data.model.utility.RechargeDthInfoResponse
import com.a2z.app.data.model.utility.RechargeOfferResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.util.PairRequest

interface UtilityRepository {
    suspend fun rechargeOperators(vararg data: Pair<String, String>): OperatorResponse
    suspend fun fetchRechargeOffer(vararg data: PairRequest): RechargeOfferResponse
    suspend fun rechargeTransaction(vararg data: PairRequest): RechargeTransactionResponse
    suspend fun fetchDthInfo(vararg data: PairRequest): RechargeDthInfoResponse
    suspend fun fetchBillInfo(vararg data: PairRequest): BillFetchInfoResponse
}