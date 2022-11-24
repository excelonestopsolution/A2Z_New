package com.a2z.app.ui.screen.test

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.R
import com.a2z.app.ui.screen.dashboard.DashboardBottomBarWidget
import com.a2z.app.ui.screen.home.component.HomeAppBarWidget
import com.a2z.app.ui.screen.home.component.HomeServiceWidget
import com.a2z.app.ui.theme.*
import com.a2z.app.ui.util.standardQuadFromTo

@Composable
fun TestHomePage() {
    Scaffold(backgroundColor = BackgroundColor, bottomBar = { DashboardBottomBarWidget() }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            HomeAppBarWidget()
            BuildWalletInfoSection()
            BuildMessageSection()
            KycDetailSection()
            HomeServiceWidget()
        }
    }
}

@Composable
private fun BuildMessageSection() {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Message,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = PrimaryColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Welcome back! how is your day (:", modifier = Modifier.weight(1f))
        }
    }
}


@Composable
private fun BuildWalletInfoSection() {

    val size = remember { mutableStateOf(IntSize(width = 0, height = 0)) }

    Box(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .onSizeChanged {
                size.value = it
            }
            .clip(shape = MaterialTheme.shapes.large)
            .background(PrimaryColorDark),
    ) {


        val width = size.value.width
        val height = size.value.height

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
                color = PrimaryColor
            )
            drawPath(
                path = lightColoredPath,
                color = PrimaryColorDark
            )
        }


        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = null,
                    Modifier
                        .size(90.dp)
                        .clip(CircularShape)
                        .border(width = 2.dp, color = Color.White, shape = CircularShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Welcome, Mr. Das",
                    style = TextStyle(
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Available Balance",
                    style = TextStyle(color = Color.White.copy(alpha = 0.8f))
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹ 10,000",
                        style = TextStyle(
                            fontSize = 24.sp, fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            Modifier.size(32.dp),
                            tint = Color.White
                        )
                    }
                }

                OutlinedButton(onClick = { }, shape = CircularShape) {
                    Text(text = "Top-Up")
                }
            }

        }

    }
}

@Composable
private fun KycDetailSection() {


    @Composable
    fun BuildTickIcon(color: Color) {

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircularShape)
                .background(color = color), contentAlignment = Alignment.Center
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
    fun BuildDocumentList(status: Int, title: String) {

        val color = when (status) {
            1 -> GreenColor
            2 -> Color.Gray
            else -> PrimaryColor

        }

        Row(modifier = Modifier.padding(top = 8.dp)) {
            BuildTickIcon(color)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                color = color,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }

    @Composable
    fun DrawProgressCircle() {
        Box(modifier = Modifier.size(90.dp), contentAlignment = Alignment.Center) {
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
                    style = Stroke(20f, cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "75%",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
                Text(text = "Done", style = TextStyle(color = SubTextColor))
            }
        }
    }


    Card(shape = MaterialTheme.shapes.large, modifier = Modifier.padding(16.dp)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            Column {
                Text(
                    text = "Kyc Details", style = TextStyle(
                        color = SubTextColor,
                        fontWeight = FontWeight.SemiBold
                    )
                )





                BuildDocumentList(1, "Pan Card Kyc")
                BuildDocumentList(1, "AEPS Kyc")
                BuildDocumentList(1, "Document Kyc")
                BuildDocumentList(2, "Aadhaar Kyc")

            }

            Spacer(modifier = Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                DrawProgressCircle()
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {}, shape = CircularShape
                ) {
                    Text(text = "Complete it ")
                }
            }

        }
    }
}
