package com.a2z.app.ui.screen.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.a2z.app.nav.bottom_bar.DashboardBottomNav
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.ShapeZeroRounded

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {

    val homeBottomNavController = rememberNavController()
    viewModel.scaffoldState = rememberScaffoldState()


    Scaffold(
        scaffoldState = viewModel.scaffoldState!!,
        bottomBar = { if(viewModel.bottomSheetVisibilityState.value)
            DashboardBottomBarWidget(homeBottomNavController) },
        drawerShape = MaterialTheme.shapes.small,
        drawerContent = { DashboardDrawerWidget(viewModel.appPreference) },
    ) {
        Box(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
           DashboardBottomNav(homeBottomNavController, viewModel)
        }
    }
}


