package com.a2z.app.ui.screen.home.di_md.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.data.model.report.AgentRequestView
import com.a2z.app.data.model.report.AgentRequestViewResponse
import com.a2z.app.ui.component.EmptyListComponent
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.screen.home.di_md.DistributorHomeViewModel
import com.a2z.app.ui.screen.report.component.BaseReportItem
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.PrimaryColor
import com.google.gson.Gson
import org.json.JSONObject


@Composable
fun ColumnScope.AgentRequestViewComponent(
    approveDialogScope: @Composable ((List<Pair<String, String>>) -> Unit)
) {

    val viewModel: DistributorHomeViewModel = hiltViewModel()
    Card(
        modifier = Modifier
            .weight(1f)
            .padding(
                horizontal = 12.dp,
            )
            .padding(top = 5.dp, bottom = 1.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            BuildTitle()

            ObsComponent(flow = viewModel.reportResultFlow) { data ->

                val mData = Gson().toJson(data)
                val remarksObject = JSONObject(mData).getJSONObject("remarks")
                val keys = remarksObject.keys()

                val remarkList = arrayListOf<Pair<String, String>>()
                keys.forEach {
                    val key = it
                    val value = remarksObject.getString(it)
                    remarkList.add(Pair(key, value))
                }


                val requestViewResponse =
                    Gson().fromJson(mData, AgentRequestViewResponse::class.java)
                requestViewResponse.mRemarks = remarkList

                approveDialogScope.invoke(remarkList)

                if (requestViewResponse.status == 1 && requestViewResponse.report!!.isNotEmpty()) LazyColumn {
                    items(requestViewResponse.report) {
                        BaseReportItem(
                            statusId = it.status_id ?: 4,
                            leftSideDate = it.created_at,
                            leftSideId = it.id,
                            centerHeading1 = it.mobile,
                            centerHeading2 = it.user_name,
                            centerHeading3 = null,
                            rightAmount = it.amount,
                            rightStatus = it.status,
                            isCard = false,
                            actionButton = {
                                viewModel.agentRequestView.value = it
                                viewModel.approveDialogState.value = true
                            },
                            expandListItems = listOf(
                                "Slip" to it.slip,
                                "Ref Id" to it.ref_id,
                                "Bank Name" to it.bank_name,
                                "Deposit Date" to it.deposit_date,
                                "Payment Mode" to it.online_payment_mode,
                                "Branch Code" to it.branch_code,
                                "Mode" to it.mode,
                                "Role" to it.role,
                            ),
                        )
                    }
                }
                else EmptyListComponent()
            }

        }
    }
}

@Composable
private fun BuildTitle() {
    Text(
        text = "Agent Request View",
        style = MaterialTheme.typography.body1.copy(
            fontWeight = FontWeight.Bold,
            color = PrimaryColor,
            fontSize = 14.sp
        ),
        modifier = Modifier
            .background(GreenColor.copy(alpha = 0.2f))
            .padding(8.dp)
    )
}