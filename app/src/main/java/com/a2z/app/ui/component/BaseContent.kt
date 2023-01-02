package com.a2z.app.ui.component

import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.a2z.app.nav.NavScreen
import com.a2z.app.receiver.ConnectionLiveData
import com.a2z.app.ui.dialog.StatusDialog
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.theme.ShapeZeroRounded
import com.a2z.app.ui.util.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest


@Composable
fun <T> CollectLatestWithScope(flow: MutableSharedFlow<T>, callback: (T) -> Unit) {
    LaunchedEffect(key1 = Unit, block = {
        flow.collectLatest { callback.invoke(it) }
    })
}

@Composable
fun BaseContent(
    vararg viewModels: ViewModel,
    content: @Composable () -> Unit
) {








    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            androidx.compose.animation.AnimatedVisibility(
                visible = true,
                enter = slideInVertically(),
                exit = slideOutVertically() + fadeOut()
            ) {

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    content()
                    viewModels.forEach {

                        val dialogState = (it as BaseViewModel).dialogState
                        val bannerState = it.bannerState
                        val navController = LocalNavController.current




                        CollectLatestWithScope(it.navigateUpWithResultFlow) {
                            it.forEach { map ->
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    map.key,
                                    map.value
                                )
                            }
                            navController.navigateUp()
                        }

                        CollectLatestWithScope(it.navigateToFlow) {
                            navController.navigate(it.route) {
                                if (it.popUpAll) {
                                    popUpTo(navController.currentDestination?.route ?: "") {
                                        inclusive = true
                                    }
                                }
                            }
                        }

                        CollectLatestWithScope(it.dashboardState) {
                            if (it) navController.navigate(
                                NavScreen.DashboardScreen.route
                            ) {
                                popUpTo(NavScreen.DashboardScreen.route) {
                                    inclusive = true
                                }
                            }
                        }





                        DialogExceptionComponent(it.exceptionState)

                        StatusDialog(dialogState)

                        AppNotificationBanner(state = bannerState)

                        val manager = LocalFocusManager.current
                        val keyboard = keyboardAsState().value
                        if (!keyboard) {
                            manager.clearFocus()
                        }

                    }
                }
            }

        }

    }
}