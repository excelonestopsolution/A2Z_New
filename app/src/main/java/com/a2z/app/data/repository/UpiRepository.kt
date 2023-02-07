package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.*
import com.a2z.app.data.model.provider.OperatorResponse
import com.a2z.app.data.model.utility.BillFetchInfoResponse
import com.a2z.app.data.model.utility.RechargeDthInfoResponse
import com.a2z.app.data.model.utility.RechargeOfferResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.util.FieldMapData
import com.a2z.app.util.PairRequest
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.QueryMap

interface UpiRepository {

    suspend fun searchSender(@QueryMap data: FieldMapData): Response<MoneySenderResponse>
    suspend fun beneficiaryList(@QueryMap data: FieldMapData): BeneficiaryListResponse
    suspend fun accountValidation(@FieldMap data: FieldMapData): AccountVerify
    suspend fun addBeneficiary(@FieldMap data: FieldMapData): AppResponse
    suspend fun vpaList(): VpaBankExtensionResponse
    suspend fun verificationCharge(@QueryMap data : FieldMapData): VpaVerificationChargeResponse
    suspend fun bankDownCheck(@QueryMap data : FieldMapData): BankDownCheckResponse
    suspend fun upiVerifyStaticMessage(): UpiStaticMessage
    suspend fun checkUpiAccountStatus(data : FieldMapData): AppResponse


}