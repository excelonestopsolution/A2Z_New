package com.a2z.app.ui.screen.utility.recharge

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.data.model.utility.RechargeOffer
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.theme.RedColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ROfferDialog(viewModel: RechargeViewModel = hiltViewModel()) {
    if (!viewModel.rOfferDialogState.value) return
    LocalNavController.current
    Dialog(
        onDismissRequest = {
            viewModel.rOfferDialogState.value = false
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = BackgroundColor) {
            ObsComponent(flow = viewModel.rOfferFlow) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    AppBar(viewModel)
                    if (it.status.equals("success", ignoreCase = true))
                        BuildList(it.offers) { offer ->
                            viewModel.input.amountInputWrapper.input.value = offer.price
                            viewModel.rOfferState.value = offer
                            viewModel.rOfferDialogState.value = false
                            viewModel.input.validate()
                        }
                    else Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Offer Found!", style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = RedColor
                            )
                        )
                    }
                }
            }
        }
    }


}


@Composable
private fun BuildList(it: List<RechargeOffer>, onClick: (RechargeOffer) -> Unit) {
    LazyColumn(content = {
        items(it) {
            Card(modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 5.dp)
                .fillMaxWidth()
                .clickable {
                    onClick(it)
                }
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "₹ " + it.price, style = MaterialTheme.typography.h6.copy(
                            color = MaterialTheme.colors.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        it.offer, style = MaterialTheme.typography.subtitle1.copy(
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            lineHeight = 24.sp
                        )
                    )
                }
            }
        }
    })
}

@Composable
private fun AppBar(viewModel: RechargeViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        IconButton(onClick = {
            viewModel.rOfferDialogState.value = false
        }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
        Text(
            text = "R-Offer", style = MaterialTheme.typography.h6.copy(
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            )
        )
    }
}


