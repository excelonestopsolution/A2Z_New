package com.a2z.app.ui.screen.report.matm

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.EmptyListComponent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.screen.report.component.BaseReportItem
import com.a2z.app.ui.screen.report.component.ReportNavActionButton
import com.a2z.app.ui.screen.report.filter.MatmReportFilterDialog
import com.a2z.app.ui.theme.BackgroundColor


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MATMReportScreen() {

    val viewModel: MatmReportViewModel = hiltViewModel()


    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = {
            NavTopBar(
                title = "M-ATM Request Report",
                actions = {
                    ReportNavActionButton() {
                        viewModel.filterDialogState.value = true
                    }
                })
        }) {

        BaseContent(viewModel) {
            ObsComponent(flow = viewModel.reportResultFlow) {
                if (it.status == 1 &&
                    it.data != null &&
                    it.data.isNotEmpty()
                )
                    LazyColumn {
                        items(it.data) {
                            BaseReportItem(
                                statusId = it.status,
                                leftSideDate = it.txn_time,
                                leftSideId = it.service_name,
                                centerHeading1 = it.customer_number,
                                centerHeading2 = it.transaction_type,
                                centerHeading3 = it.card_type,
                                rightAmount = it.transaction_amount,
                                rightStatus = it.status_desc,
                                expandListItems = listOf(
                                    "Order Id" to it.order_id,
                                    "Mode" to it.transaction_mode,
                                    "Card Type" to it.card_type,
                                    "Customer Mobile" to it.customer_number,
                                    "Bank Ref" to it.bank_ref,
                                    "Txn Id" to it.txn_id,
                                    "Credit Charge" to it.credit_charge,
                                    "Debit Charge" to it.debit_charge,
                                    "TDS" to it.tds,
                                    "GST" to it.gst,
                                    "Available Amt." to it.available_amount,
                                    "Message" to it.message,
                                )
                            )
                        }
                    }
                else EmptyListComponent()
            }
        }
    }


    MatmReportFilterDialog(viewModel.filterDialogState) { searchInput ->
        viewModel.searchInput = searchInput
        viewModel.fetchReport()
    }


}
