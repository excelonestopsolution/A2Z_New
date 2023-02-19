package com.a2z.app.ui.screen.report.matm

import androidx.compose.runtime.mutableStateOf
import com.a2z.app.data.model.report.MatmReportResponse
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.util.DateUtil
import com.a2z.app.util.extension.insertDateSeparator
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatmReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : BaseViewModel() {

    val reportResultFlow = resultStateFlow<MatmReportResponse>()

    var searchInput = SearchInput()

    val filterDialogState = mutableStateOf(false)

    init {
        fetchReport()
    }

    fun fetchReport() {

        val param = hashMapOf(
            "fromdate" to searchInput.startDate.insertDateSeparator(),
            "todate" to searchInput.endDate.insertDateSeparator(),
            "txn_type" to searchInput.transactionType,
            "status_id" to searchInput.status,
            "searchType" to searchInput.inputMode,
            "number" to searchInput.input,
        )

        callApiForStateFlow(
            flow = reportResultFlow,
            call = {
                repository.matmRequestReport(param)
            }
        )
    }


    data class SearchInput(
        val startDate: String = DateUtil.getDate(),
        val endDate: String = DateUtil.getDate(),
        val status: String = "",
        val transactionType: String = "",
        val inputMode: String = "",
        val input: String = "",
        var isRefresh: Boolean = false
    )
}