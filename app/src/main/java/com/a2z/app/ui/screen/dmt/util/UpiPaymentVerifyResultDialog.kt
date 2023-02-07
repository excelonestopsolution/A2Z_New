package com.a2z.app.ui.screen.dmt.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.PGResultType
import com.a2z.app.R
import com.a2z.app.data.model.dmt.Beneficiary
import com.a2z.app.data.model.dmt.UpiStaticMessage
import com.a2z.app.data.model.dmt.UpiVerifyPayment
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.theme.YellowColor
import com.a2z.app.util.VoidCallback


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UpiPaymentVerifyResultDialog(
    state: MutableState<Boolean>,
    data: UpiVerifyPayment?,
    onProceed: VoidCallback,
) {


    @Composable
    fun BuildTitleValue(title: String, value: String) {

        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Row {
                Text(
                    text = title, style = MaterialTheme.typography.subtitle2.copy(
                        color = Color.Black.copy(alpha = 0.5f),
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text(text = "    :    ", fontWeight = FontWeight.W500)
                Text(
                    text = value, style = MaterialTheme.typography.subtitle1.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier.weight(2.5f)
                )
            }
            Divider()
        }

    }



    if (state.value) Dialog(
        onDismissRequest = {
            state.value = false
        }, properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        if (data == null) return@Dialog
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val icon = when (data.status) {
                1, 34 -> R.drawable.icon_sucess
                else -> R.drawable.icon_pending
            }

            val color = when (data.status) {
                1, 34 -> GreenColor
                else -> YellowColor
            }


            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(color)
                    .padding(24.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                text = data.status_desc.toString(),
                fontSize = 14.sp,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = data.message,
                color = Color.Gray,
                fontSize = 12.sp
            )


            val titleValues = listOf(
                Pair("Operator Id", data.operator_id),
                Pair("Record Id", data.record_id),
                Pair("Txn Time", data.txn_time),
                Pair("Upi Id", data.account_number),
                Pair("Bank Name", data.bank_name),
                Pair("Amount", data.amount),
                Pair("Name", data.bene_name),
            )

            titleValues.forEach {

                if (it.second != null && it.second!!.isNotEmpty())
                    BuildTitleValue(title = it.first, value = it.second!!)

            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(
                    onClick = { state.value = false },
                ) {
                    Text(text = "Close")
                }

                Spacer(modifier = Modifier.width(16.dp))


                Button(
                    onClick = {
                        state.value = false
                        onProceed.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = GreenColor,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Continue Payment")
                }
            }


        }

    }


}