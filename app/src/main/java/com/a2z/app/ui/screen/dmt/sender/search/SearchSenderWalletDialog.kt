package com.a2z.app.ui.screen.dmt.sender.search

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.data.model.dmt.SenderAccountDetail
import com.a2z.app.ui.screen.dmt.util.DMTType
import com.a2z.app.util.AppConstant
import com.a2z.app.util.VoidCallback


@Composable
fun SearchSenderWalletDialog(
    senderAccountDetail: MutableState<SenderAccountDetail?>,
    onWalletSelect: (DMTType) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(12.dp)
    ) {
        Text(
            text = "Select DMT Wallet",
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            BuildItem(
                icon = com.a2z.app.R.drawable.ic_launcher_money,
                title = "Wallet 1",
                balance = senderAccountDetail.value?.remainingLimit.toString(),
                onClick = {
                    onWalletSelect.invoke(DMTType.WALLET_1)
                }
            )
            BuildItem(
                icon = com.a2z.app.R.drawable.ic_launcher_money,
                title = "Wallet 2",
                balance = senderAccountDetail.value?.remainingLimit2.toString(),
                onClick = {
                    onWalletSelect.invoke(DMTType.WALLET_2)
                }
            )
            BuildItem(
                icon = com.a2z.app.R.drawable.ic_launcher_money,
                title = "Wallet 3",
                balance = senderAccountDetail.value?.remainingLimit3.toString(),
                onClick = {
                    onWalletSelect.invoke(DMTType.WALLET_3)
                }
            )
        }
    }
}

@Composable
private fun RowScope.BuildItem(
    @DrawableRes icon: Int,
    title: String,
    balance: String,
    onClick: VoidCallback
) {
    Card(modifier = Modifier
        .weight(1f)
        .padding(8.dp)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    onClick.invoke()
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = icon),
                contentDescription = title,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                modifier = Modifier.size(52.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp
            )

            Text(
                text = AppConstant.RUPEE_SYMBOL + balance,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp
            )


        }
    }
}