package com.a2z.app.ui.screen.report.pg

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
import com.a2z.app.util.extension.notAvailable

@Composable
fun PGReportScreen() {
    val viewModel: PGReportViewModel = hiltViewModel()



    Scaffold(backgroundColor = BackgroundColor, topBar = {
        NavTopBar(title = "PG Report", actions = {
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

                    BaseReportItem(statusId = it.status,
                        leftSideDate = it.txn_time,
                        leftSideId = null,
                        centerHeading1 = it.orderId.notAvailable(),
                        centerHeading2 = it.bank_ref.notAvailable(),
                        centerHeading3 = it.customer_mobile,
                        rightAmount = it.amount,
                        rightStatus = it.status_desc,
                        expandListItems = listOf(
                            "Operator Id" to it.operator_id,
                            "Report Id" to it.report_id,
                            "Customer Mobile" to it.customer_mobile,
                            "Card Number" to it.card_number,
                            "Card Type" to it.Card_type,
                            "Credit" to it.credit,
                            "Debit" to it.debit,
                            "GST" to it.gst,
                            "Message" to it.message,
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

