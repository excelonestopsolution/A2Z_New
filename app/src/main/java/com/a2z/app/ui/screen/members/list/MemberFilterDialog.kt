package com.a2z.app.ui.screen.members.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.ui.component.common.AppDropDownMenu
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.keyboardAsState
import com.a2z.app.ui.screen.report.filter.BaseReportFilterComponent
import com.a2z.app.ui.util.rememberStateOf

private val inputTypeList = linkedMapOf(
    "All" to "",
    "Name" to "NAME",
    "Mobile" to "MOB",
    "ID" to "ID",
)


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MemberFilterDialog(
    showDialogState: MutableState<Boolean>,
    onFilter: (inputType: String, input: String) -> Unit
) {

    val inputTypeState = rememberStateOf(value = "")
    val inputState = rememberStateOf(value = "")



    if (showDialogState.value) Dialog(onDismissRequest = {
        showDialogState.value = false
    }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    Color.White,
                    MaterialTheme.shapes.small
                )
        ) {
            val manager = LocalFocusManager.current
            val keyboard = keyboardAsState()
            BaseReportFilterComponent(
                onFilter = {

                    manager.clearFocus()

                    val mInputType =
                        inputTypeList.entries.find { it.key == inputTypeState.value }?.value
                    onFilter(
                        mInputType.toString(), inputState.value.trim()
                    )
                    showDialogState.value = false
                },
                content = {


                    AppDropDownMenu(
                        selectedState = inputTypeState,
                        label = "Input Type",
                        list = inputTypeList.map { it.key },
                    )

                    val keyboardType =
                        if (inputTypeState.value == "Name") KeyboardType.Text else KeyboardType.Number
                    if(inputTypeState.value != "All")AppTextField(
                        value = inputState.value,
                        onChange = {
                            if (inputTypeState.value == "Name")
                                inputState.value = it
                            else {
                                if (it != "," && it != "-" && it != ".") inputState.value = it
                            }

                        },
                        label = "Input Search",
                        keyboardType = keyboardType,
                        maxLength = if(inputTypeState.value == "Mobile") 10 else 56
                    )

                }
            )
        }

    }


}