package com.a2z.app.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.LocalNavController
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
                        if(it.exception is Exceptions.SessionExpiredException){
                            navController.navigate(NavScreen.LoginScreen.route) {
                                popUpTo(NavScreen.LoginScreen.route) {
                                    inclusive = true
                                }
                            }
                        }
                        else {
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
                .background(color = BackgroundColor)
                .fillMaxSize()
                .weight(1f)

        ) {
            items(1) {
                HomeWalletWidget()
                BuildNews(viewModel)
                HomeCarouselWidget()
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


}

@Composable
private fun BuildNews(homeViewModel: HomeViewModel) {
    if (homeViewModel.newsResponseState.value != null) Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.NotificationImportant,
                contentDescription = null,
                tint = MaterialTheme.colors.primaryVariant,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = homeViewModel.newsResponseState.value!!.retailerNews,
                style = TextStyle(
                    fontSize = 14.sp, fontWeight = FontWeight.Normal,
                    color = Color.Black.copy(alpha = 0.7f),
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )


        }
    }
    else Spacer(modifier = Modifier.height(8.dp))
}