package com.a2z.app.data.repository

import androidx.paging.PagingData
import com.a2z.app.data.model.report.LedgerReport
import kotlinx.coroutines.flow.Flow
import retrofit2.http.QueryMap

interface ReportRepository {
    fun ledgerReport(@QueryMap data : HashMap<String, String>) : Flow<PagingData<LedgerReport>>

}