package com.a2z.app.ui.screen.util.complaint

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.AppProgress
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.EmptyListComponent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.screen.report.component.BaseReportItem
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.RedColor


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ComplaintScreen() {

    val viewModel: ComplainViewModel = hiltViewModel()

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Complaints") }) {
        BaseContent(viewModel) {

            val pagingState = viewModel.pagingState
            LazyColumn {
                items(viewModel.pagingState.items.size) { index ->

                    val it = pagingState.items[index]

                    pagingState.shouldLoadNext(index) {
                        viewModel.fetchReport()
                    }

                    BaseReportItem(statusId = (it.status_id ?: "0").toDouble().toInt(),
                        leftSideDate = it.created_at,
                        leftSideId = it.id,
                        centerHeading1 = it.issue_type,
                        centerHeading2 = it.txn_id,
                        centerHeading3 = null,
                        rightAmount = null,
                        rightStatus = it.status,
                        expandListItems = listOf(
                            "Looking By" to it.looking_by,
                            "Approved By" to it.approved_by,
                            "Approved Date" to it.approved_date,
                            "Remark" to it.remark,
                            "Current Remark" to it.current_status_remark,
                        )

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