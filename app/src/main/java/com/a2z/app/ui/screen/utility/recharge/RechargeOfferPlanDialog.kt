package com.a2z.app.ui.screen.utility.recharge

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.data.model.utility.RechargePlan
import com.a2z.app.data.model.utility.RechargePlanResponse
import com.a2z.app.data.model.utility.RechargePlanTab
import com.a2z.app.ui.component.EmptyListComponent
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.PrimaryColorLight
import com.a2z.app.util.extension.prefixRS
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RechargeOfferPlanDialog(viewModel: RechargeViewModel = hiltViewModel()) {
    if (!viewModel.rechargePlanDialogState.value) return

    Dialog(
        onDismissRequest = {
            viewModel.rechargePlanDialogState.value = false
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ObsComponent(flow = viewModel.rechargePlanResponseFlow) {

                val response = JSONObject(Gson().toJson(it))
                val status = response.getInt("status")
                val message = response.optString("message")

                if (status == 1) {
                    val tabs = arrayListOf<RechargePlanTab>()
                    val records = response.getJSONObject("records")
                    val keys = records.keys()
                    keys.forEach { key ->
                        val rechargePlans = arrayListOf<RechargePlan>()
                        val mArray = records.getJSONArray(key)
                        for (i in 0 until mArray.length()) {
                            rechargePlans.add(
                                RechargePlan(
                                    rs = mArray.getJSONObject(i).optString("rs"),
                                    desc = mArray.getJSONObject(i).optString("desc"),
                                    validity = mArray.getJSONObject(i).optString("validity"),
                                    remark = mArray.getJSONObject(i).optString("remark"),
                                    discontinued = mArray.getJSONObject(i)
                                        .optString("discontinued"),
                                )
                            )

                        }
                        viewModel.offerList = viewModel.offerList?.plus(rechargePlans)
                        tabs.add(RechargePlanTab(key, rechargePlans))

                    }

                    val res = RechargePlanResponse(status, message, tabs)

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        AppBar(viewModel)
                        BuildPagerList(res.offerTab) { offer ->
                            viewModel.input.amountInputWrapper.input.value = offer.rs.toString()
                            viewModel.rechargePlanState.value = offer
                            viewModel.rechargePlanDialogState.value = false
                            viewModel.input.validate()
                        }

                    }
                } else EmptyListComponent()

            }
        }
    }


}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun BuildPagerList(offerTab: List<RechargePlanTab>, onClick: (RechargePlan) -> Boolean) {
    val pagerState = com.google.accompanist.pager.rememberPagerState(offerTab.size)

    Column(Modifier.fillMaxSize()) {
        Tabs(pagerState = pagerState, offerTab)
        TabsContent(pagerState = pagerState, offerTab, onClick={onClick.invoke(it)})

    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState, offerTab: List<RechargePlanTab>) {

    val scope = rememberCoroutineScope()
    ScrollableTabRow(

        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = PrimaryColorLight
            )
        }
    ) {

        offerTab.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        offerTab[index].planName,
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

@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, offerTab: List<RechargePlanTab>,onClick : (RechargePlan)->Unit) {

    com.google.accompanist.pager.HorizontalPager(state = pagerState) { page ->

        val lists = offerTab[page].rechargePlanList
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(lists) {
                Card(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .fillMaxWidth().clickable { onClick.invoke(it) },
                    shape = MaterialTheme.shapes.small,
                    elevation = 8.dp

                ) {

                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)) {
                        Text(
                            text = it.rs.toString().prefixRS(),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .padding(5.dp)
                                .border(1.dp, PrimaryColorLight, CircularShape)
                                .padding(5.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = it.desc.toString(),
                        color = Color.DarkGray, fontSize = 14.sp, lineHeight = 22.sp)
                    }
                }
            }
        }

    }
}


@Composable
private fun AppBar(viewModel: RechargeViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        IconButton(onClick = {
            viewModel.rechargePlanDialogState.value = false
        }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
        Text(
            text = "Recharge Plan", style = MaterialTheme.typography.h6.copy(
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            )
        )
    }
}


