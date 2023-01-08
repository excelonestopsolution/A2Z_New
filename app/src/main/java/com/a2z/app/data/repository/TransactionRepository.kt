package com.a2z.app.data.repository

import com.a2z.app.data.model.dmt.TransactionDetail
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.utility.BillPaymentResponse
import com.a2z.app.util.FieldMapData

interface TransactionRepository {

    suspend fun billPaymentRouteOne(data : HashMap<String,String>): BillPaymentResponse
    suspend fun billPaymentRouteTwo(data : HashMap<String,String>): BillPaymentResponse
    suspend fun wallet1Transaction(data : FieldMapData) : TransactionDetailResponse
    suspend fun wallet2Transaction(data : FieldMapData) : TransactionDetailResponse
    suspend fun wallet3Transaction(data : FieldMapData) : TransactionDetailResponse
    suspend fun dmt3Transaction(data : FieldMapData) : TransactionDetailResponse
    suspend fun upiTransaction(data : FieldMapData) : TransactionDetail
}