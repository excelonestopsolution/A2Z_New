package com.a2z.app.ui.screen.home

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Message
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.a2z.app.ui.dialog.BankDownDialog
import com.a2z.app.ui.screen.dashboard.DashboardViewModel
import com.a2z.app.ui.screen.home.component.*
import com.a2z.app.ui.theme.*
import com.a2z.app.util.Exceptions
import com.a2z.app.util.VoidCallback
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
                .background(color = BackgroundColor)
                .fillMaxSize()
                .weight(1f)

        ) {
            items(1) {
                HomeWalletWidget()
                if (false) HomeCarouselWidget()

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


}

@Composable
private fun BuildAlertComponent(homeViewModel: HomeViewModel) {

    val bankDown = homeViewModel.bankDownResponseState.value
    val news = homeViewModel.newsResponseState.value
    val kyc = homeViewModel.checkDMTAndAEPSKycPending()


    val isBankDown = bankDown != null && bankDown.status == 1
    val isNews = news != null && news.status == 1

    val bankDialogState = remember {
        mutableStateOf(false)
    }
    if (isBankDown) BankDownDialog(dialogState = bankDialogState, bankList = bankDown?.bankList)


    val show = isBankDown || isNews || kyc

    if (show) Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 5.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = 16.dp
    ) {

        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Alert & Info",
                fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = RedColor
            )
            Spacer(modifier = Modifier.height(5.dp))

            val kycInfo = homeViewModel.kycInfo()

            if(kyc && kycInfo!=null) BuildAlertItem(
                message = kycInfo.second,
                icon = Icons.Default.Fingerprint,
                color = Color.DarkGray) {

            }

            if (isNews) BuildAlertItem(
                message = news!!.retailerNews.toString(),
                icon = Icons.Default.Message,
                color = PrimaryColorDark
            ) {

            }

            if (isBankDown) BuildAlertItem(
                message = bankDown!!.bankString.toString(),
                icon = Icons.Default.AccountBalance,
                color = RedColor,
                blink = true
            ) {
                bankDialogState.value = true
            }

        }

    }
}

@Composable
private fun BuildAlertItem(
    message: String,
    icon: ImageVector,
    color: Color,
    blink: Boolean = false,
    onClick: VoidCallback

) {


    val transition = rememberInfiniteTransition()
    val colorState = if (blink) transition.animateColor(
        initialValue = color,
        targetValue = PrimaryColorDark,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = tween(1000)
        )
    ) else null


    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clip(shape = MaterialTheme.shapes.medium)
            .clickable { onClick.invoke() }
            .background(color = color.copy(0.1f))
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = message,
            style = TextStyle(
                fontSize = 16.sp, fontWeight = FontWeight.W400,
                color = if (blink) colorState!!.value else color,
                textAlign = TextAlign.Start,
                lineHeight = 24.sp,
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )


    }


}