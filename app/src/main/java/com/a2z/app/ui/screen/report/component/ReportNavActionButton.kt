package com.a2z.app.ui.screen.report.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.a2z.app.util.VoidCallback

@Composable
fun ReportNavActionButton(
    icon: ImageVector = Icons.Default.ManageSearch,
    onClick: VoidCallback,
) {
    Icon(imageVector = icon,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .size(42.dp)
            .clip(MaterialTheme.shapes.small)
            .clickable { onClick.invoke() }
            .padding(4.dp)
    )
}