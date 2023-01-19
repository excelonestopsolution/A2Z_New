package com.a2z.app.ui.screen.report.matm

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
fun MATMReportScreen() {

    val viewModel : MTAMReportViewModel = hiltViewModel()
    Scaffold (
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "M-ATM Request Report")}){

        BaseContent(viewModel) {
            ObsComponent(flow = viewModel.reportResultFlow) {
                LazyColumn{
                   items(it.data!!){
                       BaseReportItem(
                           statusId = it.status,
                           leftSideDate = it.txn_time,
                           leftSideId = it.txn_id,
                           centerHeading1 = it.customer_number,
                           centerHeading2 = it.transaction_type,
                           centerHeading3 = it.card_type,
                           rightAmount = it.transaction_amount,
                           rightStatus = it.status_desc,
                           expandListItems = listOf()
                       )
                   }
                }
            }
        }
    }
}