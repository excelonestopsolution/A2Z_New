package com.a2z.app.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.window.Dialog
import com.a2z.app.data.model.dmt.BankDownBank

@Composable
fun BankDownDialog(
    dialogState : MutableState<Boolean>,
    bankList : List<BankDownBank?>?
) {
    if(dialogState.value) Dialog(onDismissRequest = { dialogState.value = false }) {

        Column(modifier = Modifier
            .background(
                color = Color.White
            )
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Bank Down List", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            bankList?.forEachIndexed { index,bank ->
              Column {
                  Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(vertical = 5.dp)) {
                      Text(text = (index+1).toString()+" . ")
                      Spacer(modifier = Modifier.width(8.dp))
                      Text(text = bank?.bankName.toString(), modifier = Modifier.weight(1f))
                  }
                  Divider()
              }

            }
        }

    }
}