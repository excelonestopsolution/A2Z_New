package com.a2z.app.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    const val FORMAT_DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss"
    fun Date.toFormat(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    val currentDate: Date
        get() = Calendar.getInstance().time

    fun isValidDate(inDate: String): Boolean {
        val dateFormat =
            SimpleDateFormat("dd${AppConstant.DATE_SEPARATOR}MM${AppConstant.DATE_SEPARATOR}yyyy")
        dateFormat.isLenient = false
        try {
            dateFormat.parse(inDate)
        } catch (pe: ParseException) {
            return false
        }
        return true
    }


}