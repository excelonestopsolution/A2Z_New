package com.a2z.app.ui.screen.fund.payment_gateway

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.PGResultType
import com.a2z.app.R
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.theme.YellowColor
import com.a2z.app.util.VoidCallback
import org.json.JSONObject

@Composable
fun PgResultDialog(
    state: MutableState<PGResultType>,
    callback: VoidCallback
) {

    if (state.value != PGResultType.Nothing) Dialog(
        onDismissRequest = {
            state.value = PGResultType.Nothing
            callback.invoke()
        },
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(Color.White, MaterialTheme.shapes.small)
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            val icon = when (state.value) {
                is PGResultType.Failure -> R.drawable.icon_failed
                is PGResultType.Success -> R.drawable.icon_sucess
                is PGResultType.Nothing -> R.drawable.icon_pending
            }

            val color = when (state.value) {
                is PGResultType.Failure -> RedColor
                is PGResultType.Success -> GreenColor
                is PGResultType.Nothing -> YellowColor
            }

            val status = when (state.value) {
                is PGResultType.Failure -> "Failed"
                is PGResultType.Nothing -> "Pending"
                is PGResultType.Success -> "Success"
            }

            val statusDesc = when (state.value) {
                is PGResultType.Failure -> {
                    val str = (state.value as PGResultType.Failure).response.toString()
                    val json = JSONObject(str)
                    json.optJSONObject("error")?.optString("description")
                        ?: "something went wrong!"
                }
                is PGResultType.Nothing -> "Please check report"
                is PGResultType.Success -> (state.value as PGResultType.Success).razorpayPaymentId.toString()
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

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = status, style = MaterialTheme.typography.h6.copy(
                    color = color,
                    fontWeight = FontWeight.Bold,
                )
            )

            Text(
                text = statusDesc, style = MaterialTheme.typography.subtitle2.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                state.value = PGResultType.Nothing
                callback.invoke()
            }, modifier = Modifier.widthIn(min = 176.dp)) {
                Text(text = "Done")
            }

        }

    }
}