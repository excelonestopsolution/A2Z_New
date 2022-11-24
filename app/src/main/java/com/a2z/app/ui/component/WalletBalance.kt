package com.a2z.app.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.screen.AppViewModel
import com.a2z.app.ui.theme.GreenColor

@Composable
fun WalletBalanceComponent() {
    val viewModel: AppViewModel = hiltViewModel()

    Card(
        modifier = Modifier.padding(
            horizontal = 12.dp,
            vertical = 5.dp
        )
    ) {
        BaseContent(viewModel) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(16.dp)
                ) {
                    BuildDebitedFromText()
                    Spacer(modifier = Modifier.height(8.dp))
                    BuildIconAndTitle()
                    Spacer(modifier = Modifier.height(5.dp))
                    BuildAvailableBalanceText(viewModel)

                }
                Spacer(modifier = Modifier.weight(1f))
                BuildRefreshButton(viewModel)
            }
        }

    }

}

@Composable
private fun BuildDebitedFromText() {
    Text(
        text = "Debited From", style = MaterialTheme.typography.subtitle1.copy(
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
private fun BuildIconAndTitle() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.AccountBalanceWallet,
            contentDescription = null,
            Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "A2Z Suvidhaa Wallet",
            style = MaterialTheme.typography.subtitle1.copy(
                color = Color.Gray
            )
        )
    }
}

@Composable
private fun BuildAvailableBalanceText(viewModel: AppViewModel) {
    Text(
        text = "Available Wallet Balance : ₹ ${viewModel.balanceObs.value}", style = TextStyle(
            fontWeight = FontWeight.Bold, color = GreenColor, fontSize = 12.sp
        )
    )
}

@Composable
private fun BuildRefreshButton(viewModel: AppViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp)
    ) {

        IconButton(onClick = {
            viewModel.fetchWalletBalance()
        }) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                Modifier.size(32.dp)
            )
        }
        Text(text = "Refresh", style = TextStyle(color = Color.Gray))

    }
}