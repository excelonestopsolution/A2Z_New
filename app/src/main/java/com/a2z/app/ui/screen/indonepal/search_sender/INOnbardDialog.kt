package com.a2z.app.ui.screen.indonepal.search_sender

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.a2z.app.ui.component.common.AppDropDownMenu
import com.a2z.app.ui.screen.indonepal.INUtil
import com.a2z.app.ui.theme.PrimaryColorLight
import com.a2z.app.ui.util.rememberStateOf

@Composable
fun INOnbaordDialog(
    state: MutableState<Boolean>,
    onProceed: (String, String, String) -> Unit
) {

    if (state.value) Dialog(onDismissRequest = { state.value = false }) {


        val customerTypeState = rememberStateOf("")
        val sourceIncomeTypeState = rememberStateOf("")
        val annualIncomeState = rememberStateOf("")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "Onbarod User",
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColorLight
                )
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            AppDropDownMenu(
                selectedState = customerTypeState,
                label = "Customer Type",
                list = INUtil.staticData().customerType.map { it.name }
            )

            AppDropDownMenu(
                selectedState = sourceIncomeTypeState,
                label = "Income Source Type",
                list = INUtil.staticData().sourceIncomeType.map { it.name }
            )

            AppDropDownMenu(
                selectedState = annualIncomeState,
                label = "Annual Income",
                list = INUtil.staticData().annualIncome.map { it.name }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                enabled = customerTypeState.value.trim().isNotEmpty() &&
                        sourceIncomeTypeState.value.trim().isNotEmpty() &&
                        annualIncomeState.value.trim().isNotEmpty(),
                onClick = {

                    val customerTypeId =
                        INUtil.staticData().customerType.find { it.name == customerTypeState.value }!!.id
                    val sourceIncomeTypeId =
                        INUtil.staticData().sourceIncomeType.find { it.name == sourceIncomeTypeState.value }!!.id
                    val annualIncomeId =
                        INUtil.staticData().annualIncome.find { it.name == annualIncomeState.value }!!.id

                    onProceed.invoke(customerTypeId, sourceIncomeTypeId, annualIncomeId)
                    state.value = false

                }, modifier = Modifier
                    .height(52.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Proceed")
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}
