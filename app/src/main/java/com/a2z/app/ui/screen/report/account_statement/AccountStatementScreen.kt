package com.a2z.app.ui.screen.report.account_statement

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
import com.a2z.app.ui.screen.report.component.BaseReportItem
import com.a2z.app.ui.screen.report.component.ReportNavActionButton
import com.a2z.app.ui.screen.report.filter.ReportDateFilterDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.util.extension.prefixRS


@Composable
fun AccountStatementReportScreen() {
    val viewModel: AccountStatementViewModel = hiltViewModel()


    Scaffold(backgroundColor = BackgroundColor, topBar = {
        NavTopBar(title = "Account Statement", actions = {
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

                    BaseReportItem(statusId = it.statusId!!.toDouble().toInt(),
                        leftSideDate = it.created_at,
                        leftSideId = it.id.toString(),
                        centerHeading1 = it.product,
                        centerHeading2 = "Cl "+it.total_bal?.prefixRS(),
                        centerHeading3 = it.number,
                        rightAmount = it.amount,
                        rightStatus = it.status,
                        expandListItems = listOf(
                            "Mode" to it.mode,
                            "Mobile Number" to it.mobile_number,
                            "Name" to it.name,
                            "Bank Name" to it.bank_name,
                            "Txn Id" to it.txn_id,
                            "Description" to it.description,
                            "Opening Bal" to it.opening_bal?.prefixRS(),
                            "Credit Charge" to it.credit_charge?.prefixRS(),
                            "Debit Charge" to it.debit_charge?.prefixRS(),
                            "Remark" to it.remark,
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

    ReportDateFilterDialog(viewModel.filterDialogState) { startDate, endDate ->
        viewModel.onSearch(startDate,endDate)
    }
}



