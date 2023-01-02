package com.a2z.app.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    text: String,
    modifier : Modifier = Modifier,
    icon: @Composable () -> Unit = {},
    isEnable: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = isEnable,
        modifier = modifier,
        shape = shape
    ) {
        icon()
        Text(text = text, modifier = Modifier.padding(horizontal = 12.dp))
    }
}