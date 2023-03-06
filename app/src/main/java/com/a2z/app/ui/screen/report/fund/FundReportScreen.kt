package com.a2z.app.ui.screen.report.fund

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.screen.report.component.BaseReportItem
import com.a2z.app.ui.screen.report.component.ReportNavActionButton
import com.a2z.app.ui.screen.report.filter.ReportDateFilterDialog
import com.a2z.app.ui.theme.BackgroundColor

@Composable
fun FundReportScreen() {
    val viewModel: FundReportViewModel = hiltViewModel()


    Scaffold(backgroundColor = BackgroundColor, topBar = {
        NavTopBar(title = "Fund Report", actions = {
            ReportNavActionButton {
                viewModel.filterDialogState.value = true
            }
        })
    }) {
        BaseContent(viewModel) {
            val pagingState = viewModel.pagingState
            LazyColumn {
                items(viewModel.pagingState.items.size) { index ->

                    val it = pagingState.items[index]

                    pagingState.shouldLoadNext(index) {
                        viewModel.fetchReport()
                    }

                    BaseReportItem(statusId = it.status_id,
                        leftSideDate = it.created_at,
                        leftSideId = it.id.toString(),
                        centerHeading1 = it.request_for,
                        centerHeading2 = it.payment_mode,
                        centerHeading3 = it.account_number,
                        rightAmount = it.wallet_amount,
                        rightStatus = it.status,
                        expandListItems = listOf(
                            "Approval Remark" to it.approval_remark,
                            "Request Remark" to it.request_remark,
                            "Update Remark" to it.update_remark,
                            "Branch Name" to it.branch_name,
                            "Deposit Date" to it.deposit_date,
                            "Bank Ref" to it.bank_ref,
                            "Bank Name" to it.bank_name,
                            "Username" to it.username,
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
                    if (pagingState.isLoading) Box(
                        modifier = modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        AppProgress()
                    }
                }
            }
        }
    }


    ReportDateFilterDialog(viewModel.filterDialogState) { startDate, endDate ->
        viewModel.onSearch(startDate,endDate)
    }
}
