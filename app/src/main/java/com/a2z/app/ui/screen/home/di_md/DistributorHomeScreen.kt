package com.a2z.app.ui.screen.home.di_md

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.screen.dashboard.DashboardViewModel
import com.a2z.app.ui.screen.home.component.HomeAppBarWidget
import com.a2z.app.ui.screen.home.component.HomeWalletWidget
import com.a2z.app.ui.screen.home.di_md.component.AgentRequestApproveDialog
import com.a2z.app.ui.screen.home.di_md.component.AgentRequestViewComponent
import com.a2z.app.ui.screen.home.retailer.component.*
import com.a2z.app.ui.theme.BackgroundColor2

@Composable
fun DistributorHomeScreen(
    dashboardViewModel: DashboardViewModel,
) {

    val viewModel: DistributorHomeViewModel = hiltViewModel()

    BaseContent(viewModel) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeAppBarWidget(dashboardViewModel)
            Spacer(modifier = Modifier.height(2.dp))
            Column(
                modifier = Modifier
                    .background(color = BackgroundColor2)
                    .fillMaxSize()
                    .weight(1f)

            ) {

                val newsState = dashboardViewModel.newsResponseState.value
                HomeWalletWidget()
                HomeNewsComponent(newsState)
                AgentRequestViewComponent {
                    AgentRequestApproveDialog(
                        state = viewModel.approveDialogState,
                        remarkList = it,
                        statusList = listOf(
                            "1" to "Approve",
                            "2" to "Reject",
                            "3" to "Pending"
                        ),
                        item = viewModel.agentRequestView.value,
                        onProceed = {remark,status,remarkInput->
                            viewModel.onProceed(remark,status,remarkInput)
                        }
                    )
                }

            }
        }
    }
}