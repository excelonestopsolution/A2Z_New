package com.a2z.app.ui.screen.fund.method

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.data.model.fund.FundMethod
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.LocalNavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FundMethodScreen() {
    val viewModel: FundMethodViewModel = hiltViewModel()
    val util = viewModel.util
    val navController = LocalNavController.current

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Select Fund Method") },
        content = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                content = {
                    items(util.methodList.size) {
                        BuildItem(util.methodList[it], onClick = { item ->
                            when (item.type) {
                                FundMethodType.UPI,
                                FundMethodType.PAYMENT_GATEWAY -> {
                                }
                                else -> {

                                    navController
                                        .navigate(NavScreen.FundBankListScreen.passData(item))
                                }
                            }
                        })
                    }
                })
        }
    )
}

@Composable
private fun BuildItem(item: FundMethod, onClick: (FundMethod) -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .clickable {
            onClick.invoke(item)
        }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = item.drawable),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                item.name, style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            )
        }
    }
}