package com.a2z.app.ui.screen.report.ledger

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.a2z.app.R
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.report.ComplainType
import com.a2z.app.data.model.report.ComplainTypeListResponse
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.data.model.utility.BillPaymentResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.util.AppConstant
import com.a2z.app.util.DateUtil
import com.a2z.app.util.extension.insertDateSeparator
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LedgerReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : BaseViewModel() {


    val searchInput = mutableStateOf(SearchInput())

    private lateinit var pagingSource: LedgerPagingSource

    private val _downloadReportResultFlow = resultShareFlow<TransactionDetailResponse>()
    private val _complainTypeListResultFlow = resultShareFlow<ComplainTypeListResponse>()
    private var complainTransactionId = ""
    private val _complainResultFlow = resultShareFlow<AppResponse>()

    val complaintDialogVisibleState = mutableStateOf(value = false)
    val fetchLedgerReport = Pager(
        config = PagingConfig(
            initialLoadSize = 1,
            pageSize = 30,
        ),
        pagingSourceFactory = {
            LedgerPagingSource(
                repository,
                searchInput.value.run {
                    hashMapOf(
                        "todate" to this.endDate.insertDateSeparator(),
                        "fromdate" to this.startDate.insertDateSeparator(),
                        "searchType" to this.criteria,
                        "number" to this.input,
                        "status_id" to this.status,
                        "product" to this.product,
                        "isRefresh" to if (this.isRefresh) "1" else "0"
                    )
                }
            ).also {
                pagingSource = it
            }
        }
    ).flow

    fun invalidateDataSource() {
        pagingSource.invalidate()
        pagingSource.keyReuseSupported

    }

    val complaintTypeListState = mutableListOf<ComplainType>()

    init {
        viewModelScope.launch {
            _downloadReportResultFlow.getLatest {
                if (it.status == 1 && it.data != null) {
                    val data = it.data!!
                    when (data.slipType) {
                        "TRANSACTION" -> {
                            navigateTo(NavScreen.DMTTxnScreen.passArgs(
                                response = data.apply { this.isTransaction = false }
                            ))
                        }
                        "UPI_PAYMENT" -> {
                            navigateTo(NavScreen.UPITxnScreen.passArgs(
                                response = data.apply { this.isTransaction = false }
                            ))
                        }
                        "BILLPAYMENT",
                        "FASTTAG" -> {
                            navigateTo(
                                NavScreen.BillPaymentTxnScreen.passArgs(
                                    response = BillPaymentResponse(
                                        status = data.status ?: 0,
                                        message = data.message.orEmpty(),
                                        payId = data.reportId,
                                        dateTime = data.txnTime,
                                        operatorRef = data.bankRef,
                                        statusDescription = data.statusDesc,
                                        billName = data.senderName,
                                        providerName = data.provider,
                                        amount = data.amount,
                                        numberTitle = "Number",
                                        type = "",
                                        recordId = data.reportId,
                                        providerIcon = R.drawable.ic_launcher_electricity,
                                        number = data.number,
                                        serviceName = data.serviceName,
                                        isTransaction = false
                                    )
                                )
                            )

                        }
                        "RECHARGE" -> {
                            navigateTo(
                                NavScreen.RechargeTxnScreen.passArgs(
                                    response = RechargeTransactionResponse(
                                        status = data.status ?: 0,
                                        message = data.message.orEmpty(),
                                        payId = data.reportId,
                                        dateTime = data.txnTime,
                                        operatorRef = data.bankRef,
                                        statusDescription = data.statusDesc,
                                        providerName = data.provider,
                                        amount = data.amount,
                                        numberTitle = "Mobile Number",
                                        recordId = data.reportId,
                                        providerIcon = R.drawable.ic_launcher_mobile,
                                        number = data.number,
                                        serviceName = data.serviceName,
                                        isTransaction = false
                                    )
                                )
                            )
                        }
                        "AEPS" -> {

                        }
                        "MATM" -> {

                        }
                    }
                } else alertDialog(it.message.toString())
            }
        }

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


    fun onPrint(report: LedgerReport) {
        val url = AppConstant.BASE_URL + "slip/new/${report.id.toString()}"
        callApiForShareFlow(
            flow = _downloadReportResultFlow,
            call = { repository.downloadLedgerReceiptData(url) }
        )
    }

    fun onCheckStatus(report: LedgerReport) {


    }

    fun onComplain(report: LedgerReport) {
        complainTransactionId = report.id.toString()
        callApiForShareFlow(_complainTypeListResultFlow) {
            repository.fetchComplainTypes(
                hashMapOf(
                    "transaction_type_id" to report.transactionTypeId.toString(),
                )
            )
        }
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

    data class SearchInput(
        val startDate: String = DateUtil.getDate(),
        val endDate: String = DateUtil.getDate(),
        val status: String = "",
        val product: String = "",
        val criteria: String = "",
        val input: String = "",
        var isRefresh: Boolean = false
    )
}