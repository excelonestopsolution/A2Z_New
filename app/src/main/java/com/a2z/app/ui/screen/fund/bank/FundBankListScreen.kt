package com.a2z.app.ui.screen.fund.bank

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.data.model.fund.FundRequestBank
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.theme.LocalNavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FundBankListScreen() {

    val navController = LocalNavController.current
    val viewModel: FundBankListViewModel = hiltViewModel()


    Scaffold(

        topBar = { NavTopBar(title = "Select Bank") },
        content = {
            BaseContent(viewModel) {
                ObsComponent(flow = viewModel.bankListFlow) {
                    if (it.status == 1) BuildListComponent(it.banks, viewModel.notes) { item ->
                        navController
                            .navigate(NavScreen.FundRequestScreen.passData(
                                viewModel.fundMethod,
                                item
                            ))
                    }
                }
            }
        }
    )
}

@Composable
private fun BuildListComponent(
    banks: List<FundRequestBank>,
    notes: List<String>?,
    onClick: (FundRequestBank) -> Unit
) {
    LazyColumn {
        item {
            notes?.let { BuildNote(it) }
        }
        items(banks.size) {
            Column(modifier = Modifier.clickable {
                onClick.invoke(banks[it])
            }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(banks[it].bankName ?: "", style = MaterialTheme.typography.subtitle1)
                        Text(banks[it].account_number ?: "", style = MaterialTheme.typography.subtitle2)
                        Text(banks[it].ifsc ?: "", style = MaterialTheme.typography.subtitle2)
                    }
                }
                Divider()
            }
        }
    }
}

@Composable
private fun BuildNote(notes: List<String>) {

    Card(modifier = Modifier.padding(8.dp), backgroundColor = MaterialTheme.colors.primary) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Note : ", style = MaterialTheme.typography.h6.copy(color = Color.White))
            Spacer(modifier = Modifier.height(8.dp))
            notes.forEach {
                Text(
                    it, style = TextStyle(
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    ), modifier = Modifier.padding(vertical = 4.dp)
                )
            }

        }
    }
}
