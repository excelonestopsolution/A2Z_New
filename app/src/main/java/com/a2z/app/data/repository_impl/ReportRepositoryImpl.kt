package com.a2z.app.data.repository_impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.data.model.report.LedgerReportResponse
import com.a2z.app.data.model.utility.BillPaymentResponse
import com.a2z.app.data.network.ReportService
import com.a2z.app.data.network.TransactionService
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.data.repository.TransactionRepository
import com.a2z.app.ui.screen.report.ledger.LedgerPagingSource
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


}