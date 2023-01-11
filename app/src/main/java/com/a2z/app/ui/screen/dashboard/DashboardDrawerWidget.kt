package com.a2z.app.ui.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.a2z.app.data.local.AppPreference
import com.a2z.app.ui.component.common.AppNetworkImage
import com.a2z.app.util.VoidCallback

@Composable
fun ColumnScope.DashboardDrawerWidget(appPreference: AppPreference) {
    Box(
        modifier = Modifier
            .weight(1f)
            .background(color = MaterialTheme.colors.primary.copy(alpha = 0.050f))
    ) {
        Column(Modifier.scrollable(rememberScrollState(), Orientation.Vertical)) {

            BuildNavLogo(appPreference)

            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(color = Color.Gray)
            )

            BuildSingleMenu(Icons.Default.Home, "Home") {}
            BuildSingleMenu(Icons.Default.Home, "Home") {}
            BuildSingleMenu(Icons.Default.Home, "Home") {}
            BuildSingleMenu(Icons.Default.Home, "Home") {}
        }
    }
    Box {
        Icon(imageVector = Icons.Default.Close, contentDescription = null)
    }
}


@Composable
private fun BuildSingleMenu(painter: ImageVector, title: String, onClick: VoidCallback) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 5.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier
            .clickable { onClick() }
            .padding(12.dp)) {
            Icon(imageVector = painter, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, style = MaterialTheme.typography.h6)
        }
    }
}


@Composable
private fun BuildNavLogo(appPreference: AppPreference) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .padding(12.dp)
            .background(
                color = MaterialTheme.colors.primary.copy(alpha = 0.0f),
                shape = MaterialTheme.shapes.large
            ),

        contentAlignment = Alignment.Center

    ) {

        Row {
            AppNetworkImage(
                url = appPreference.user?.profilePicture.toString(),
                shape = CircleShape, size = 90
            )
        }


    }
}