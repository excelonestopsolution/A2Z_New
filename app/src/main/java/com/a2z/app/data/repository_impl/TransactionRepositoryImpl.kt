package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.dmt.TransactionDetail
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.utility.BillPaymentResponse
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

    override suspend fun wallet1Transaction(data: FieldMapData) = service.wallet1Transaction(data)
    override suspend fun wallet2Transaction(data: FieldMapData) = service.wallet2Transaction(data)
    override suspend fun wallet3Transaction(data: FieldMapData) = service.wallet3Transaction(data)
    override suspend fun dmt3Transaction(data: FieldMapData) = service.dmt3Transaction(data)
    override suspend fun upiTransaction(data: FieldMapData) = service.upiTransaction(data)


}