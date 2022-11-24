package com.a2z.app.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.ui.util.getHeightWidth

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SpinnerSearchDialog(

    state: MutableState<Boolean>,
    list: ArrayList<String>,
    initialSelectedValue : String? = null,
    title : String = "Select",
    onClick: (String) -> Unit
) {


    val selectedItem = rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val selectedItemState = remember {
        derivedStateOf {
            if (selectedItem.value == null) null
            else list.find { it == selectedItem.value }
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        if(initialSelectedValue!=null){
            val item = list.find { it == initialSelectedValue }
            if(item!=null) selectedItem.value = initialSelectedValue
        }
    })


    val (width, height) = getHeightWidth()

    if (state.value) Dialog(
        onDismissRequest = {
            state.value = false
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {

        Box(
            modifier = Modifier
                .wrapContentHeight()
                .width(width = width * 0.90f)
                .padding(vertical = height * 0.07f)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                )
                Divider(thickness = 2.dp)

                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(list) {
                        Column(modifier = Modifier.clickable {
                            selectedItem.value = it
                            state.value = false
                            onClick(it)

                        }) {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.subtitle2,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color =
                                        when (selectedItemState.value) {
                                            null -> Color.Transparent
                                            it -> Color.Blue.copy(alpha = 0.1f)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .padding(vertical = 12.dp, horizontal = 5.dp)

                            )
                            Divider()
                        }
                    }
                }

            }

        }
    }

}