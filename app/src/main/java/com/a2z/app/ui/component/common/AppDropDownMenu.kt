@file:OptIn(ExperimentalMaterialApi::class)

package com.a2z.app.ui.component.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Input
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun AppDropDownMenu(
    selectedState: MutableState<String>,
    label: String,
    onSelect: ((String) -> Unit?)? = null,
    list: List<String>
) {

    var expanded by remember { mutableStateOf(false) }

   Column {
       Spacer(modifier = Modifier.height(4.dp))
       ExposedDropdownMenuBox(
           expanded = expanded,
           onExpandedChange = {
               expanded = !expanded
           }, modifier = Modifier.fillMaxWidth()
       ) {
           TextField(
               readOnly = true,
               value = selectedState.value,
               onValueChange = {onSelect?.invoke(it) },
               label = { Text(label) },
               leadingIcon = { Icon(imageVector = Icons.Default.Input, contentDescription = null)},
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
       Spacer(modifier = Modifier.height(4.dp))
   }
}