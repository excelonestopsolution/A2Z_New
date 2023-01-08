package com.a2z.app.data.network

import com.a2z.app.data.model.report.LedgerReportResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ReportService {

    @GET("report/ledger")
    suspend fun ledgerReport(@QueryMap data : Map<String, String>) : LedgerReportResponse



}
