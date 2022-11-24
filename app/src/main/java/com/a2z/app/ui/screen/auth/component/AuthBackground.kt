package com.a2z.app.ui.screen.auth.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.PrimaryColor

@Composable
fun AuthBackgroundDraw() {


    val configuration = LocalConfiguration.current
    derivedStateOf { }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((configuration.screenHeightDp / 3).dp)
        ) {

            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    lineTo(0f, size.height)
                    quadraticBezierTo(90f, size.height - 50f, 200f, size.height - 20f)
                    quadraticBezierTo(350f, size.height - 100f, 450f, size.height)
                    quadraticBezierTo(
                        size.width - 100f,
                        size.height - 100f,
                        size.width,
                        size.height - 200f
                    )
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(
                    path = path,
                    color = PrimaryColor
                )
            }

        }
    }
}