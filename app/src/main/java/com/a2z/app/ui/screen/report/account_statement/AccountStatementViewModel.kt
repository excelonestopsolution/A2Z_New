package com.a2z.app.ui.screen.report.account_statement


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.a2z.app.data.model.report.AccountStatement
import com.a2z.app.data.model.report.AccountStatementResponse
import com.a2z.app.data.model.report.PaymentReport
import com.a2z.app.data.model.report.PaymentReportResponse
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.PagingState
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.DateUtil
import com.a2z.app.util.extension.insertDateSeparator
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountStatementViewModel @Inject constructor(
    private val repository: ReportRepository
) : BaseViewModel() {


    var searchInput by mutableStateOf(SearchInput())
    var pagingState by mutableStateOf<PagingState<AccountStatement>>(PagingState())
    private val _reportPagingFlow = resultStateFlow<AccountStatementResponse>()


    init {

        fetchReport()

        _reportPagingFlow.getLatest(
            progress = {
                pagingState = pagingState.loadingState()
                Unit
            },
            success = {
                pagingState = if (it.status == 1)
                    pagingState.successState(it.reports!!)
                else pagingState.failureState(Exception(it.message.toString()))
            },
            failure = {
                pagingState = pagingState.failureState(it)
                Unit
            }
        )


    }


    fun fetchReport() {
        val param = searchInput.run {
            hashMapOf(
                "todate" to this.endDate.insertDateSeparator(),
                "fromdate" to this.startDate.insertDateSeparator(),
            )
        }
        callApiForShareFlow(
            flow = _reportPagingFlow,
            call = { repository.accountStatement(param) }
        )
    }

    fun onSearch(startDate: String, endDate: String) {
        pagingState.refresh()
        searchInput = SearchInput(startDate,endDate)
        fetchReport()
    }


    data class SearchInput(
        val startDate: String = DateUtil.getDate(),
        val endDate: String = DateUtil.getDate(),
    )
}