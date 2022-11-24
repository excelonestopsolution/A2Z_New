package com.a2z.app.data.repository

import com.a2z.app.data.model.utility.BillPaymentResponse

interface TransactionRepository {

    suspend fun billPaymentRouteOne(data : HashMap<String,String>): BillPaymentResponse
    suspend fun billPaymentRouteTwo(data : HashMap<String,String>): BillPaymentResponse
}