package com.a2z.app.ui.component

import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.a2z.app.util.AppConstant
import com.a2z.app.util.extension.isValidateDate
import com.a2z.app.util.extension.showToast

@Composable
fun DatePickerDialog(
    onCancelled: () -> Unit,
    preSelectedDate: String? = null,
    onDatePicked: (date: String) -> Unit,
) {

    val preYear by remember { mutableStateOf(1998) }
    val preMonth by remember { mutableStateOf(0) }
    val preDay by remember { mutableStateOf(1) }

    val context = LocalContext.current
    context.showToast(preSelectedDate.toString())
    val datePicker = android.app.DatePickerDialog(
        context,
        { _: DatePicker, year: Int, m: Int, d: Int ->
            val m2 = m + 1

            val day = if (d > 9) "$d" else "0$d"
            val month = if (m2 > 9) "$m2" else "0$m2"

            val mDate = "$day${AppConstant.DATE_SEPARATOR}$month${AppConstant.DATE_SEPARATOR}$year"
            onDatePicked.invoke(mDate)
        },
        preYear,
        preMonth,
        preDay
    )

    datePicker.setOnCancelListener { onCancelled.invoke() }

    if (preSelectedDate.isValidateDate()) {
        preSelectedDate!!.split(AppConstant.DATE_SEPARATOR).let { dateList ->
            val mYear = dateList[2].toInt()
            val mMonth = dateList[1].toInt() - 1
            val mDay = dateList[0].toInt()
            datePicker.updateDate(mYear, mMonth, mDay)
        }
    }



    datePicker.show()
}
