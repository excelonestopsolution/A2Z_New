package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.provider.OperatorResponse
import com.a2z.app.data.model.utility.*
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

    override suspend fun fetchRechargeOffer(vararg data: PairRequest): ROfferResponse {
        return utilityService.fetchRechargeOffer(data.toMap())
    }

    override suspend fun fetchRechargePlan(vararg data: PairRequest): Any {
        return utilityService.fetchRechargePlan(data.toMap())
    }


    override suspend fun fetchDthInfo(vararg data: PairRequest): RechargeDthInfoResponse {
        return utilityService.fetchDthInfo(data.toMap())
    }

    override suspend fun fetchBillInfo(vararg data: PairRequest): Any {
        return utilityService.fetchBillInfo(data.toMap())
    }


}