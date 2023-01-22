package com.a2z.app.ui.screen.report.ledger

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.a2z.app.R
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.matm.MatmTransactionResponse
import com.a2z.app.data.model.report.ComplainType
import com.a2z.app.data.model.report.ComplainTypeListResponse
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.data.model.report.LedgerReportResponse
import com.a2z.app.data.model.utility.BillPaymentResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.PagingState
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.util.*
import com.a2z.app.util.extension.insertDateSeparator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LedgerReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : BaseViewModel() {


    var searchInput by mutableStateOf(SearchInput())


    var pagingState by mutableStateOf<PagingState<LedgerReport>>(PagingState())

    private val _downloadReportResultFlow = resultShareFlow<TransactionDetailResponse>()
    private val _complainTypeListResultFlow = resultShareFlow<ComplainTypeListResponse>()
    private var complainTransactionId = ""
    private val _complainResultFlow = resultShareFlow<AppResponse>()
    private val _checkStatusResultFlow = resultShareFlow<AppResponse>()

    val complaintDialogVisibleState = mutableStateOf(value = false)

    val complaintTypeListState = mutableListOf<ComplainType>()

    private val _reportPagingFlow = resultStateFlow<LedgerReportResponse>()


    init {

        fetchReport()

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
                            navigateTo(
                                NavScreen.AEPSTxnScreen.passArgs(
                                    response = it.data.apply { this!!.isTransaction = false }!!
                                )
                            )
                        }
                        "MATM" -> {

                            val mData = it.data!!.run {
                                MatmTransactionResponse(
                                    status = this.status,
                                    statusDesc =this.statusDesc,
                                    message = this.message,
                                    serviceName = this.serviceName,
                                    customerNumber =this.senderNumber,
                                    txnId = "",
                                    orderId = this.reportId,
                                    recordId = this.reportId,
                                    transactionType = this.txnType,
                                    cardNumber = this.number,
                                    cardType = this.cardType,
                                    creditDebitCardType = this.bankName,
                                    availableAmount = this.availableBalance,
                                    transactionAmount = this.amount,
                                    transactionMode = "",
                                    bankRef = this.bankRef,
                                    txnTime =this.txnTime,
                                    shopName =this.outletName,
                                    retailerNumber = this.outletNumber,
                                    retailerName = "",
                                    outletAddress =this.outletAddress,
                                    isTransaction = false
                                )
                            }
                            NavScreen.MATMTxnScreen.passArgs(
                                response = mData
                            )

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
                if (it.status == 1) successDialog(it.message)
                else alertDialog(it.message)
            }
        }

        _reportPagingFlow.getLatest(
            progress = {
                pagingState = pagingState.loadingState()
                Unit
            },
            success = {
                pagingState = pagingState.successState(it.reports!!, it.nextPage)
            },
            failure = {
                pagingState = pagingState.failureState(it)
                Unit
            }
        )

        _checkStatusResultFlow.getLatest {
            when (it.status) {
                1 -> successDialog(it.message)
                2 -> failureDialog(it.message)
                3 -> pendingDialog(it.message)
                else -> alertDialog(it.message)
            }
        }

    }


    fun fetchReport() {
        val param = searchInput.run {
            hashMapOf(
                "todate" to this.endDate.insertDateSeparator(),
                "fromdate" to "01-01-2021",
                "searchType" to this.criteria,
                "number" to this.input,
                "status_id" to this.status,
                "product" to this.product,
                "page" to pagingState.page.toString(),
            )
        }
        callApiForShareFlow(
            flow = _reportPagingFlow,
            call = { repository.ledgerReport(param) }
        )
    }


    fun onPrint(report: LedgerReport) {
        val url = AppConstant.BASE_URL + "slip/new/${report.id.toString()}"
        callApiForShareFlow(
            flow = _downloadReportResultFlow,
            call = { repository.downloadLedgerReceiptData(url) }
        )
    }

    fun onCheckStatus(report: LedgerReport) {
        val param = hashMapOf("id" to report.id.toString())
        callApiForShareFlow(_checkStatusResultFlow) { repository.checkStatus(param) }
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
    )
}