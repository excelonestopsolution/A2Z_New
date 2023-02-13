package com.a2z.app.ui.component

import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.ViewModel
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.dialog.StatusDialog
import com.a2z.app.ui.theme.LocalNavController
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
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    Box(modifier = modifier) {
        Column(modifier = modifier) {
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
                                   popUpTo(it.route){
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

                        ExceptionDialogComponent(it.exceptionState)

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