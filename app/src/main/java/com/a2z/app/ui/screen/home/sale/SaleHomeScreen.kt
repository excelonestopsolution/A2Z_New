package com.a2z.app.ui.screen.home.sale

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.screen.dashboard.DashboardViewModel
import com.a2z.app.ui.screen.home.component.HomeAppBarWidget
import com.a2z.app.ui.screen.home.component.HomeWalletWidget
import com.a2z.app.ui.screen.home.retailer.component.HomeNewsComponent
import com.a2z.app.ui.theme.*
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun SaleHomeScreen(dashboardViewModel: DashboardViewModel) {
    val viewModel: SaleHomeViewModel = hiltViewModel()

    BaseContent(viewModel) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeAppBarWidget(dashboardViewModel)
            Spacer(modifier = Modifier.height(2.dp))
            Column(
                modifier = Modifier
                    .background(color = BackgroundColor2)
                    .fillMaxSize()
                    .weight(1f)

            ) {


                Card(
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {

                    Column(modifier = Modifier.padding(12.dp)) {

                        Row {
                            Column() {
                                Text(text = "Users", style = MaterialTheme.typography.h6)
                                Text(
                                    text = "mapped under you",
                                    fontSize = 14.sp, color = PrimaryColorLight
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))

                            OutlinedButton(onClick = {
                                viewModel.navigateTo(NavScreen.RegistrationTypeScreen.passArgs(
                                    shouldMap = true
                                ))
                            }) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(5.dp))
                                Text("Create New")
                            }
                        }

                        val mList = viewModel.getMappedUserTags()
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyVerticalGrid(columns = GridCells.Fixed(3), content = {


                            items(mList) {

                                Card(
                                    modifier = Modifier
                                        .padding(3.dp)
                                        .weight(1f)
                                        .clickable {
                                            viewModel.navigateMemberList(it)
                                        },
                                    shape = MaterialTheme.shapes.small,
                                    elevation = 8.dp
                                ) {

                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.user),
                                            contentDescription = null,
                                            modifier = Modifier.size(60.dp)
                                        )
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(text = it)
                                    }

                                }

                            }
                        })


                    }

                }


                Card(
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
                        .fillMaxWidth()
                ) {


                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.user),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Users Created By User",
                                fontWeight = FontWeight.Bold,
                                color = PrimaryColorLight
                            )
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        OutlinedButton(
                            onClick = {viewModel.navigateTo( NavScreen.MemberCreatedBySaleScreen.passArgs(true)) },
                            border = BorderStroke(1.dp, color = GreenColor)
                        ) {
                            Text(text = "Registration Completed Users   ")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                viewModel.navigateTo(NavScreen.MemberCreatedBySaleScreen.passArgs(false))
                            },
                            border = BorderStroke(1.dp, color = RedColor)
                        ) {
                            Text(text = "Registration InCompleted Users")
                        }
                    }


                }


            }
        }
    }
}