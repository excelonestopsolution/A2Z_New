package com.a2z.app.ui.screen.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.a2z.app.R
import com.a2z.app.ui.screen.dashboard.DashboardViewModel
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.ShapeZeroRounded

@Composable
fun HomeAppBarWidget(
    viewModel : DashboardViewModel? = null
) {
    Card(modifier = Modifier.fillMaxWidth(),
        shape = ShapeZeroRounded,
        elevation = 16.dp) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                val scope = rememberCoroutineScope()
                Card(shape = CircularShape, modifier = Modifier.clickable {
                   /* scope.launch {
                        viewModel?.scaffoldState?.let {
                            if(it.drawerState.isOpen){
                                it.drawerState.close()
                            }
                            else it.drawerState.open()
                        }
                    }*/
                }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(12.dp)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier.height(40.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                BuildActionIcon(Icons.Default.QrCode)
                Spacer(modifier = Modifier.width(8.dp))
                BuildActionIcon(Icons.Default.Notifications)

            }

        }
    }
}

@Composable
private fun BuildActionIcon(icon: ImageVector) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colors.primary.copy(alpha = 0.050f))
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