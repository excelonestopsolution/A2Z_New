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
import com.a2z.app.ui.component.common.SearchTextField
import com.a2z.app.ui.util.getHeightWidth
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.util.AppUtil

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SpinnerSearchDialog(

    state: MutableState<Boolean>,
    list: ArrayList<String>,
    title: String = "Select",
    onClick: (String) -> Unit
) {


    if (state.value) Dialog(
        onDismissRequest = {
            state.value = false
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {

        val renderList = remember { mutableListOf(*list.toTypedArray()) }


        val selectedItem = rememberStateOf<String?>(value = null)
        var searchInput by rememberStateOf(value = "")


        val (width, height) = getHeightWidth()

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(width = width * 0.90f)
                .padding(vertical = height * 0.07f)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top

            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                )
                Divider(thickness = 2.dp)

                SearchTextField(
                    value = searchInput,
                    onQuery = { value ->
                        searchInput = value
                        if (value.isNotEmpty())
                        {
                            val newLists =  list.filter {
                                it.contains(value, ignoreCase = true)
                            }
                            renderList.clear()
                            renderList.addAll(newLists)

                            AppUtil.logger(newLists)
                        }
                        else {
                            renderList.clear()
                            renderList.addAll(list)
                        }
                    },
                    onClear = {
                        searchInput = ""
                        renderList.clear()
                        renderList.addAll(list)
                    })

                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(renderList) {
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