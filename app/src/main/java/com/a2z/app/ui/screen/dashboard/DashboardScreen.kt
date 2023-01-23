package com.a2z.app.ui.screen.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.a2z.app.nav.bottom_bar.DashboardBottomNav
import com.a2z.app.ui.component.bottomsheet.BottomSheetComponent

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {

    val homeBottomNavController = rememberNavController()
    viewModel.scaffoldState = rememberScaffoldState()
    viewModel.bottomSheetVisibilityState

    Scaffold(
        scaffoldState = viewModel.scaffoldState!!,
        bottomBar = {
            if (viewModel.bottomSheetVisibilityState.value)
                DashboardBottomBarWidget(homeBottomNavController)
        },
        drawerShape = MaterialTheme.shapes.small,
        drawerContent = { DashboardDrawerWidget(viewModel) },

        ) {
        Box(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
            DashboardBottomNav(homeBottomNavController, viewModel)
        }

    }
}


