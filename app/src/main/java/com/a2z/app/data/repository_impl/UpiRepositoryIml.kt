package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.*
import com.a2z.app.data.model.provider.OperatorResponse
import com.a2z.app.data.model.utility.BillFetchInfoResponse
import com.a2z.app.data.model.utility.RechargeDthInfoResponse
import com.a2z.app.data.model.utility.RechargeOfferResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.data.network.UpiService
import com.a2z.app.data.network.UtilityService
import com.a2z.app.data.repository.UpiRepository
import com.a2z.app.data.repository.UtilityRepository
import com.a2z.app.util.FieldMapData
import com.a2z.app.util.PairRequest
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpiRepositoryIml @Inject constructor(private val service: UpiService) :
    UpiRepository {
    override suspend fun searchSender(data: FieldMapData): Response<MoneySenderResponse> {
        return service.searchSender(data)
    }

    override suspend fun beneficiaryList(data: FieldMapData): BeneficiaryListResponse {
        return service.beneficiaryList(data)
    }

    override suspend fun accountValidation(data: FieldMapData): AccountVerify {
       return service.accountValidation(data)
    }

    override suspend fun addBeneficiary(data: FieldMapData): AppResponse {
        return service.addBeneficiary(data)
    }

    override suspend fun vpaList(): VpaBankExtensionResponse {
        return service.vpaList()
    }

    override suspend fun verificationCharge(data: FieldMapData): VpaVerificationChargeResponse {
        return service.verificationCharge(data)
    }

    override suspend fun bankDownCheck(data: FieldMapData): BankDownCheckResponse {
        return service.bankDownCheck(data)
    }

    override suspend fun upiVerifyStaticMessage(): UpiStaticMessage {
        return service.upiVerifyStaticMessage()
    }

    override suspend fun checkUpiAccountStatus(data : FieldMapData): AppResponse {
        return service.checkUpiAccountStatus(data)
    }


}