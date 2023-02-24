package com.a2z.app.ui.screen.report.fund

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.a2z.app.data.model.report.FundReport
import com.a2z.app.data.model.report.FundReportResponse
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.PagingState
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.util.DateUtil
import com.a2z.app.util.VoidCallback
import com.a2z.app.util.extension.insertDateSeparator
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FundReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : BaseViewModel() {

    var startDate by mutableStateOf(DateUtil.getDate())
    var endDate by mutableStateOf(DateUtil.getDate())

    var pagingState by mutableStateOf<PagingState<FundReport>>(PagingState())

    private val _reportResultResponse = resultStateFlow<FundReportResponse>()

    val filterDialogState = mutableStateOf(false)
    init {
        fetchReport()

        _reportResultResponse.getLatest(
            success = {
                pagingState = pagingState.successState(it.reports!!)
            },
            progress = {
                pagingState = pagingState.loadingState()
            },
            failure = {
                pagingState = pagingState.failureState(it)
            }

        )
    }

    fun fetchReport() {
        val param = hashMapOf(
            "start_date" to startDate.insertDateSeparator(),
            "end_date" to endDate.insertDateSeparator(),
            "page" to pagingState.page.toString()
        )
        val call = suspend { repository.fundReport(param) }
        callApiForStateFlow(_reportResultResponse) { call.invoke() }
    }

    fun onSearch(startDate: String, endDate: String) {
        this.startDate = startDate
        this.endDate = endDate
        pagingState.refresh()
        fetchReport()
    }
}

