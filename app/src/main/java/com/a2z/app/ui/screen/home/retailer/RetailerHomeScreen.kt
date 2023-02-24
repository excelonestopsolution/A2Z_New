package com.a2z.app.ui.screen.home.retailer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.CollectLatestWithScope
import com.a2z.app.ui.screen.dashboard.DashboardViewModel
import com.a2z.app.ui.screen.home.component.HomeAppBarWidget
import com.a2z.app.ui.screen.home.component.HomeWalletWidget
import com.a2z.app.ui.screen.home.retailer.component.*
import com.a2z.app.ui.theme.BackgroundColor2
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.util.resource.ResultType




@Composable
fun RetailerHomeScreen(
    dashboardViewModel: DashboardViewModel,

    viewModel: RetailerHomeViewModel = hiltViewModel()

) {

    val navController = LocalNavController.current

   BaseContent(viewModel) {
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

                   val newsState = dashboardViewModel.newsResponseState.value

                   HomeWalletWidget()
                   HomeCarouselWidget()
                   HomeKycComponent(viewModel)
                   HomeNewsComponent(newsState)
                   HomeServiceWidget()
               }

           }
       }

       KycWarningDialog(viewModel.kycDialogState, viewModel.kycInfo()){
           viewModel.navigateKycScreen(it)
       }

       val context = LocalContext.current

       CollectLatestWithScope(flow = viewModel.hotelFlightDirectUrlFlow, callback = {
           when (it) {
               is ResultType.Failure -> viewModel.alertDialog("Something went wrong!, please try again")
               is ResultType.Loading -> viewModel.progressDialog()
               is ResultType.Success -> {
                   if (it.data.status == 1) {
                       viewModel.dismissDialog()
                       val i = Intent(Intent.ACTION_VIEW)
                       i.data = Uri.parse(it.data.url.toString())
                       context.startActivity(i)
                   } else viewModel.failureDialog(it.data.message.toString())
               }
           }
       })

       CollectLatestWithScope(flow = viewModel.panAutoLogFlow, callback = {
           when (it) {
               is ResultType.Failure -> viewModel.alertDialog("Something went wrong!, please try again")
               is ResultType.Loading -> viewModel.progressDialog()
               is ResultType.Success -> {
                   if (it.data.status == 1) {
                       viewModel.dismissDialog()
                       val i = Intent(Intent.ACTION_VIEW)
                       i.data = Uri.parse(it.data.url.toString())
                       context.startActivity(i)
                   } else viewModel.failureDialog(it.data.message.toString())
               }
           }
       })

   }
}



