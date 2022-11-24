package com.a2z.app.ui.screen.test

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.R
import com.a2z.app.ui.theme.*
import com.a2z.app.ui.util.standardQuadFromTo

@Composable
fun TestTaskManagement() {
    Scaffold(backgroundColor = BackgroundColor) {
        it.calculateBottomPadding()

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            BuildAppBarSection()

            BuildWalletInfoSection()

            BuildDocumentProcessSection()
            BuildStatisticSection()
            BuildDescriptionSection()

        }
    }

}

@Composable
private fun BuildWalletInfoSection() {
    BoxWithConstraints(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(176.dp)
            .clip(shape = MaterialTheme.shapes.large)
            .background(BlueViolet1),
    ) {


        val width = constraints.maxWidth
        val height = constraints.maxHeight

        // Medium colored path
        val mediumColoredPoint1 = Offset(0f, height * 0.3f)
        val mediumColoredPoint2 = Offset(width * 0.1f, height * 0.35f)
        val mediumColoredPoint3 = Offset(width * 0.4f, height * 0.05f)
        val mediumColoredPoint4 = Offset(width * 0.75f, height * 0.7f)
        val mediumColoredPoint5 = Offset(width * 1.4f, -height.toFloat())

        val mediumColoredPath = Path().apply {
            moveTo(mediumColoredPoint1.x, mediumColoredPoint1.y)
            standardQuadFromTo(mediumColoredPoint1, mediumColoredPoint2)
            standardQuadFromTo(mediumColoredPoint2, mediumColoredPoint3)
            standardQuadFromTo(mediumColoredPoint3, mediumColoredPoint4)
            standardQuadFromTo(mediumColoredPoint4, mediumColoredPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }

        // Light colored path
        val lightPoint1 = Offset(0f, height * 0.35f)
        val lightPoint2 = Offset(width * 0.1f, height * 0.4f)
        val lightPoint3 = Offset(width * 0.3f, height * 0.35f)
        val lightPoint4 = Offset(width * 0.65f, height.toFloat())
        val lightPoint5 = Offset(width * 1.4f, -height.toFloat() / 3f)

        val lightColoredPath = Path().apply {
            moveTo(lightPoint1.x, lightPoint1.y)
            standardQuadFromTo(lightPoint1, lightPoint2)
            standardQuadFromTo(lightPoint2, lightPoint3)
            standardQuadFromTo(lightPoint3, lightPoint4)
            standardQuadFromTo(lightPoint4, lightPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawPath(
                path = mediumColoredPath,
                color = BlueViolet2
            )
            drawPath(
                path = lightColoredPath,
                color = BlueViolet3
            )
        }


    }
}

@Composable
private fun BuildDescriptionSection() {
    Column(Modifier.padding(16.dp)) {

        Text(text = "Description", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                    " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s," +
                    " when an unknown printer took a galley of type and scrambled it to make a type " +
                    "specimen book. It has survived not only five centuries, but also the leap into " +
                    "electronic typesetting, remaining essentially unchanged. It was popularised in " +
                    "the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, " +
                    "and more recently with desktop publishing software like Aldus PageMaker including " +
                    "versions of Lorem Ipsum.",
            style = TextStyle(color = SubTextColor, lineHeight = 24.sp)
        )

    }
}

@Composable
private fun BuildStatisticSection() {


    fun DrawScope.drawChart(points: List<Float>, colors: List<Color>) {

        val proportions = points.map {
            val percentage = it * 100 / points.sum()
            360 * percentage / 100
        }


        var startAngle = 0f
        var sweepAngle: Float

        proportions.forEachIndexed { index, value ->
            val color = colors[index]

            sweepAngle = value

            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(
                    width = 28f,
                    cap = StrokeCap.Round
                )
            )

            startAngle += sweepAngle
        }


    }

    @Composable
    fun BuildDotPoint(color: Color) {
        Canvas(modifier = Modifier.size(15.dp)) {
            drawCircle(
                color = color,
                style = Stroke(
                    width = 8f
                )
            )
        }
    }

    @Composable
    fun BuildDotAndTitle(color: Color, text: String) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BuildDotPoint(color = color)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = SubTextColor
                )
            )
        }
    }


    Column(Modifier.padding(16.dp)) {
        Text(text = "Jetpack Compose UI Design", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(5.dp))
        Row {
            Text(
                "09.00 AM - 11.00 AM", style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    color = SubTextColor
                )
            )

            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clip(shape = CircularShape)
                    .background(color = PrimaryColor.copy(alpha = 0.2f))
            ) {
                Text(
                    text = "On Going", style = TextStyle(
                        color = PrimaryColor
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Statistic",
            style = MaterialTheme.typography.h6.copy(color = SubTextColor)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(120.dp), contentAlignment = Alignment.Center
            ) {

                Canvas(modifier = Modifier.fillMaxSize(0.8f), onDraw = {
                    val points = listOf(
                        35f, 55f, 33f
                    )
                    val colors = listOf(
                        PrimaryColor, PrimaryColorLight, SecondaryColor
                    )
                    drawChart(points, colors)
                })

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "75%",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    )
                    Text(text = "Done", style = TextStyle(color = SubTextColor))
                }


            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                BuildDotAndTitle(color = PrimaryColor, text = "Finish on time")
                Spacer(modifier = Modifier.height(16.dp))
                BuildDotAndTitle(color = PrimaryColorLight, text = "Past the deadline")
                Spacer(modifier = Modifier.height(16.dp))
                BuildDotAndTitle(color = SecondaryColor, text = "Still ongoing")
            }

        }
    }
}


@Composable
private fun BuildDocumentProcessSection() {
    Card(shape = MaterialTheme.shapes.large, modifier = Modifier.padding(16.dp)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            Column {
                Text(
                    text = "Daily Task", style = TextStyle(
                        color = SubTextColor,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                val annotatedString1 = AnnotatedString.Builder("4/6 Task")
                    .apply {
                        addStyle(
                            SpanStyle(
                                color = PrimaryColor,
                            ), 0, 3
                        )
                    }


                Row(modifier = Modifier.padding(top = 8.dp)) {
                    BuildTickIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = annotatedString1.toAnnotatedString(),

                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Almost finished, keep it up", style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {}, shape = CircularShape, colors = ButtonDefaults.buttonColors(
                        backgroundColor = PrimaryColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Daily Task")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            DrawProgressCircle()

        }
    }
}

@Composable
private fun BuildTickIcon() {

    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircularShape)
            .background(color = PrimaryColor), contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
    }

}

@Composable
private fun DrawProgressCircle() {
    Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize(0.9f)) {
            drawCircle(
                color = BackgroundColor,
                style = Stroke(
                    width = 28f
                ),
                radius = size.minDimension / 2f

            )

            drawArc(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        PrimaryColorLight,
                        PrimaryColor
                    )
                ),
                startAngle = 270f,
                sweepAngle = 220f,
                useCenter = false,
                style = Stroke(26f, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "80%", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
            Text(text = "Done", style = TextStyle(color = SubTextColor))
        }
    }
}

@Composable
private fun BuildAppBarSection() {
    Row(Modifier.padding(16.dp)) {

        Column {
            Text("Hello, Akash Das", style = MaterialTheme.typography.h6)
            Text("let's do your today task", style = TextStyle(color = SubTextColor))
        }

        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.test_logo),
            contentDescription = null,
            Modifier
                .size(40.dp)
                .clip(shape = CircularShape)
        )
    }
}