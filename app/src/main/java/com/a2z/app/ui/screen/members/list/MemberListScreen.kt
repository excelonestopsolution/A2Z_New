package com.a2z.app.ui.screen.members.list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
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
import com.a2z.app.ui.screen.report.component.ReportNavActionButton
import com.a2z.app.ui.theme.RedColor
import androidx.annotation.Keep

@Keep
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MemberListScreen() {
    val viewModel: MemberListViewModel = hiltViewModel()

    Scaffold(topBar = { NavTopBar(title = viewModel.title, actions = {
        ReportNavActionButton {
            viewModel.showFilterDialogState.value =true
        }
    }) }) {

        BaseContent(viewModel) {

            val pagingState = viewModel.pagingState
            LazyColumn {
                items(viewModel.pagingState.items.size) { index ->

                    val it = pagingState.items[index]

                    pagingState.shouldLoadNext(index) {
                        viewModel.fetchMembers()
                    }

                    BaseReportItem(
                        statusId = (it.statusId ?: "0").toInt(),
                        leftSideDate = it.prefix,
                        leftSideId = "ID : " + it.id.toString(),
                        centerHeading1 = it.mobile,
                        centerHeading2 = it.name,
                        centerHeading3 = null,
                        rightAmount = it.balance,
                        rightStatus = it.status,
                        expandListItems = listOf(
                            "Shop Name" to it.shopName,
                            "Parent" to it.parentDetails,
                            "Email" to it.email,
                        ),
                        actionButton = {
                            if(viewModel.isTransfer) {
                                Button(onClick = {
                                    viewModel.member.value = it
                                    viewModel.showTransferDialogState.value = true
                                }) {
                                    Text(text = ("Transfer"))
                                }
                            }
                        }
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

            MemberFilterDialog(
                showDialogState = viewModel.showFilterDialogState,
                onFilter = { type, input ->
                    viewModel.pagingState.refresh()
                    viewModel.fetchMembers(type,input)
                })
            MemberFundTransferDialog(
                viewModel = viewModel,
                onProceed = {amount,remark->
                    viewModel.onTransfer(amount,remark)
                }
            )

        }
    }
}