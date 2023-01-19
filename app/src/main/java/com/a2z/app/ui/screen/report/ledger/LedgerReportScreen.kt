package com.a2z.app.ui.screen.report.ledger

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.ui.component.AppLazyList
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.screen.report.component.BaseReportItem
import com.a2z.app.ui.theme.BackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LedgerReportScreen() {

    val viewModel: LedgerReportViewModel = hiltViewModel()

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Ledger Report") }
    ) { _ ->

        val pagingData = viewModel.fetchLedgerReport.collectAsLazyPagingItems()

        val rememberPagingData = remember { pagingData }

        BaseContent(viewModel) {

            AppLazyList<LedgerReport>(rememberPagingData) {
                BaseReportItem(
                    statusId = it.statusId,
                    leftSideDate = it.txnTime,
                    leftSideId = it.id.toString(),
                    centerHeading1 = it.number,
                    centerHeading2 = it.serviceName,
                    centerHeading3 = it.senderNumber,
                    rightAmount = it.amount,
                    rightStatus = it.statusDesc,
                    isPrint = it.isPrint,
                    isComplaint = it.isComplain,
                    isCheckStatus = it.isCheckStatus,
                    expandListItems = listOf(
                        "Mobile Number" to it.senderNumber,
                        "Bank Name" to it.bankName,
                        "IFSC Code" to it.ifsc,
                        "Beneficiary Name" to it.beneName,
                        "Reference Id" to it.operatorId,
                        "Transaction Type" to it.txnType,
                        "Opening Balance" to it.opBalance,
                        "Txn Amount" to it.amount,
                        "Credit Amount" to it.credit,
                        "Debit Amount" to it.debit,
                        "TDS Amount" to it.tds,
                        "GST Amount" to it.gst,
                        "Closing Balance" to it.clBalance,
                        "Provider" to it.providerName,
                        "Remark" to it.remark,
                    ),
                    onPrint = {viewModel.onPrint(it)}
                )
            }
        }
    }
}


