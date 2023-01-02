package com.a2z.app.ui.screen.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.a2z.app.nav.bottom_bar.DashboardBottomNav
import com.a2z.app.ui.screen.initialBalanceFetched
import com.a2z.app.ui.theme.ShapeZeroRounded

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {

    val homeBottomNavController = rememberNavController()
    viewModel.scaffoldState = rememberScaffoldState()


    Scaffold(
        scaffoldState = viewModel.scaffoldState!!,
        bottomBar = { if(initialBalanceFetched.value)
            DashboardBottomBarWidget(homeBottomNavController) },
        drawerShape = ShapeZeroRounded,
        drawerContent = { DashboardDrawerWidget() },
    ) {
        Box(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
           DashboardBottomNav(homeBottomNavController, viewModel)
        }
    }
}


