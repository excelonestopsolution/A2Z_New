package com.a2z.app.ui.screen.report.fund_transfer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.screen.report.component.BaseReportItem
import com.a2z.app.ui.screen.report.component.ReportNavActionButton
import com.a2z.app.ui.screen.report.filter.FundTransferFilterDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.util.VoidCallback
import com.a2z.app.util.extension.prefixRS


@Composable
fun FundTransferReportReportScreen() {
    val viewModel: FundTransferReportViewModel = hiltViewModel()

    FundTransferFilterDialog(
        showDialogState = viewModel.filterDialogVisibleState,
        users = viewModel.userList,
        onFilter = {
            viewModel.pagingState.refresh()
            viewModel.searchInput = it
            viewModel.fetchReport()
        })

    MainContent(viewModel) {
        viewModel.filterDialogVisibleState.value = true
    }

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun MainContent(
    viewModel: FundTransferReportViewModel,
    filterAction: VoidCallback
) {

    Scaffold(backgroundColor = BackgroundColor, topBar = {
        NavTopBar(title = "Fund Transfer Report", actions = {
            ReportNavActionButton {
                filterAction.invoke()
            }
        })
    }) { _ ->
        BaseContent(viewModel) {
            val pagingState = viewModel.pagingState
            LazyColumn {
                items(viewModel.pagingState.items.size) { index ->

                    val it = pagingState.items[index]

                    pagingState.shouldLoadNext(index) {
                        viewModel.fetchReport()
                    }

                    BaseReportItem(statusId = (it.status_id ?: "0").toDouble().toInt(),
                        leftSideDate = it.date,
                        leftSideId = it.order_id,
                        centerHeading1 = it.user,
                        centerHeading2 = "OP Bal ${it.opening_bal.toString().prefixRS()}",
                        centerHeading3 = "Cl Bal ${it.opening_bal.toString().prefixRS()}",
                        rightAmount = it.credit_amount,
                        rightStatus = it.status,
                        expandListItems = listOf(
                            "Bank Charge" to it.bank_charge,
                            "Wallet" to it.wallet,
                            "Transfer To / From" to it.transfer_to_from,
                            "Firm Name" to it.firm_name,
                            "Ref Id" to it.ref_id,
                            "Bank Ref" to it.bank_ref,
                            "Remark" to it.remark,
                            "Agent Remark" to it.agent_remark,
                            "Description" to it.description,
                        ),
                    )
                }
                item {
                    if (pagingState.items.isEmpty() &&
                        !pagingState.isLoading
                    ) {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyListComponent()
                        }
                    }
                }
                item {
                    val modifier = if (pagingState.page == 1)
                        Modifier.fillParentMaxSize() else Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                    if (pagingState.isLoading && pagingState.exception == null) Box(
                        modifier = modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        AppProgress()
                    }
                }

                item {
                    val modifier = if (pagingState.page == 1)
                        Modifier.fillParentMaxSize() else Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                    if (pagingState.exception != null) Box(
                        modifier = modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error, contentDescription = null,
                                tint = RedColor,
                                modifier = Modifier.size(52.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = pagingState.exception!!.message.toString(),
                                fontSize = 14.sp, color = RedColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}


