package com.a2z.app.ui.screen.report.aeps

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.report.*
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.util.DateUtil
import com.a2z.app.util.extension.insertDateSeparator
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AEPSReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : BaseViewModel() {

    var searchInput = SearchInput()

    val reportResultFlow = resultStateFlow<AepsReportResponse>()
    val complaintTypeListState = mutableListOf<ComplainType>()
    val complaintDialogVisibleState = mutableStateOf(value = false)
    private val _complainTypeListResultFlow = resultShareFlow<ComplainTypeListResponse>()
    private var complainTransactionId = ""
    private val _complainResultFlow = resultShareFlow<AppResponse>()

    init {
        fetchReport()

        viewModelScope.launch {
            _complainTypeListResultFlow.getLatest {
                if (it.status == 1) {
                    complaintTypeListState.addAll(it.data!!)
                    complaintDialogVisibleState.value = true
                } else alertDialog(it.message)
            }
        }

        viewModelScope.launch {
            _complainResultFlow.getLatest {
                if(it.status == 1) successDialog(it.message)
                else alertDialog(it.message)
            }
        }
    }

    fun fetchReport() {
        callApiForStateFlow(
            flow = reportResultFlow,
            call = { repository.aepsRequestReport(hashMapOf(
                "fromdate" to searchInput.startDate.insertDateSeparator(),
                "todate" to searchInput.endDate.insertDateSeparator(),
                "search" to searchInput.input,
                "search_type" to searchInput.searchType,
                "status_id" to searchInput.status,
                "txn_type" to searchInput.txnType,
            )) }
        )
    }

    fun onComplainSubmit(complainTypeId: String, remark: String) {

        callApiForShareFlow(
            _complainResultFlow
        ) {
            repository.makeComplain(
                hashMapOf(
                    "transaction_id" to complainTransactionId,
                    "issueType" to complainTypeId,
                    "remark" to remark,
                )
            )
        }
    }


    fun onComplaint(it: AepsReport) {
        complainTransactionId = it.order_id.toString()
        callApiForShareFlow(_complainTypeListResultFlow) {
            repository.fetchComplainTypes(
                hashMapOf(
                    "transaction_type_id" to it.transaction_type_id.toString(),
                )
            )
        }
    }

    data class SearchInput(
        val startDate: String = DateUtil.getDate(),
        val endDate: String = DateUtil.getDate(),
        val status: String = "",
        val searchType: String = "",
        val txnType: String = "",
        val input: String = "",
    )
}