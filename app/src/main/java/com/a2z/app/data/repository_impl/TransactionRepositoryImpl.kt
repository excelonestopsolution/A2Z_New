package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.utility.BillPaymentResponse
import com.a2z.app.data.network.TransactionService
import com.a2z.app.data.repository.TransactionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(private val service: TransactionService) :
    TransactionRepository {
    override suspend fun billPaymentRouteOne(data : HashMap<String,String>): BillPaymentResponse {
        return service.billPaymentRouteOne(data)
    }

    override suspend fun billPaymentRouteTwo(data : HashMap<String,String>): BillPaymentResponse {
        return service.billPaymentRouteTwo(data)
    }

}