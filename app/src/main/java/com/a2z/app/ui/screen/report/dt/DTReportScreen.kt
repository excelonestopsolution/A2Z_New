package com.a2z.app.ui.screen.report.dt

import android.annotation.SuppressLint
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
import com.a2z.app.ui.component.bottomsheet.BottomSheetComponent
import com.a2z.app.ui.screen.report.ReportUtil
import com.a2z.app.ui.screen.report.component.BaseReportItem
import com.a2z.app.ui.screen.report.component.ReportNavActionButton
import com.a2z.app.ui.screen.report.filter.ReportDateFilterDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.util.VoidCallback

@Composable
fun DTReportScreen() {
    val viewModel: DTReportViewModel = hiltViewModel()



    Scaffold(backgroundColor = BackgroundColor, topBar = {
        NavTopBar(title = "DT Report", actions = {
            ReportNavActionButton {
                viewModel.filterDialogState.value = false
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

                    BaseReportItem(
                        statusId = ReportUtil.getStatusId(it.status),
                        leftSideDate = it.created_at,
                        leftSideId = it.order_id,
                        centerHeading1 = it.firm_name,
                        centerHeading2 = it.number,
                        centerHeading3 = it.opening_bal,
                        rightAmount = it.wallet,
                        rightStatus = it.status,
                        expandListItems = listOf(
                            "Bank Charge" to it.bank_charge,
                            "Credit Amount" to it.credit_amount,
                            "Ref Id" to it.ref_id,
                            "Branch Name" to it.remark,
                            "Transfer To" to it.transfer_to_from,
                            "Opening Bal." to it.opening_bal,
                            "Closing Bal" to it.closing_bal,
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
        viewModel.onSearch(startDate, endDate)
    }

}
