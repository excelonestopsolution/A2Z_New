package com.a2z.app.data.repository_impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.report.CommissionSchemeDetailResponse
import com.a2z.app.data.model.report.CommissionSchemeListResponse
import com.a2z.app.data.model.report.ComplainTypeListResponse
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.data.network.ReportService
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.ui.screen.report.ledger.LedgerPagingSource
import com.a2z.app.util.FieldMapData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(private val service: ReportService) :
    ReportRepository {
    override fun ledgerReport(data: HashMap<String, String>): Flow<PagingData<LedgerReport>> {

        return Pager(
            config = PagingConfig(
                initialLoadSize = 1,
                pageSize = 30,
            ),
            pagingSourceFactory = {
                LedgerPagingSource(
                    service,
                    data
                )
            }
        ).flow
    }

    override suspend fun checkStatus(data: FieldMapData) = service.checkStatus(data)

    override suspend fun makeComplain(data: FieldMapData) = service.makeComplain(data)
    override suspend fun fetchComplainTypes(data: FieldMapData) = service.fetchComplainTypes(data)

    override suspend fun downloadLedgerReceiptData(url: String) =
        service.downloadLedgerReceiptData(url)

    override suspend fun schemeList(): CommissionSchemeListResponse = service.schemeList()

    override suspend fun schemeDetail(data: FieldMapData) = service.schemeDetail(data)


}