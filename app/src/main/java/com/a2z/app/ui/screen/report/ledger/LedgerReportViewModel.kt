package com.a2z.app.ui.screen.report.ledger

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.a2z.app.data.model.fund.FundRequestBankListResponse
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.data.model.report.LedgerReportResponse
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForFlow
import com.a2z.app.ui.util.resource.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LedgerReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : BaseViewModel() {


    val param =  hashMapOf(
        "fromdate" to "01-01-2022",
        "todate" to "01-01-2023",
    )

    val fetchLedgerReport = repository.ledgerReport(param)
}