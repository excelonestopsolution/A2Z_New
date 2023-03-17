package com.a2z.app.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitleValueVertically(
    title: String,
    value: String?,
    useNA: Boolean = false
) {
   if(value != null && value.trim().isNotEmpty()) Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            title, style = TextStyle.Default.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black.copy(alpha = 0.7f)
            )
        )
        Text(value ?: if (useNA) "Not Available" else "")
        Divider()

    }
}


@Composable
fun TitleValueHorizontally(
    title: String,
    value: String?,
    useNA: Boolean = false,
    color : Color ? = null,
    titleWeight : Float?=null,
    valueWeight: Float? = null
) {



  if(value !=null && value.toString().isNotEmpty())  Column {
        Row(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
            Text(
                title, style = TextStyle.Default.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = color ?: Color.Black.copy(alpha = 0.7f)
                ),
                modifier = Modifier.weight(titleWeight ?: 1f)
            )
            Text(" : ")
            Text(value ?: if (useNA) "Not Available" else "",
                color = color ?: TextStyle.Default.color,
                modifier = Modifier.weight(valueWeight ?: 2f))


        }
        Divider()
    }
}