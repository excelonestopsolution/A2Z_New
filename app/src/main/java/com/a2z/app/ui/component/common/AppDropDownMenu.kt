@file:OptIn(ExperimentalMaterialApi::class)

package com.a2z.app.ui.component.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun AppDropDownMenu(
    selectedState : MutableState<String>,
    label : String,
    list : List<String>
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }, modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            readOnly = true,
            value = selectedState.value,
            onValueChange = { },
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                backgroundColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            list.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedState.value = selectionOption
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}