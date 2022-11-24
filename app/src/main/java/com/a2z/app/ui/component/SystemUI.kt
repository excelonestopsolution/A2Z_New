package com.a2z.app.ui.component

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.a2z.app.util.FunCompose
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun StatusBarUI(color: Color, content : FunCompose) {
    MaterialTheme {
        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(color)
        content()
    }

}

private typealias FunComposeLocalConfiguration= @Composable (height : Dp, width : Dp)->Unit

@Composable
fun LocalConfigurationUI(content : FunComposeLocalConfiguration) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    content(screenHeight,screenWidth)
}