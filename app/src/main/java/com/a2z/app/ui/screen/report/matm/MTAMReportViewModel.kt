package com.a2z.app.ui.screen.report.matm

import com.a2z.app.data.model.report.AepsReportResponse
import com.a2z.app.data.model.report.MatmReportResponse
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MTAMReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : BaseViewModel() {

    val reportResultFlow = resultStateFlow<MatmReportResponse>()

    init {
        fetchReport()
    }

    private fun fetchReport() {
        callApiForStateFlow(
            flow = reportResultFlow,
            call = { repository.matmRequestReport(hashMapOf(
                "fromdate" to "01-01-2022",
                "todate" to "01-01-2023",
            )) }
        )
    }
}