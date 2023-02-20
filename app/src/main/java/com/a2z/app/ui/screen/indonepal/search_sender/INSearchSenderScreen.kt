package com.a2z.app.ui.screen.indonepal.search_sender

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.CircularShape

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun INSearchSenderScreen() {

    val viewModel: INSearchSenderViewModel = hiltViewModel()

    Scaffold(
        topBar = { NavTopBar(title = "Search Sender") },
        backgroundColor = BackgroundColor,

        ) {

        BaseContent(viewModel) {
            Card(Modifier.padding(12.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Search By Mobile",
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                    )


                    Spacer(modifier = Modifier.height(8.dp))

                    AppTextField(
                        keyboardType = KeyboardType.Number,
                        value = viewModel.input.mobileNumber.getValue(),
                        label = "Mobile Number",
                        isOutline = true,
                        maxLength = 10,
                        onChange = { viewModel.input.mobileNumber.onChange(it) },
                        error = viewModel.input.mobileNumber.formError(),
                        trailingIcon = {
                            Button(
                                enabled = viewModel.input.isValidObs.value,
                                onClick = {
                                    viewModel.onSearchClick()
                                },
                                contentPadding = PaddingValues(
                                    horizontal = 12.dp
                                ),
                                shape = CircularShape,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(end = 8.dp)
                            ) {
                                Text(text = "Search")
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                            }
                        })
                }
            }
        }

    }
}
