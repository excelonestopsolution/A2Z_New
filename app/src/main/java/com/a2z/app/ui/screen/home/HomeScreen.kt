package com.a2z.app.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.a2z.app.BuildConfig
import com.a2z.app.nav.NavScreen
import com.a2z.app.service.LocalAuth
import com.a2z.app.service.LocalAuthResultType
import com.a2z.app.ui.component.BackPressHandler
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.screen.dashboard.DashboardViewModel
import com.a2z.app.ui.screen.home.component.*
import com.a2z.app.ui.theme.*
import com.a2z.app.util.Exceptions
import com.a2z.app.util.extension.showToast
import kotlinx.coroutines.flow.collectLatest


var useLocalAuth = true

@Composable
fun HomeScreen(
    dashboardViewModel: DashboardViewModel,

    viewModel: HomeViewModel = hiltViewModel()
) {

    val isFromLogin = dashboardViewModel.fromLogin
    if (isFromLogin) useLocalAuth = false
    val context = LocalContext.current


    BackPressHandler(onBack = {
        viewModel.exitDialogState.value = true
    }, enabled = viewModel.handleBackPressState.value) {

        BaseContent(dashboardViewModel, viewModel) {
            if (dashboardViewModel.bottomSheetVisibilityState.value) {
                HomeScreenMainContent(dashboardViewModel)
            } else ObsComponent(
                flow = viewModel.balanceResponseFlow,
                onRetry = {
                    viewModel.fetchWalletBalance()
                    viewModel.fetchBanners()
                    viewModel.fetchNews()
                }
            ) {
                viewModel.handleBackPressState.value = true
                dashboardViewModel.bottomSheetVisibilityState.value = true
                HomeScreenMainContent(dashboardViewModel)
            }
        }
    }

    HomeExitDialogComponent(viewModel.exitDialogState)


    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    val activity = LocalContext.current as FragmentActivity
    val navController = LocalNavController.current
    LaunchedEffect(true) {
        lifecycleScope.launchWhenStarted {
            viewModel.homeScreenState.collectLatest {
                when (it) {
                    is HomeScreenState.OnHomeApiFailure -> {
                        activity.showToast(it.exception.message.toString())
                        if (it.exception is Exceptions.SessionExpiredException) {
                            navController.navigate(NavScreen.LoginScreen.route) {
                                popUpTo(NavScreen.LoginScreen.route) {
                                    inclusive = true
                                }
                            }
                        } else {
                            viewModel.handleBackPressState.value = false
                        }
                    }
                    is HomeScreenState.OnLogoutComplete -> {
                        navController.navigate(NavScreen.LoginScreen.route) {
                            popUpTo(NavScreen.LoginScreen.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
    }


}


@Composable
private fun HomeScreenMainContent(
    dashboardViewModel: DashboardViewModel,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    HomeSignInOptionWidget(dashboardViewModel)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeAppBarWidget(dashboardViewModel)
        Spacer(modifier = Modifier.height(2.dp))
        LazyColumn(
            modifier = Modifier
                .background(color = BackgroundColor2)
                .fillMaxSize()
                .weight(1f)

        ) {
            items(1) {
                HomeWalletWidget()
                HomeCarouselWidget()
                BuildAlertComponent(viewModel)
                HomeServiceWidget()
            }

        }
    }

    val activity = LocalContext.current as FragmentActivity
    LaunchedEffect(key1 = viewModel.onLaunchEffect.value, block = {
        if (useLocalAuth && !BuildConfig.DEBUG)
            LocalAuth.showBiometricPrompt(activity) {
                when (it) {
                    is LocalAuthResultType.Error ->
                        viewModel.singInDialogState.value = true
                    LocalAuthResultType.Failure -> {}
                    is LocalAuthResultType.Success -> {
                        useLocalAuth = false
                    }
                }
            }
    })

    HomeLocationServiceDialog()

}


