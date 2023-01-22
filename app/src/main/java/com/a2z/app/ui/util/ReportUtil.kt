package com.a2z.app.ui.util

object ReportUtil {
    fun getStatusId(value: String?) : Int {
       return if (value.equals("success", true) ||
            value.equals("complete", true) ||
            value.equals("active", true) ||
            value.equals("successfully submitted,true") ||
            value.equals("credit", true)
        ) {
            1
        } else if (
            value.equals("failure", true) ||
            value.equals("failed", true) ||
            value.equals("debit", true)
        ) {
            2
        } else {
            3
        }
    }
}