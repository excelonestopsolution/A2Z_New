package com.di_md.a2z.data.repository

import com.di_md.a2z.data.service.ReportService
import com.di_md.a2z.util.ents.FieldMapData
import javax.inject.Inject

class ReportRepository @Inject constructor(private val reportService: ReportService) {

    suspend fun ledgerReport(data: FieldMapData) = reportService.ledgerReport(data)
    suspend fun checkStatus(data: FieldMapData) = reportService.checkStatus(data)
    suspend fun makeComplain(data: FieldMapData) = reportService.makeComplain(data)
    suspend fun fetchComplainTypes(data: FieldMapData) = reportService.fetchComplainTypes(data)
    suspend fun downloadLedgerReceiptData(url: String) = reportService.downloadLedgerReceiptData(url)

}