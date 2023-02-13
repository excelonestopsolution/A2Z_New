package com.a2z.app.ui.screen.home.di_md.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.data.model.report.AgentRequestView
import com.a2z.app.ui.component.TitleValueHorizontally
import com.a2z.app.ui.component.common.AppDropDownMenu
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.util.VoidCallback
import com.a2z.app.util.extension.prefixRS

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AgentRequestApproveDialog(
    state: MutableState<Boolean>,
    remarkList: List<Pair<String, String>>,
    statusList: List<Pair<String, String>>,
    item: AgentRequestView?,
    onProceed: (String,String,String) ->Unit
) {

    if (state.value) Dialog(
        onDismissRequest = { state.value = false },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(MaterialTheme.shapes.small)
                .background(Color.White)
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Approve Request",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )

            Divider(modifier = Modifier.padding(16.dp))


            BuildRequestInfo(item)


            val remarkState = rememberStateOf(value = remarkList.first().second)
            val statusState = rememberStateOf(value = statusList.first().second)
            val remarkInputState = rememberStateOf(value = "")

            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .border(1.dp, PrimaryColor, MaterialTheme.shapes.small)
            ) {
                AppDropDownMenu(
                    selectedState = remarkState,
                    label = "Remark",
                    list = remarkList.map { it.second },
                    onSelect = {
                        remarkState.value = it
                        Unit
                    }
                )
                AppDropDownMenu(
                    selectedState = statusState,
                    label = "Status",
                    list = statusList.map { it.second },
                    onSelect = {
                        statusState.value = it
                        Unit
                    }
                )
                AppTextField(value = remarkInputState.value, label = "Remark", onChange = {
                    remarkInputState.value = it
                })
            }


            Spacer(modifier = Modifier.height(16.dp))


            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { state.value = false },
                    modifier = Modifier.height(52.dp)
                ) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val remarkValue = remarkList.find {
                            it.second == remarkState.value
                        }
                        val statusValue = statusList.find {
                            it.second == statusState.value
                        }
                        val remark = remarkState.value

                        state.value = false
                        onProceed.invoke(remarkValue!!.first,statusValue!!.first,remark)


                    }, modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                ) {
                    Text(text = "Confirm & Approve")
                }
            }

        }
    }

}

@Composable
private fun BuildRequestInfo(item: AgentRequestView?) {

    Column(
        modifier = Modifier
            .border(1.dp, PrimaryColor, MaterialTheme.shapes.small)
            .padding(8.dp)
    ) {

        TitleValueHorizontally(title = "Amount", value = item?.amount?.prefixRS())
        TitleValueHorizontally(title = "Agent Name", value = item?.user_name)
        TitleValueHorizontally(title = "Ref Id", value = item?.ref_id)
        TitleValueHorizontally(title = "Bank Charge", value = "0.0".prefixRS())

    }

}