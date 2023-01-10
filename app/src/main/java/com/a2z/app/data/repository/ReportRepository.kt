package com.a2z.app.data.repository

import androidx.paging.PagingData
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.report.CommissionSchemeDetailResponse
import com.a2z.app.data.model.report.CommissionSchemeListResponse
import com.a2z.app.data.model.report.ComplainTypeListResponse
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.util.FieldMapData
import kotlinx.coroutines.flow.Flow
import retrofit2.http.QueryMap

interface ReportRepository {

    fun ledgerReport(@QueryMap data : HashMap<String, String>) : Flow<PagingData<LedgerReport>>
    suspend fun checkStatus(data: FieldMapData)  : AppResponse
    suspend fun makeComplain(data: FieldMapData) : AppResponse
    suspend fun fetchComplainTypes(data: FieldMapData) : ComplainTypeListResponse
    suspend fun downloadLedgerReceiptData(url: String) : TransactionDetailResponse
    suspend fun schemeList() : CommissionSchemeListResponse
    suspend fun schemeDetail(data : FieldMapData) : CommissionSchemeDetailResponse
}