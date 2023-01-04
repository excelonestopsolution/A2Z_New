package com.a2z.app.ui.screen.report.ledger

import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.ui.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LedgerReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : BaseViewModel() {


    val param =  hashMapOf(
        "fromdate" to "04-01-2022",
        "todate" to "01-01-2023",
    )

    val fetchLedgerReport = repository.ledgerReport(param)
}