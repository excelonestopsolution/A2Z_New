package com.a2z.app.ui.screen.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.a2z.app.R
import com.a2z.app.ui.screen.dashboard.DashboardViewModel
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.theme.ShapeZeroRounded
import kotlinx.coroutines.launch

@Composable
fun HomeAppBarWidget(
    viewModel: DashboardViewModel? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = ShapeZeroRounded,
        elevation = 16.dp,
        backgroundColor = PrimaryColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {

            val scope = rememberCoroutineScope()

            Column(horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable {


                        scope.launch {
                            viewModel?.scaffoldState?.let {
                                if (it.drawerState.isOpen) {
                                    it.drawerState.close()
                                } else it.drawerState.open()
                            }
                        }

                    }) {
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(10.dp)
                        .clip(CircularShape)
                        .background(Color.White)
                        .clip(CircularShape)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(12.dp)
                        .clip(CircularShape)
                        .background(Color.White)
                        .clip(CircularShape)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(16.dp)
                        .clip(CircularShape)
                        .background(Color.White)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .width(86.dp)
                    .height(48.dp), contentAlignment = Alignment.Center
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(32.dp)
                        .width(70.dp)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.Black.copy(0.2f))
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            BuildActionIcon(Icons.Default.QrCode)
            Spacer(modifier = Modifier.width(8.dp))
            BuildActionIcon(Icons.Default.Notifications)

        }
    }
}

@Composable
private fun BuildActionIcon(icon: ImageVector) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(Color.White.copy(alpha = 0.10f))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp)
        )
    }
}