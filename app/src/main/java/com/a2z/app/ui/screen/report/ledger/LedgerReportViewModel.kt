package com.a2z.app.ui.screen.report.ledger

import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.R
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.data.model.utility.BillPaymentResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.AppConstant
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LedgerReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : BaseViewModel() {


    val param =  hashMapOf(
        "fromdate" to "01-01-2022",
        "todate" to "30-01-2023",
    )

    private val _downloadReportResultFlow = resultShareFlow<TransactionDetailResponse>()

    val fetchLedgerReport = repository.ledgerReport(param)

    init {
        viewModelScope.launch {
            _downloadReportResultFlow.getLatest {
                if(it.status == 1 && it.data != null){
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
                            navigateTo(NavScreen.BillPaymentTxnScreen.passArgs(
                                response = BillPaymentResponse(
                                    status = data.status ?: 0,
                                    message =  data.message.orEmpty(),
                                    payId = data.reportId,
                                    dateTime = data.txnTime,
                                    operatorRef =data.bankRef,
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
                                    isTransaction =  false
                                )
                            ))

                        }
                        "RECHARGE" ->{
                            navigateTo(NavScreen.RechargeTxnScreen.passArgs(
                                response = RechargeTransactionResponse(
                                    status = data.status ?: 0,
                                    message =  data.message.orEmpty(),
                                    payId = data.reportId,
                                    dateTime = data.txnTime,
                                    operatorRef =data.bankRef,
                                    statusDescription = data.statusDesc,
                                    providerName = data.provider,
                                    amount = data.amount,
                                    numberTitle = "Mobile Number",
                                    recordId = data.reportId,
                                    providerIcon = R.drawable.ic_launcher_mobile,
                                    number = data.number,
                                    serviceName = data.serviceName,
                                    isTransaction =  false
                                )
                            ))
                        }
                        "AEPS" -> {

                        }
                        "MATM"->{

                        }
                    }
                }
                else alertDialog(it.message.toString())
            }
        }
    }


    fun onPrint(report: LedgerReport) {
        val url = AppConstant.BASE_URL+"slip/new/${report.id.toString()}"
        callApiForShareFlow(
            flow = _downloadReportResultFlow,
            call = {repository.downloadLedgerReceiptData(url)}
        )
    }

    fun onCheckStatus(report: LedgerReport) {


    }

    fun onComplain(report: LedgerReport) {


    }
}