package com.a2z.app.ui.screen.auth.registration.register_type

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.a2z.app.R
import com.a2z.app.data.model.auth.RegistrationRoleUser
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.util.extension.singleParcelableResult

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegistrationTypeScreen(navBackStackEntry: NavBackStackEntry) {


    val viewModel: RegistrationTypeViewModel = hiltViewModel()
    val navController = LocalNavController.current

    fun navigate() {
        navController.navigate(
            NavScreen.UserRegistrationScreen.passArg(
                selfRegister = false,
                role = viewModel.selectedRole.value,
                mapRole = viewModel.mapRole,
                mobileNumber = viewModel.mobileNumber
            )
        )
    }


    LaunchedEffect(key1 = Unit) {
        val data =
            navBackStackEntry.singleParcelableResult<RegistrationRoleUser>("registrationUserRole")
        if (data != null) {
            viewModel.mapRole = data
            navigate()
        }
    }

    Scaffold(
        drawerBackgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Create User") }) {
        BaseContent(viewModel) {

            ObsComponent(viewModel.roleListResultFlow) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            BackgroundColor
                        )
                ) {

                    Text(
                        "Select Role", style = MaterialTheme.typography.h6.copy(
                            color = PrimaryColor,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ), modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {

                        items(it.roles!!, key = { it.roleId }) {


                            val id = viewModel.selectedRole.value?.roleId
                            val contentColor = if (id == it.roleId) Color.White else Color.Black
                            val backgroundColor = if (id == it.roleId) GreenColor else Color.White

                            Card(
                                backgroundColor = backgroundColor,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        if (id == it.roleId) viewModel.selectedRole.value = null
                                        else viewModel.selectedRole.value = it
                                    }) {
                                Row(
                                    modifier = Modifier
                                        .padding(24.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.icon_sucess),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        colorFilter = ColorFilter.tint(contentColor)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.h6.copy(
                                            color = contentColor
                                        )
                                    )
                                }
                            }

                        }

                    }

                    if (viewModel.selectedRole.value != null) Button(
                        onClick = {

                            if (viewModel.mapRole != null) {
                                if (viewModel.selectedRole.value!!.roleId == 7
                                    || (viewModel.appPreference.user!!.roleId == 24
                                            && viewModel.selectedRole.value!!.roleId == 3)
                                ) navigate()
                                else {

                                    navController.navigate(
                                        NavScreen.RegistrationMappedUserScreen.passArgs(
                                            viewModel.selectedRole.value!!.roleId.toInt()
                                        )
                                    )
                                }
                            } else navigate()

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Continue")
                    }

                }

            }

        }
    }

}