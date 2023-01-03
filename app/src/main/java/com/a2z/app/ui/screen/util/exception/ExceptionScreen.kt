package com.a2z.app.ui.screen.util.exception

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BackPressHandler
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.util.extension.safeParcelable
import com.a2z.app.util.AppException
import com.a2z.app.util.Exceptions

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ExceptionScreen(it: NavBackStackEntry) {

    val exception = it.safeParcelable<AppException>("exception")?.exception
    val navController = LocalNavController.current


    BackPressHandler(onBack = {
        if (exception is Exceptions.SessionExpiredException) {
            navController.navigate(NavScreen.LoginScreen.route) {
                popUpTo(NavScreen.DashboardScreen.route) {
                    inclusive = true
                }
            }
        } else navController.navigateUp()
    }, enabled = true) {
        Scaffold {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(exception?.message.toString())
            }
        }
    }

}