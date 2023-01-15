package com.a2z.app.ui.screen.util.commission

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.theme.BackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyCommissionScreen() {

    val viewModel: CommissionViewModel = hiltViewModel()

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "My Commissions") }
    ) {


        BaseContent(viewModel) {

            ObsComponent(flow = viewModel.schemeResponseResultFlow) {
                Card(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize(), shape = MaterialTheme.shapes.medium
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
                    ) {
                        items(it.result!!) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.clickable {
                                    viewModel.onSchemeItemClick(it)
                                }
                            ) {
                                Text(
                                    text = it.title,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                                )
                                Divider()
                            }
                        }
                    }
                }
            }

        }


    }
}