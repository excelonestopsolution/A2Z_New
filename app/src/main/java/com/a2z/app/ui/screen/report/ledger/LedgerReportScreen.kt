package com.a2z.app.ui.screen.report.ledger

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.ui.component.AppLazyList
import com.a2z.app.ui.component.AppProgress
import com.a2z.app.ui.component.AppTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.screen.report.component.BuildExpandableContent
import com.a2z.app.ui.screen.report.component.BuildItemVisibleContent
import com.a2z.app.ui.screen.report.component.BuildLedgerActionButton
import com.a2z.app.ui.screen.report.component.ReportErrorRetryComponent
import com.a2z.app.ui.theme.BackgroundColor
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LedgerReportScreen() {

    val viewModel: LedgerReportViewModel = hiltViewModel()

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { AppTopBar(title = "Ledger Report") }
    ) { _ ->



        val pagingData =viewModel.fetchLedgerReport.collectAsLazyPagingItems()
        AppLazyList<LedgerReport>(pagingData){
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


