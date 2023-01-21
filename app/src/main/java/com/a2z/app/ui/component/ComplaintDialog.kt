package com.a2z.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.data.model.report.ComplainType
import com.a2z.app.ui.component.common.AppDropDownMenu
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.util.rememberStateOf

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComplaintDialog(
    complaintTypes: MutableList<ComplainType>,
    dialogState: MutableState<Boolean>,
    onSubmit : (String,String) ->Unit
) {

    if (dialogState.value)
        Dialog(
            onDismissRequest = {
                dialogState.value = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {

            val selectedTypeState = rememberStateOf(value = complaintTypes.first().name)
            val remarkState = rememberStateOf(value = "")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Make Complain",
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = {
                        dialogState.value = false
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                AppDropDownMenu(
                    selectedState = selectedTypeState,
                    label = "Select Reason",
                    list = complaintTypes.map { it.name }
                )

                AppTextField(value = remarkState.value, label = "Remark", onChange = {
                    remarkState.value = it
                })

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {

                        val complainTypeId =
                            complaintTypes.find { it.name == selectedTypeState.value }

                        dialogState.value = false
                        onSubmit(complainTypeId?.id.toString(), remarkState.value)


                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Submit")
                }
            }

        }


}