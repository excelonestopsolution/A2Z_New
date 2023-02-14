package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.report.CommissionSchemeListResponse
import com.a2z.app.data.model.report.LedgerReportResponse
import com.a2z.app.data.network.ReportService
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.util.FieldMapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(private val service: ReportService) :
    ReportRepository {
    override suspend fun ledgerReport(data: HashMap<String, String>): LedgerReportResponse {
        return service.ledgerReport(data)
    }

    override suspend fun checkStatus(data: FieldMapData) = service.checkStatus(data)
    override suspend fun aepsCheckStatus(data: FieldMapData) = service.aepsCheckStatus(data)

    override suspend fun makeComplain(data: FieldMapData) = service.makeComplain(data)
    override suspend fun complaintRequestView(data: FieldMapData)= service.complaintRequestView(data)

    override suspend fun fetchComplainTypes(data: FieldMapData) = service.fetchComplainTypes(data)

    override suspend fun downloadLedgerReceiptData(url: String) =
        service.downloadLedgerReceiptData(url)

    override suspend fun schemeList(): CommissionSchemeListResponse = service.schemeList()

    override suspend fun schemeDetail(data: FieldMapData) = service.schemeDetail(data)
    override suspend fun matmRequestReport(data: FieldMapData) = service.matmRequestReport(data)
    override suspend fun aepsRequestReport(data: FieldMapData) = service.aepsRequestReport(data)
    override suspend fun aepsPrintDetail(url : String) = service.aepsPrintDetail(url)
    override suspend fun matmPrintDetail(url: String) = service.matmPrintDetail(url)
    override suspend fun otherPrintDetail(url: String) = service.otherPrintDetail(url)

    override suspend fun fundReport(data: FieldMapData) = service.fundReport(data)

    override suspend fun dtReport(data: FieldMapData) = service.dtReport(data)
    override suspend fun pgReport(data: FieldMapData) = service.pgReport(data)
    override suspend fun agentRequestView(data: FieldMapData) = service.agentRequestView(data)
    override suspend fun fundTransfer(data: FieldMapData) = service.fundTransfer(data)
    override suspend fun paymentReport(data: FieldMapData) = service.paymentReport(data)
    override suspend fun accountStatement(data: FieldMapData) = service.accountStatement(data)

}