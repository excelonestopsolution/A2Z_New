package com.a2z.app.ui.screen.report.filter

import android.widget.Spinner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.ui.component.common.AppDropDownMenu
import com.a2z.app.ui.component.common.DateTextField
import com.a2z.app.ui.component.common.DropDownTextField
import com.a2z.app.ui.dialog.SpinnerSearchDialog
import com.a2z.app.ui.screen.report.fund_transfer.FundTransferReportViewModel
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.util.DateUtil
import com.a2z.app.util.extension.removeDateSeparator


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FundTransferFilterDialog(
    showDialogState: MutableState<Boolean>,
    users: List<Pair<String, String>>,
    onFilter: (FundTransferReportViewModel.SearchInput) -> Unit
) {

    val currentDate = DateUtil.getDate()

    val userState = rememberStateOf(value = "")
    val startDateState = rememberStateOf(value = currentDate)
    val endDateState = rememberStateOf(value = currentDate)





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
            BaseReportFilterComponent(
                onFilter = {
                    val mUser = users.find { it.first == userState.value }
                    onFilter(
                        FundTransferReportViewModel.SearchInput(
                            startDate = startDateState.value,
                            endDate = endDateState.value,
                            user = mUser!!
                        )
                    )
                    showDialogState.value = false
                },
                content = {
                    DateTextField(

                        label = "Start Date",
                        value = startDateState.value,
                        onChange = { startDateState.value = it },
                        onDateSelected = {
                            startDateState.value = it.removeDateSeparator()
                        }
                    )

                    DateTextField(
                        label = "End Date",
                        value = endDateState.value,
                        onChange = { endDateState.value = it },
                        onDateSelected = {
                            endDateState.value = it.removeDateSeparator()
                        }
                    )


                    val spinnerState = rememberStateOf(value = false)
                    Spacer(modifier = Modifier.height(8.dp))
                    DropDownTextField(
                        value = userState.value,
                        hint = "Select User",
                        paddingValues = PaddingValues(horizontal = 0.dp)
                    ) {
                        spinnerState.value = true
                    }

                    SpinnerSearchDialog(
                        state = spinnerState,
                        list = users.map { it.first }.toList() as ArrayList<String>,
                        onClick ={
                            userState.value = it
                        }
                    )


                }
            )
        }

    }


}