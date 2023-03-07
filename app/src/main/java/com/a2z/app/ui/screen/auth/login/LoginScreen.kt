package com.a2z.app.ui.screen.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Login
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.a2z.app.R
import com.a2z.app.nav.NavScreen
import com.a2z.app.service.LocalAuth
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.bottomsheet.BottomSheetComponent
import com.a2z.app.ui.component.common.AppCheckBox
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.PasswordTextField
import com.a2z.app.ui.component.permission.LocationComponent
import com.a2z.app.ui.screen.auth.component.AuthBackgroundDraw
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.util.extension.singleResult
import com.a2z.app.util.Exceptions
import com.a2z.app.util.ToggleBottomSheet

@Composable
fun LoginScreen(

    navBackStackEntry: NavBackStackEntry,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val navController = LocalNavController.current
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit, block = {

        if (viewModel.appPreference.user != null
            && LocalAuth.checkForBiometrics(context)
            && viewModel.appPreference.latitude.isNotEmpty()
            && viewModel.appPreference.longitude.isNotEmpty()
        ) {
            viewModel.autoLogin = true
            viewModel.login()
        } else viewModel.autoLogin = false

    })

   LaunchedEffect(key1 = Unit) {
       val shouldNavigate = navBackStackEntry.singleResult<Boolean>("callLogin")
        if (shouldNavigate == true) viewModel.login()
   }


    BaseContent(viewModel) {

        if (viewModel.error.value != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(
                        imageVector = Icons.Default.Info, contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val text = when (viewModel.error.value!!) {
                        is Exceptions.NoInternetException -> "No Internet connection!"
                        else -> "Unable to login!!"
                    }

                    Text(
                        text = text,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        viewModel.login()
                    }, shape = CircularShape) {
                        Text(text = "Retry Login")
                    }


                }
            }
        } else if (viewModel.autoLogin)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
            }
        else BottomSheetComponent(sheetContent = {
            LoginForgotComponent(
                onLoginId = {
                    it.invoke()
                    navController.navigate(NavScreen.ForgotLoginIdScreen.route)
                },
                onPassword = {
                    it.invoke()
                    navController.navigate(NavScreen.ForgotPasswordScreen.route)
                }
            )
        }) { toggle ->
            Scaffold { padding ->
                padding.calculateBottomPadding()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding.calculateBottomPadding())
                ) {
                    AuthBackgroundDraw()

                    BuildFormWidget(toggle)
                }
            }
        }


    }
}

@Composable
private fun BoxScope.BuildFormWidget(toggle: ToggleBottomSheet) {
    val viewModel: LoginViewModel = hiltViewModel()
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

            Spacer(modifier = Modifier.height(24.dp))


            LocationComponent(
                onLocation = {
                    viewModel.login()
                }
            ) {

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
                    it.invoke()
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                TextButton(onClick = {
                    viewModel.navigateTo(
                        NavScreen.UserRegistrationScreen.passArg(selfRegister = true)
                    )
                }) {
                    Text(text = "Sign Up")
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = { toggle.invoke() }) {
                    Text(text = "Forgot ?")
                }
            }

        }
    }


}









