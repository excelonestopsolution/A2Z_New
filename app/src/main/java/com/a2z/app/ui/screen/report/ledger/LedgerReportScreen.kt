package com.a2z.app.ui.screen.report.ledger

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.screen.report.component.BaseReportItem
import com.a2z.app.ui.screen.report.component.ReportNavActionButton
import com.a2z.app.ui.screen.report.filter.LedgerFilterDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.util.VoidCallback


@Composable
fun LedgerReportScreen() {
    val viewModel: LedgerReportViewModel = hiltViewModel()




    LedgerFilterDialog(
        showDialogState = viewModel.filterDialogVisibleState,
        onFilter = {

            viewModel.pagingState.refresh()
            viewModel.searchInput = it
            viewModel.fetchReport()
        })

    MainContent(viewModel) {
        viewModel.filterDialogVisibleState.value = true
    }

    ComplaintDialog(
        complaintTypes = viewModel.complaintTypeListState,
        dialogState = viewModel.complaintDialogVisibleState
    ) { complainTypeId, remark ->
        viewModel.onComplainSubmit(complainTypeId, remark)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun MainContent(
    viewModel: LedgerReportViewModel,
    filterAction: VoidCallback
) {

    Scaffold(backgroundColor = BackgroundColor, topBar = {
        NavTopBar(title = "Ledger Report", actions = {
            ReportNavActionButton {
                filterAction.invoke()
            }
        }, useDefaultBack = false)
    }) { _ ->
        BaseContent(viewModel) {

            AppPagingComponent(
                pagingState = viewModel.pagingState,
                onLoadNext = { viewModel.fetchReport() }
            ) {
                BaseReportItem(statusId = it.statusId,
                    leftSideDate = it.txnTime,
                    leftSideId = it.id.toString(),
                    centerHeading1 = it.number,
                    centerHeading2 = it.beneName,
                    centerHeading3 = it.serviceName,
                    rightAmount = it.amount,
                    rightStatus = it.statusDesc,
                    rightCBal = it.clBalance,
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
                    onComplaint = { viewModel.onComplain(it) },
                    onCheckStatus = { viewModel.onCheckStatus(it) }
                )
            }

            /*  LazyColumn {
                  items(viewModel.pagingState.items.size) { index ->

                      val it = pagingState.items[index]

                      pagingState.shouldLoadNext(index) {
                          viewModel.fetchReport()
                      }

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
                          onComplaint = { viewModel.onComplain(it) },
                          onCheckStatus = { viewModel.onCheckStatus(it) }
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
              }*/
        }
    }
}


