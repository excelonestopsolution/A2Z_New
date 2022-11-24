package com.a2z.app.ui.screen.test

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun CanvasTest() {

    Scaffold {
        it.calculateBottomPadding()

        Canvas(modifier = Modifier.fillMaxSize()) {

            val (points, colors) = Pair(
                listOf(25f, 55f, 35f,10f,15f),
                listOf(Color.Red, Color.Green, Color.Magenta,Color.Blue,Color.Black)
            )

            drawPieChart(points, colors)
        }
    }
}

fun DrawScope.drawPieChart(points: List<Float>, colors: List<Color>) {
    val propertions = points.map {
        val percentage = it * 100 / points.sum()
        val percentageInDegree = 360 * percentage / 100
        percentageInDegree

    }


    var startAngle = 0f
    var sweepAngle: Float
    val padding = 200f
    propertions.forEachIndexed {index,value->

        sweepAngle = value


        drawArc(
            color = colors[index],
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            size = Size(size.width - padding, size.width-padding),
            style = Stroke(
                width = 160f
            ),
            topLeft = Offset(padding/2,padding/2)
        )

        startAngle += sweepAngle
    }


}


private fun DrawScope.drawCircle() {
    val radius = 200f
    drawCircle(
        color = Color.Red,
        radius = radius,
        center = Offset(radius, radius)
    )
}

private fun DrawScope.drawRectangle() {

    val (width, height) = Pair(500f, 1000f)

    val xOffset = center.x - width / 2
    val yOffset = center.y - height / 2

    drawRect(
        color = Color.Red,
        size = Size(width, height),
        topLeft = Offset(xOffset, yOffset)
    )
}