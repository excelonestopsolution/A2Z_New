package com.a2z.app.ui.screen.util.device_order

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.screen.util.device_order.matm.MATMOrderTabContent
import com.a2z.app.ui.screen.util.device_order.mpos.MPOSOrderTabContent
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.GreenColor
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DeviceOrderTabScreen() {

    val pagerState = rememberPagerState(pageCount = 2)

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                NavTopBar(title = "Order M-ATM Device")
                Tabs(pagerState = pagerState)
            }


        }
    ) {
        TabsContent(pagerState = pagerState)
    }
}


@OptIn(ExperimentalPagerApi::class)
@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf("M-ATM", "M-POS")
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = Color.White
            )
        }
    ) {

        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        list[index],
                        color = if (pagerState.currentPage == index) Color.White else Color.LightGray
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}



@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0-> MATMOrderTabContent()
            1 -> MPOSOrderTabContent()
        }
    }
}