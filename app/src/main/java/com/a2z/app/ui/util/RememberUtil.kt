package com.a2z.app.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.a2z.app.data.model.report.LedgerReportResponse


@Composable
fun <T> rememberStateOf(value: T) = remember {
    mutableStateOf(value)
}

