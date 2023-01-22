package com.a2z.app.ui.screen.report.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.a2z.app.ui.component.common.AppDropDownMenu
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.DateTextField
import com.a2z.app.ui.screen.report.ledger.LedgerReportViewModel
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.util.DateUtil
import com.a2z.app.util.DateUtil.toFormat
import com.a2z.app.util.extension.removeDateSeparator


@Composable
fun ReportDateFilterComponent(
    onFilter: (startDate: String, endDate: String) -> Unit
) {

    val currentDate = DateUtil.getDate()
    val startDateState = rememberStateOf(value = currentDate)
    val endDateState = rememberStateOf(value = currentDate)



    BaseReportFilterComponent(
        onFilter = {

            onFilter(
                startDateState.value,
                endDateState.value
            )
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


        }
    )
}