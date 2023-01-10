package com.a2z.app.ui.screen.report

import android.graphics.Color
import android.widget.TextView
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.theme.YellowColor

object ReportUtil {
    fun getColorFromId(statusId: Int?) : androidx.compose.ui.graphics.Color {
        return  when (statusId) {
            1,7,24-> GreenColor
            2,6 -> RedColor
            3 -> YellowColor
            else -> PrimaryColorDark
        }
    }
}