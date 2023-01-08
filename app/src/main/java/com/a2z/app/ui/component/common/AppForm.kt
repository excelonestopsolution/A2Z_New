package com.a2z.app.ui.component.common


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.ui.component.WalletBalanceComponent


data class AppFormCard(
    val title: String? = null,
    val isVisible: Boolean = true,
    val contents: @Composable ColumnScope.() -> Unit,
)


@Composable
fun AppFormUI(
    button: @Composable () -> Unit,
    showWalletCard: Boolean =true,
    cardContents: List<AppFormCard>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {

        Box(modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                cardContents.forEach {
                 if(it.isVisible)   Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                            if (it.title != null) {
                                Text(text = it.title, style = MaterialTheme.typography.h6.copy(
                                    fontSize = 16.sp
                                ))
                                Spacer(modifier = Modifier.height(1.dp))
                            }
                            if (it.isVisible) it.contents(this)
                        }
                    }
                }
                if (showWalletCard) WalletBalanceComponent()
            }
        }
        button()
    }
}
