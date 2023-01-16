package com.a2z.app.ui.screen.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.a2z.app.R
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.PasswordTextField
import com.a2z.app.ui.component.permission.LocationPermissionComponent
import com.a2z.app.ui.screen.auth.component.AuthBackgroundDraw
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.util.extension.singleResult

@Composable
fun LoginScreen(

    navBackStackEntry: NavBackStackEntry,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val navController = LocalNavController.current

    LaunchedEffect(key1 = Unit, block = {
        if (viewModel.appPreference.user != null) {
            navController.navigate(NavScreen.DashboardScreen.route) {
                popUpTo(NavScreen.DashboardScreen.route) {
                    inclusive = true
                }
            }
        }
    })


    LaunchedEffect(key1 = Unit) {
        val shouldNavigate = navBackStackEntry.singleResult<Boolean>("callLogin")
        if (shouldNavigate == true) viewModel.login()
    }

    BaseContent(viewModel) {
        Scaffold { padding ->
            padding.calculateBottomPadding()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding.calculateBottomPadding())
            ) {
                AuthBackgroundDraw()

                BuildFormWidget()
            }
        }
    }
}

@Composable
private fun BoxScope.BuildFormWidget(
    viewModel: LoginViewModel = hiltViewModel()
) {
    Card(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp), elevation = 10.dp
    ) {

        val formData = viewModel.input
        val userIDWrapper = formData.userIdWrapper
        val passwordWrapper = formData.passwordWrapper

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 32.dp)
                .verticalScroll(rememberScrollState())

        ) {

            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
                modifier = Modifier
                    .height(50.dp)
                    .width(180.dp)
            )


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Login", style = MaterialTheme.typography.h6.copy(
                    color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                value = userIDWrapper.formValue(),
                label = "User ID",
                hint = "Enter User ID",
                error = userIDWrapper.formError(),
                onChange = { userIDWrapper.onChange(it) },
            )

            PasswordTextField(
                value = passwordWrapper.formValue(),
                error = passwordWrapper.formError(),
                onChange = { passwordWrapper.onChange(it) },
            )

            AppCheckBox(
                title = "Remember Login",
                value = viewModel.loginCheckState.value,
                onChange = {
                    viewModel.loginCheckState.value = it
                })

            Spacer(modifier = Modifier.height(32.dp))


            LocationPermissionComponent {
                AppButton(
                    text = "     Login     ",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Login,
                            contentDescription = null,
                            Modifier.size(20.dp)
                        )
                    },
                    isEnable = formData.isValidObs.value,
                    shape = CircularShape,
                    modifier = Modifier.height(45.dp)
                ) {
                    val enable = it.invoke()
                    if (enable) viewModel.login()

                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            TextButton(onClick = { viewModel.navigateTo(NavScreen.UserRegistrationScreen.route) }) {
                Text(text = "Sign Up")
            }

        }
    }


}









