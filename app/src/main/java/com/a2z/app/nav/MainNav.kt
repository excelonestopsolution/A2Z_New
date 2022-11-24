package com.a2z.app.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.a2z.app.InitialRouteType
import com.a2z.app.MainViewModel
import com.a2z.app.ui.screen.auth.login.LoginScreen
import com.a2z.app.ui.screen.auth.verification.LoginOtpScreen
import com.a2z.app.ui.screen.dashboard.DashboardScreen
import com.a2z.app.ui.screen.exception.ExceptionScreen
import com.a2z.app.ui.screen.fund.bank.FundBankListScreen
import com.a2z.app.ui.screen.fund.method.FundMethodScreen
import com.a2z.app.ui.screen.fund.request.FundRequestScreen
import com.a2z.app.ui.screen.permission.PermissionScreen
import com.a2z.app.ui.screen.result.RechargeResultScreen
import com.a2z.app.ui.screen.test.TestScreen
import com.a2z.app.ui.screen.util.DeviceLockScreen
import com.a2z.app.ui.screen.utility.bill.BillPaymentScreen
import com.a2z.app.ui.screen.utility.operator.OperatorScreen
import com.a2z.app.ui.screen.utility.recharge.RechargeScreen
import com.a2z.app.ui.theme.LocalNavController


@Composable
fun MainNav(viewModel: MainViewModel, initialRouteType: InitialRouteType) {

    val startDestination = when (initialRouteType) {
        InitialRouteType.LOGIN_PAGE -> NavScreen.LoginScreen.route
        InitialRouteType.DASHBOARD_PAGE -> NavScreen.DashboardScreen.route
        InitialRouteType.DEVICE_LOCK_PAGE -> NavScreen.DeviceLockScreen.route
    }

    NavHost(
        navController = LocalNavController.current,
        startDestination = startDestination,
        route = "main-root"
    ) {

        composable(
            route = NavScreen.DeviceLockScreen.route,
            content = {
               DeviceLockScreen()
            })

        composable(
            route = NavScreen.ExceptionScreen.route,
            content = {
                ExceptionScreen(it)
            })

        composable(
            route = NavScreen.LoginScreen.route,
            content = { LoginScreen(it) })

        composable(
            route = NavScreen.LoginOtpScreen.route
        ) {
            val mobileNumber: String = it.arguments?.getString("mobile") ?: ""
            LoginOtpScreen(mobileNumber)
        }
        composable(route = NavScreen.DashboardScreen.route, content = { DashboardScreen() })

        composable(
            route = NavScreen.OperatorScreen.route,
            content = { OperatorScreen() }
        )
        composable(
            route = NavScreen.RechargeScreen.route,
            content = { RechargeScreen() })
        composable(
            route = NavScreen.BillPaymentScreen.route,
            content = { BillPaymentScreen() })
        composable(
            route = NavScreen.RechargeTxnScreen.route,
            content = {
                RechargeResultScreen(it)
            }
        )

        composable(
            route = NavScreen.FundMethodScreen.route,
            content = { FundMethodScreen() }
        )

        composable(
            route = NavScreen.FundBankListScreen.route,
            content = {
                FundBankListScreen()
            }
        )

        composable(
            route = NavScreen.FundRequestScreen.route,
            content = {
                FundRequestScreen()
            }
        )
        composable(
            route = NavScreen.PermissionScreen.route,
            content = {
                PermissionScreen()
            }
        )
        composable(route = NavScreen.TestScreen.route, content = { TestScreen() })
    }
}