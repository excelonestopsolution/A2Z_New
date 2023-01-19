package com.a2z.app.ui.screen.report.aeps

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.screen.report.component.BaseReportItem
import com.a2z.app.ui.theme.BackgroundColor


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AEPSReportScreen() {

    val viewModel: AEPSReportViewModel = hiltViewModel()
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "AEPS Report") }) {

        BaseContent(viewModel) {
            ObsComponent(flow = viewModel.reportResultFlow) {
                LazyColumn {
                    items(it.data!!) {
                        BaseReportItem(
                            statusId = it.status_id,
                            leftSideDate = it.created_at,
                            leftSideId = it.api_name,
                            centerHeading1 = it.number,
                            centerHeading2 = it.txn_type,
                            centerHeading3 = it.bank_name,
                            rightAmount = it.amount,
                            rightStatus = it.status,
                            isPrint = it.is_print ?: false,
                            isCheckStatus = it.is_check_status ?: false,
                            isComplaint = it.is_complain ?: false,
                            expandListItems = listOf()
                        )
                    }
                }
            }
        }
    }
}