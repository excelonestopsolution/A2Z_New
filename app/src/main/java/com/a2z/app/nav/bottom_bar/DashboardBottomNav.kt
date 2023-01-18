package com.a2z.app.nav.bottom_bar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.a2z.app.ui.screen.dashboard.DashboardViewModel
import com.a2z.app.ui.screen.home.HomeScreen
import com.a2z.app.ui.screen.report.ledger.LedgerReportScreen


@Composable
fun DashboardBottomNav(navController: NavHostController,
                       viewModel: DashboardViewModel) {

    NavHost(
        navController = navController,
        startDestination = "bottom-home-page",
        route = "dashboard-bottom-bar-root"
    ) {
        composable("bottom-home-page") {
            HomeScreen(viewModel)
        }
        composable("bottom-ledger-page") {
            LedgerReportScreen()
        }

    }
}