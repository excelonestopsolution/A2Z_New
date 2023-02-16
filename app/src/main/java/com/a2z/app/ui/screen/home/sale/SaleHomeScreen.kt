package com.a2z.app.ui.screen.home.sale

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.screen.dashboard.DashboardViewModel
import com.a2z.app.ui.screen.home.component.HomeAppBarWidget
import com.a2z.app.ui.screen.home.component.HomeWalletWidget
import com.a2z.app.ui.screen.home.retailer.component.HomeNewsComponent
import com.a2z.app.ui.theme.BackgroundColor2

@Composable
fun SaleHomeScreen(dashboardViewModel : DashboardViewModel) {
    val viewModel: SaleHomeViewModel = hiltViewModel()

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




            }
        }
    }
}