package com.a2z.app.ui.screen.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.screen.AppViewModel
import com.a2z.app.ui.screen.home.HomeViewModel
import com.a2z.app.ui.theme.LocalNavController

@Composable
fun HomeWalletWidget() {

    val viewModel: HomeViewModel = hiltViewModel()
    val appViewModel: AppViewModel = hiltViewModel()


    val size = remember {
        mutableStateOf(IntSize(0, 0))
    }
    val navController = LocalNavController.current
   Card (modifier = Modifier.padding(horizontal = 12.dp).padding(top = 12.dp, bottom = 5.dp),
   shape = MaterialTheme.shapes.medium, elevation = 16.dp){
       Box() {
           BaseContent(appViewModel) {
               Box(

                   modifier = Modifier
                       .fillMaxWidth()
                       .clip(MaterialTheme.shapes.medium)
                       .onSizeChanged { size.value = it }
                       .background(
                           brush = Brush.linearGradient(
                               colors = listOf(
                                   Color(0xFF5BC9D6),
                                   Color(0xFF1DA1D2),
                               ),

                               )
                       ),

                   ) {

                   Column(
                       horizontalAlignment = Alignment.CenterHorizontally
                   ) {
                       Row(
                           modifier = Modifier
                               .fillMaxWidth()
                               .background(Color.Black.copy(alpha = 0.1f))
                               .padding(8.dp)
                       ) {
                           Text(
                               text = viewModel.appPreference.user?.mobile.toString(),
                               color = Color.White.copy(alpha = 0.9f)
                           )
                           Spacer(modifier = Modifier.weight(1f))
                           Text(
                               text = viewModel.appPreference.user?.roleTitle.toString(),
                               color = Color.White.copy(alpha = 0.9f)
                           )


                       }

                       Column(
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(12.dp),
                           horizontalAlignment = Alignment.CenterHorizontally
                       ) {
                           Text(
                               "Available Balance", style = TextStyle(
                                   fontSize = 12.sp,
                                   fontWeight = FontWeight.SemiBold,
                                   color = Color.White.copy(alpha = 0.8f)
                               )
                           )

                           Row(
                               verticalAlignment = Alignment.CenterVertically,
                               modifier = Modifier.padding(top = 2.dp, bottom = 5.dp)
                           ) {

                               Image(
                                   painter = painterResource(id = R.drawable.money_bag),
                                   colorFilter = ColorFilter.tint(
                                       Color.White
                                   ),
                                   contentDescription = null,
                                   modifier = Modifier.size(20.dp)
                               )
                               Spacer(modifier = Modifier.width(5.dp))
                               Text(
                                   text = appViewModel.balanceObs.value.toString(),
                                   style = TextStyle(
                                       fontWeight = FontWeight.SemiBold,
                                       fontSize = 22.sp,
                                       color = Color.White

                                   )
                               )

                               Spacer(modifier = Modifier.width(5.dp))

                               Icon(
                                   imageVector = Icons.Default.Refresh,
                                   contentDescription = null,
                                   modifier = Modifier
                                       .size(24.dp)
                                       .clickable {
                                           appViewModel.fetchWalletBalance()
                                       },
                                   tint = Color.White
                               )
                           }
                       }

                       Divider(color = Color.White.copy(alpha = 0.5f))
                       Row(
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(horizontal = 16.dp, vertical = 8.dp)
                       ) {
                           Row(modifier = Modifier.clickable {
                               navController.navigate(NavScreen.FundMethodScreen.route)
                           }) {
                               Icon(
                                   imageVector = Icons.Default.AccountBalanceWallet,
                                   contentDescription = null,
                                   tint = Color.White
                               )
                               Spacer(modifier = Modifier.width(8.dp))
                               Text(
                                   text = "TopUp Wallet", color = Color.White.copy(alpha = 0.9f),
                                   style = TextStyle(fontWeight = FontWeight.SemiBold)
                               )
                           }
                           Spacer(modifier = Modifier.weight(1f))

                           Row(modifier = Modifier.clickable {
                               navController.navigate(NavScreen.R2RTransferScreen.route)
                           }) {
                               Icon(
                                   imageVector = Icons.Default.SendToMobile,
                                   contentDescription = null,
                                   tint = Color.White
                               )
                               Spacer(modifier = Modifier.width(8.dp))
                               Text(
                                   text = "R2R Transfer", color = Color.White.copy(alpha = 0.9f),
                                   style = TextStyle(fontWeight = FontWeight.SemiBold)
                               )
                           }


                       }
                   }

               }
           }
       }
   }
}
