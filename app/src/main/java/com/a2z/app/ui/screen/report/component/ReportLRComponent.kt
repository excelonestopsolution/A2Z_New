package com.a2z.app.ui.screen.report.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ReportLFComponent(titleLeft: String, valueRight: String?) {
   if(valueRight != null && valueRight.isNotEmpty()) Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = titleLeft, modifier = Modifier.weight(1f), style =
                MaterialTheme.typography.body1.copy(
                    color = Color.Black.copy(0.9f)
                )
            )
            Text(text = "  :  ",
                style =  MaterialTheme.typography.body1.copy(
                    color = Color.Black.copy(0.9f),

                ))
            Text(text = valueRight.toString(), modifier = Modifier.weight(2f),
                style =  MaterialTheme.typography.body1.copy(
                    color = Color.Black.copy(1f)
                ))
        }
       Divider(modifier = Modifier.padding(top = 4.dp), color = Color.White.copy(alpha = 0.9f))
    }
}
