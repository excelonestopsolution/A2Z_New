package com.a2z.app.ui.screen.home.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.a2z.app.BuildConfig
import com.a2z.app.MainActivity
import com.a2z.app.nav.NavScreen
import com.a2z.app.service.LocalAuth
import com.a2z.app.service.LocalAuthResultType
import com.a2z.app.ui.component.BackPressHandler
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.screen.dashboard.DashboardViewModel
import com.a2z.app.ui.screen.home.di_md.DistributorHomeScreen
import com.a2z.app.ui.screen.home.retailer.HomeScreenState
import com.a2z.app.ui.screen.home.retailer.RetailerHomeScreen
import com.a2z.app.ui.screen.home.sale.SaleHomeScreen
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.util.Exceptions
import com.a2z.app.util.extension.showToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

var useLocalAuth = true

@Composable
fun HomeScreen(viewModel: DashboardViewModel) {

    @Composable
    fun HomeScreenContent() {
        when (viewModel.appPreference.user?.roleId) {
            5 -> RetailerHomeScreen(viewModel)
            3,4 -> DistributorHomeScreen(viewModel)
            22,23,24 -> SaleHomeScreen(viewModel)
        }
    }

    val isFromLogin = viewModel.fromLogin
    if (isFromLogin) useLocalAuth = false

    val context = LocalContext.current
    val scope = rememberCoroutineScope()



   BaseContent(viewModel) {

       val navController = LocalNavController.current
       val activity = LocalContext.current as FragmentActivity

       BackPressHandler(onBack = {

           if (viewModel.scaffoldState?.drawerState?.isOpen == true) {
               scope.launch { viewModel.scaffoldState?.drawerState?.close() }
           } else {
               if (viewModel.exitFromApp) {
                   viewModel.exitFromApp = false
                   (context as MainActivity).finishAffinity()
               } else viewModel.exitDialogState.value = true
           }

       }, enabled = true) {


           HomeSignInOptionWidget(
               dialogState = viewModel.singInDialogState,
               onBiometric = {
                   viewModel.singInDialogState.value = false
                   viewModel.setOnLaunchEffect()
               },
               onLogin = {
                   viewModel.logout()
               }
           )

           HomeExitDialogComponent(viewModel.exitDialogState){
               viewModel.logout()
           }

           HomeLocationServiceDialog()


           LaunchedEffect(key1 = viewModel.onLaunchEffect.value, block = {
               if (useLocalAuth)
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
           HomeScreenContent()


       }
       val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
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
                               viewModel.exitFromApp = true
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

}