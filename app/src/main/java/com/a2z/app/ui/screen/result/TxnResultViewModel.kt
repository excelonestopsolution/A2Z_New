package com.a2z.app.ui.screen.result

import androidx.compose.runtime.mutableStateOf
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TxnResultViewModel @Inject constructor(
    private val repository: ReportRepository
) : BaseViewModel() {

    lateinit var response: TransactionDetailResponse
    var receiptType = TxnResultPrintReceiptType.OTHER
    var recordId : String = ""

    val resultFlow = resultShareFlow<TransactionDetailResponse>()

    val commissionAmountDialog = mutableStateOf(false)
    fun downloadReceiptData() {

        val call = suspend {
            when (receiptType) {
                TxnResultPrintReceiptType.AEPS -> repository.aepsPrintDetail("aeps/report/slip/new/$recordId")
                TxnResultPrintReceiptType.MATM -> repository.matmPrintDetail("matm/report-slip/$recordId")
                TxnResultPrintReceiptType.OTHER -> repository.otherPrintDetail("slip/new/$recordId")
            }
        }

        callApiForShareFlow(
            flow = resultFlow,
            call = call
        )

    }
}

enum class TxnResultPrintReceiptType {
    AEPS, MATM, OTHER
}