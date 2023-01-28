package com.a2z.app.data.repository

import androidx.paging.PagingData
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.report.*
import com.a2z.app.util.FieldMapData
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ReportRepository {

    suspend fun ledgerReport( data : HashMap<String, String>) : LedgerReportResponse
    suspend fun checkStatus(data: FieldMapData)  : AppResponse
    suspend fun aepsCheckStatus(data: FieldMapData)  : AppResponse
    suspend fun makeComplain(data: FieldMapData) : AppResponse
    suspend fun fetchComplainTypes(data: FieldMapData) : ComplainTypeListResponse
    suspend fun downloadLedgerReceiptData(url: String) : TransactionDetailResponse
    suspend fun schemeList() : CommissionSchemeListResponse
    suspend fun schemeDetail(data : FieldMapData) : CommissionSchemeDetailResponse
    suspend fun matmRequestReport(data : FieldMapData) : MatmReportResponse
    suspend fun aepsRequestReport(data : FieldMapData) : AepsReportResponse
    suspend fun aepsPrintDetail(url : String) : TransactionDetailResponse
    suspend fun matmPrintDetail(url : String) : TransactionDetailResponse
    suspend fun otherPrintDetail(url : String) : TransactionDetailResponse
    suspend fun fundReport(data : FieldMapData) : FundReportResponse
    suspend fun dtReport(data : FieldMapData) : DTReportResponse
}