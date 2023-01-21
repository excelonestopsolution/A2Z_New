package com.a2z.app.ui.screen.report.ledger

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.ui.component.AppPagingLazyList
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.ComplaintDialog
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.bottomsheet.BottomSheetComponent
import com.a2z.app.ui.screen.report.component.BaseReportItem
import com.a2z.app.ui.screen.report.component.ReportNavActionButton
import com.a2z.app.ui.screen.report.filter.LedgerReportFilterComponent
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.util.VoidCallback


@Composable
fun LedgerReportScreen() {
    val viewModel: LedgerReportViewModel = hiltViewModel()
    val pagingData = viewModel.fetchLedgerReport.collectAsLazyPagingItems()
    BottomSheetComponent(sheetContent = { closeAction ->
        LedgerReportFilterComponent { searchInput ->
            closeAction.invoke()
            viewModel.invalidateDataSource()
            viewModel.searchInput.value = searchInput.apply {
                this.isRefresh = true
            }
        }
    }) { toggleAction ->
        MainContent(viewModel, pagingData) {
            toggleAction.invoke()
        }
    }

    ComplaintDialog(
        complaintTypes =viewModel.complaintTypeListState,
        dialogState = viewModel.complaintDialogVisibleState
    ){complainTypeId,remark->
        viewModel.onComplainSubmit(complainTypeId,remark)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun MainContent(
    viewModel: LedgerReportViewModel,
    pagingData: LazyPagingItems<LedgerReport>,
    filterAction: VoidCallback
) {


    val rememberPagingData = remember { pagingData }

    Scaffold(backgroundColor = BackgroundColor, topBar = {
        NavTopBar(title = "Ledger Report", actions = {
            ReportNavActionButton {
                filterAction.invoke()
            }
        })
    }) { _ ->
        BaseContent(viewModel) {
            AppPagingLazyList<LedgerReport>(rememberPagingData) {
                BaseReportItem(statusId = it.statusId,
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
                    onPrint = { viewModel.onPrint(it) },
                    onComplaint = {viewModel.onComplain(it)}
                )
            }
        }


    }
}

