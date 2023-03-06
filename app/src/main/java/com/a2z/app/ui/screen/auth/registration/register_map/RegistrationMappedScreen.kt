package com.a2z.app.ui.screen.auth.registration.register_map

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.dialog.ConfirmActionDialog
import com.a2z.app.ui.theme.BackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegistrationMappedScreen() {

    val viewModel: RegistrationMappedViewModel = hiltViewModel()
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = {
            NavTopBar(
                title = viewModel.titleObs.value, subTitle = "Mapped User Under"
            )
        }) {

        BackPressHandler(
            enabled = viewModel.userMapperType != viewModel.userType,
            onBack = {

                viewModel.job?.let {
                    if (it.isActive && !it.isCancelled) it.cancel()
                }
                viewModel.inputMode = ""
                viewModel.inputText = ""
                viewModel.setUserType(true)
                if (viewModel.isFetchDistributor) {
                    viewModel.isFetchDistributor = false
                } else viewModel.userIds.removeLast()
                viewModel.pagingState.refresh()
                viewModel.fetchUsers()


            }) {
            BaseContent(viewModel) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {


                    AppPagingComponent(
                        modifier = Modifier.weight(1f),
                        pagingState = viewModel.pagingState,
                        onLoadNext = { viewModel.fetchUsers() }) { item ->

                        if (
                            viewModel.createUserType == RegistrationMappedViewModel.UserType.Retailer
                            && viewModel.titleObs.value == "MD List"
                            && viewModel.pagingState.items.size == 1
                        ) {
                            viewModel.bottomComponentVisibility.value = true
                        } else if (viewModel.userType == RegistrationMappedViewModel.UserType.FOS
                            && viewModel.createUserType == RegistrationMappedViewModel.UserType.Retailer
                            && viewModel.titleObs.value == "MD List"
                            && viewModel.pagingState.items.size == 1
                        ) {

                            viewModel.bottomComponentVisibility.value = true
                        }

                        Card(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 3.dp)
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onItemClick(item)
                                },
                            shape = MaterialTheme.shapes.small,
                            elevation = 8.dp
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = item.userDetails,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = item.mobile)
                            }
                        }
                    }

                    if (viewModel.bottomComponentVisibility.value) Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        elevation = 8.dp
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "No Master Distributor found under this role ! mapped under direct to company or fetch distributor list",
                                fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(0.7f)
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    viewModel.isFetchDistributor = true
                                    viewModel.setUserType()
                                    viewModel.inputMode = ""
                                    viewModel.inputText = ""
                                    viewModel.fetchUsers()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                            ) {
                                Text("Fetch Distributor")
                            }
                        }
                    }

                }
            }
        }

        ConfirmActionDialog(
            state = viewModel.confirmActionDialog,
            description = viewModel.confirmDescription.value
        ) {
            viewModel.navigateUpWithResult("registrationUserRole" to viewModel.confirmItem!!)
        }
    }

}