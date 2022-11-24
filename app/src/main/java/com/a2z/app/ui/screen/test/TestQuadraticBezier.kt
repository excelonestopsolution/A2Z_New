package com.a2z.app.ui.screen.test

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun TestQuadraticBezier() {

    Scaffold {
        it.calculateBottomPadding()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent),
            contentAlignment = Alignment.Center
        ) {


            Canvas(modifier = Modifier.size(300.dp)) {

                val width = size.width
                val height = size.height

                val path = Path().apply {

                    lineTo(0f, height)
                    quadraticBezierTo(width/5,height, width/2.25f,height - 50f)
                    quadraticBezierTo(width - (width/ 3.25f),height - 105,width,height - 10f)
                    lineTo(width,0f)
                    close()

                }
                drawPath(path, Color.Red)
            }


        }
    }
}

@Composable
private fun ExampleOne() {
    Canvas(modifier = Modifier.fillMaxSize(0.8f), onDraw = {

        val width = size.width
        val height = size.height
        val roundnessFactor = 50f

        val path = Path().apply {

            moveTo(0f, height * .33f)
            lineTo(0f, height - 100f)
            quadraticBezierTo(0f, height, 100f, height)
            lineTo(width - 100f, height)
            quadraticBezierTo(width, height, width, height - 50f)
            lineTo(width, 100f)
            quadraticBezierTo(width, 0f, width - 150f, 100f)
            lineTo(50f, height * .33f)
            quadraticBezierTo(0f, height * .33f + 50f, 0f, height * .33f + 150)




            close()

        }
        drawPath(
            path, brush = Brush.linearGradient(
                colors = listOf(
                    Color.Red,
                    Color.Yellow
                )
            )
        )
    })
}