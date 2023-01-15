package com.a2z.app.ui.screen.util.scheme_detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.a2z.app.data.model.report.CommissionSchemeDetail
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.TitleValueHorizontally
import com.a2z.app.ui.theme.BackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SchemeDetailScreen() {

    val viewModel: SchemeDetailViewModel = viewModel()

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = viewModel.data.title ?: "") }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = 12.dp,
                vertical = 8.dp
            )
        ) {

            items(viewModel.data.result!!) {

                Card(modifier = Modifier.padding(vertical = 4.dp)) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)

                    ) {
                        TitleValueHorizontally(title = "Service", value = it.service)
                        TitleValueHorizontally(title = "Min Amount", value = it.minAmount)
                        TitleValueHorizontally(title = "Max Amount", value = it.maxAmount)
                        TitleValueHorizontally(title = "Agent Charge", value = it.agentCharge)
                        TitleValueHorizontally(
                            title = "Agent Commission",
                            value = it.agentCommission
                        )
                    }
                }
            }
        }
    }


}