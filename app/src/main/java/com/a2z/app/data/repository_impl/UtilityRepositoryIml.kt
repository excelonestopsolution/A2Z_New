package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.provider.OperatorResponse
import com.a2z.app.data.model.utility.BillFetchInfoResponse
import com.a2z.app.data.model.utility.RechargeDthInfoResponse
import com.a2z.app.data.model.utility.RechargeOfferResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.data.network.UtilityService
import com.a2z.app.data.repository.UtilityRepository
import com.a2z.app.util.PairRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UtilityRepositoryIml @Inject constructor(private val utilityService: UtilityService) :
    UtilityRepository {

    override suspend fun rechargeOperators(vararg data: Pair<String, String>): OperatorResponse {
        return utilityService.rechargeOperators(data.toMap())
    }

    override suspend fun fetchRechargeOffer(vararg data: PairRequest): RechargeOfferResponse {
        return utilityService.fetchRechargeOffer(data.toMap())
    }

    override suspend fun rechargeTransaction(vararg data: PairRequest): RechargeTransactionResponse {
        return utilityService.rechargeTransaction(data.toMap())
    }

    override suspend fun fetchDthInfo(vararg data: PairRequest): RechargeDthInfoResponse {
        return utilityService.fetchDthInfo(data.toMap())
    }

    override suspend fun fetchBillInfo(vararg data: PairRequest): BillFetchInfoResponse {
        return utilityService.fetchBillInfo(data.toMap())
    }


}