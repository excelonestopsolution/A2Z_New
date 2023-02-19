package com.a2z.app.ui.screen.report.aeps

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.bottomsheet.BottomSheetComponent
import com.a2z.app.ui.screen.report.component.BaseReportItem
import com.a2z.app.ui.screen.report.component.ReportNavActionButton
import com.a2z.app.ui.theme.BackgroundColor


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AEPSReportScreen() {

    val viewModel: AEPSReportViewModel = hiltViewModel()


    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = {
            NavTopBar(title = "AEPS Report",
                actions = {
                    ReportNavActionButton {
                     viewModel.filterDialogState.value = true
                    }
                })
        }) {_->

        BaseContent(viewModel) {
            ObsComponent(flow = viewModel.reportResultFlow) {response->
                if (response.status == 1 && response.data!!.isNotEmpty()) LazyColumn {
                    items(response.data) {
                        BaseReportItem(
                            statusId = it.status_id,
                            leftSideDate = it.created_at,
                            leftSideId = it.api_name,
                            centerHeading1 = it.number,
                            centerHeading2 = it.txn_type,
                            centerHeading3 = it.bank_name,
                            rightAmount = it.amount,
                            rightStatus = it.status,
                            isPrint = it.is_print ?: false,
                            isCheckStatus = it.is_check_status ?: false,
                            isComplaint = it.is_complain ?: false,
                            onComplaint = { viewModel.onComplaint(it) },
                            expandListItems = listOf(
                                "Order Id" to it.order_id,
                                "Bank Ref" to it.bank_ref,
                                "Mobile Number" to it.customer_number,
                                "Mode" to it.mode,
                                "Ak No." to it.ackno,
                                "Opening Bal." to it.opening_balance,
                                "Credit Charge" to it.credit_charge,
                                "Debit Charge" to it.debit_charge,
                                "TDS" to it.tds,
                                "Total Balance" to it.total_balance,
                                "Message" to it.fail_msg,
                            ),
                            onCheckStatus = {viewModel.onCheckStatus(it)},
                            onPrint = {viewModel.onPrint(it)}
                        )
                    }
                }
                else EmptyListComponent()
            }
        }
    }

    AepsReportFilterDialog (viewModel.filterDialogState){ searchInput ->
        viewModel.searchInput = searchInput
        viewModel.fetchReport()
    }



    ComplaintDialog(
        complaintTypes = viewModel.complaintTypeListState,
        dialogState = viewModel.complaintDialogVisibleState
    ) { complainTypeId, remark ->
        viewModel.onComplainSubmit(complainTypeId, remark)
    }
}