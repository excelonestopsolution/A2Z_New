package com.a2z.app.ui.screen.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.a2z.app.nav.bottom_bar.DashboardBottomNav
import com.a2z.app.ui.component.bottomsheet.BottomSheetComponent
import com.a2z.app.ui.screen.home.component.HomeScreen
import com.a2z.app.ui.theme.LocalUserRole
import com.a2z.app.ui.util.UserRole

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {

    val role = when (viewModel.appPreference.user!!.roleId) {
        5 -> UserRole.RETAILER
        4 -> UserRole.DISTRIBUTOR
        3 -> UserRole.MD
        else -> UserRole.NOT_DEFINED
    }
    CompositionLocalProvider(
        LocalUserRole provides role
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
}


