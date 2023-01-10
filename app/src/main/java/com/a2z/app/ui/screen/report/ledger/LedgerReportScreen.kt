package com.a2z.app.ui.screen.report.ledger

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.ui.component.AppLazyList
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.theme.BackgroundColor

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LedgerReportScreen() {

    val viewModel: LedgerReportViewModel = hiltViewModel()

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Ledger Report") }
    ) { _ ->

        val pagingData =viewModel.fetchLedgerReport.collectAsLazyPagingItems()

        val rememberPagingData = remember { pagingData }

        BaseContent(viewModel) {

            AppLazyList<LedgerReport>(rememberPagingData){
                val isExpanded = rememberSaveable { mutableStateOf(false) }
                Card(modifier = Modifier
                    .clickable {
                        isExpanded.value = !isExpanded.value
                    }
                    .padding(horizontal = 12.dp, vertical = 4.dp)) {

                    Column(modifier = Modifier.padding(vertical = 12.dp)) {
                        BuildItemVisibleContent(it)
                        AnimatedContent(targetState = isExpanded.value) { targetExpanded ->
                            if (targetExpanded) BuildExpandableContent(it)
                        }
                        BuildLedgerActionButton(it, isExpanded)
                    }
                }

            }
        }
    }
}


