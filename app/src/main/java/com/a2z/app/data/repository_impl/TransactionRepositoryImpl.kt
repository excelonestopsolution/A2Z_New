package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.utility.BillPaymentResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.data.network.TransactionService
import com.a2z.app.data.repository.TransactionRepository
import com.a2z.app.util.FieldMapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(private val service: TransactionService) :
    TransactionRepository {
    override suspend fun billPaymentRouteOne(data: HashMap<String, String>): BillPaymentResponse {
        return service.billPaymentRouteOne(data)
    }

    override suspend fun billPaymentRouteTwo(data: HashMap<String, String>): BillPaymentResponse {
        return service.billPaymentRouteTwo(data)
    }

    override suspend fun rechargeTransaction(data: HashMap<String, String>): RechargeTransactionResponse {
        return service.rechargeTransaction(data)
    }

    override suspend fun wallet1Transaction(data: FieldMapData) = service.wallet1Transaction(data)
    override suspend fun wallet2Transaction(data: FieldMapData) = service.wallet2Transaction(data)
    override suspend fun wallet3Transaction(data: FieldMapData) = service.wallet3Transaction(data)
    override suspend fun dmt3Transaction(data: FieldMapData) = service.dmt3Transaction(data)
    override suspend fun upiTransaction(data: FieldMapData) = service.upiTransaction(data)
    override suspend fun upi2Transaction(data: FieldMapData) = service.upi2Transaction(data)

    override suspend fun upiVerifyPayment(data: FieldMapData) = service.upiVerifyPayment(data)
    override suspend fun r2rTransfer(data: FieldMapData) = service.r2rTransfer(data)
    override suspend fun settlementTransfer(data: FieldMapData) = service.settlementTransfer(data)
    override suspend fun aeps1Transaction(data: FieldMapData) = service.aeps1Transaction(data)
    override suspend fun aeps2Transaction(data: FieldMapData) = service.aeps2Transaction(data)
    override suspend fun aeps3Transaction(data: FieldMapData): AepsTransaction {
        return service.aeps3Transaction(data)
    }

    override suspend fun parentPaymentFundReturn(data: FieldMapData) =
        service.parentPaymentFundReturn(data)

    override suspend fun approveFundRequest(data: FieldMapData) = service.approveFundRequest(data)
    override suspend fun memberFundTransfer(data: FieldMapData) = service.memberFundTransfer(data)
    override suspend fun indoNepalTransfer(data: FieldMapData) = service.indoNepalTransfer(data)


}