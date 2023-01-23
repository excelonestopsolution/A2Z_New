package com.a2z.app.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.a2z.app.ui.component.AppButton
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.util.VoidCallback

@Composable
fun BaseConfirmDialog(
    state: MutableState<Boolean>,
    amount: String,
    title: String = "Confirm",
    titleValues: List<Pair<String, String>>,
    onConfirm: VoidCallback
) {
    if (state.value) Dialog(
        onDismissRequest = {
            state.value = false
        }) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(5.dp))
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    title, style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = "₹ $amount/-",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = GreenColor,
                        fontSize = 22.sp
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(vertical = 12.dp)
                )

                titleValues.forEach {

                    if(it.second.isNotEmpty())
                        BuildTitleValue(title = it.first, value = it.second)

                }


                Spacer(modifier = Modifier.height(16.dp))
                AppButton(
                    text = "Confirm & Proceed",
                    modifier = Modifier
                        .height(48.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = CircleShape
                ) {
                    state.value = false
                    onConfirm()
                }

            }
        }

    }
}

@Composable
private fun BuildTitleValue(title: String, value: String) {

    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        Text(
            text = title, style = MaterialTheme.typography.subtitle2.copy(
                color = Color.Black.copy(alpha = 0.5f)
            )
        )
        Text(
            text = value, style = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
        Divider()
    }

}