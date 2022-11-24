package com.a2z.app.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
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
import com.a2z.app.service.LocalAuth
import com.a2z.app.service.LocalAuthResultType
import com.a2z.app.ui.component.BackPressHandler
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.screen.AppViewModel
import com.a2z.app.ui.screen.dashboard.DashboardViewModel
import com.a2z.app.ui.screen.home.component.*
import com.a2z.app.ui.screen.initialBalanceFetched
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.util.extension.showToast
import kotlinx.coroutines.flow.collectLatest


var isLocalAuthDone = false

@Composable
fun HomeScreen(
    dashboardViewModel: DashboardViewModel,
    appViewModel: AppViewModel = hiltViewModel(),
    viewModel : HomeViewModel = hiltViewModel()
) {

    val isFromLogin = dashboardViewModel.fromLogin
    if(isFromLogin) isLocalAuthDone = true
    val context = LocalContext.current


    BackPressHandler(onBack = {
        viewModel.exitDialogState.value = true
    }) {

       BaseContent(appViewModel,dashboardViewModel,viewModel) {
           if (initialBalanceFetched.value) HomeScreenMainContent(dashboardViewModel)
           else ObsComponent(appViewModel.balanceFlow) {
               initialBalanceFetched.value = true
               dashboardViewModel.bottomSheetVisibilityState.value =
                   initialBalanceFetched.value
               context.showToast(initialBalanceFetched.toString())
               HomeScreenMainContent(dashboardViewModel)
           }
       }
    }



}


@Composable
private fun HomeScreenMainContent(
    dashboardViewModel: DashboardViewModel,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {

    HomeExitDialogComponent(homeViewModel.exitDialogState)

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
                BuildNews(homeViewModel)
                HomeCarouselWidget()
                HomeServiceWidget()
            }

        }
    }


    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    val activity = LocalContext.current as FragmentActivity
    LaunchedEffect(true){
        lifecycleScope.launchWhenStarted {
            homeViewModel.homeScreenState.collectLatest {
                when (it) {
                    is HomeScreenState.OnLogoutComplete -> {
                        activity.finishAffinity()

                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = homeViewModel.onLaunchEffect.value, block = {
        if (!isLocalAuthDone)
            LocalAuth.showBiometricPrompt(activity) {
                when (it) {
                    is LocalAuthResultType.Error ->
                        homeViewModel.singInDialogState.value = true
                    LocalAuthResultType.Failure -> {}
                    is LocalAuthResultType.Success -> {
                        isLocalAuthDone = true
                        activity.showToast("Login Success")
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
                imageVector = Icons.Default.Message,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = homeViewModel.newsResponseState.value!!.retailerNews,
                style = TextStyle(
                    fontSize = 16.sp, fontWeight = FontWeight.Normal,
                    color = Color.Black,
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