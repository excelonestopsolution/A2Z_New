package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.dmt.TransactionDetail
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.dmt.UpiVerifyPayment
import com.a2z.app.data.model.indonepal.INTransferResponse
import com.a2z.app.data.model.utility.BillPaymentResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.util.FieldMapData

interface TransactionRepository {

    suspend fun billPaymentRouteOne(data : HashMap<String,String>): BillPaymentResponse
    suspend fun billPaymentRouteTwo(data : HashMap<String,String>): BillPaymentResponse
    suspend fun rechargeTransaction(data : HashMap<String,String>): RechargeTransactionResponse
    suspend fun wallet1Transaction(data : FieldMapData) : TransactionDetailResponse
    suspend fun wallet2Transaction(data : FieldMapData) : TransactionDetailResponse
    suspend fun wallet3Transaction(data : FieldMapData) : TransactionDetailResponse
    suspend fun dmt3Transaction(data : FieldMapData) : TransactionDetailResponse
    suspend fun upiTransaction(data : FieldMapData) : TransactionDetail
    suspend fun upi2Transaction(data : FieldMapData) : TransactionDetail
    suspend fun upiVerifyPayment(data : FieldMapData) : UpiVerifyPayment
    suspend fun r2rTransfer(data : FieldMapData) : AppResponse
    suspend fun settlementTransfer(data : FieldMapData) : AppResponse
    suspend fun aeps1Transaction(data : FieldMapData) : AepsTransaction
    suspend fun aeps2Transaction(data : FieldMapData) : AepsTransaction
    suspend fun aeps3Transaction(data : FieldMapData) : AepsTransaction
    suspend fun parentPaymentFundReturn(data : FieldMapData) : AppResponse
    suspend fun approveFundRequest(data : FieldMapData) : AppResponse
    suspend fun memberFundTransfer(data : FieldMapData) : AppResponse
    suspend fun indoNepalTransfer(data : FieldMapData) : INTransferResponse
}