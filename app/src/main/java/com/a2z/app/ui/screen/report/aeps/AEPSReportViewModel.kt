package com.a2z.app.ui.screen.report.aeps

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.report.*
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.nav.NavScreen
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
    private val _checkStatusResultFlow = resultShareFlow<AppResponse>()
    private val _printDetailResultFlow = resultShareFlow<TransactionDetailResponse>()


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
                when (it.status) {
                    1 -> successDialog(it.message)
                    2 -> failureDialog(it.message)
                    3 -> pendingDialog(it.message)
                    else -> alertDialog(it.message)
                }
            }
        }

        _checkStatusResultFlow.getLatest {
            if (it.status == 1) successDialog(it.message)
            else failureDialog(it.message)
        }

        _printDetailResultFlow.getLatest {
            if (it.status == 1) {
                navigateTo(
                    NavScreen.AEPSTxnScreen.passArgs(
                        response = it.data!!.run {
                            AepsTransaction(
                                status = this.status ?: 3,
                                message = this.message.toString(),
                                record_id = this.reportId,
                                status_desc = this.statusDesc,
                                service_name = this.serviceName,
                                order_id = this.reportId,
                                transaction_type = this.txnType,
                                aadhaar_number = this.number,
                                available_amount = this.availableBalance,
                                transaction_amount = this.amount,
                                bank_ref = this.bankRef,
                                txn_time = this.txnTime,
                                bank_name = this.bankName,
                                customer_number = this.senderNumber,
                                shop_name = this.outletName,
                                retailer_number = this.outletNumber,
                                statement = this.miniStatement,
                                pay_type = "",
                                txn_id = "",
                                isTransaction = false
                            )
                        }
                    )
                )
            } else alertDialog(it.message.toString())
        }
    }

    fun fetchReport() {
        callApiForStateFlow(
            flow = reportResultFlow,
            call = {
                repository.aepsRequestReport(
                    hashMapOf(
                        "fromdate" to searchInput.startDate.insertDateSeparator(),
                        "todate" to searchInput.endDate.insertDateSeparator(),
                        "search" to searchInput.input,
                        "search_type" to searchInput.searchType,
                        "status_id" to searchInput.status,
                        "txn_type" to searchInput.txnType,
                    )
                )
            }
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

    fun onCheckStatus(report: AepsReport) {
        val param = hashMapOf("id" to report.id.toString())
        callApiForShareFlow(_checkStatusResultFlow) { repository.aepsCheckStatus(param) }
    }

    fun onPrint(it: AepsReport) {
        val url = "aeps/report/slip/new/${it.id.toString()}"
        callApiForShareFlow(_printDetailResultFlow) { repository.aepsPrintDetail(url) }
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