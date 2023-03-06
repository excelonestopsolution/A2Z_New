package com.a2z.app.ui.screen.report.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.ui.screen.report.ReportUtil
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.util.AppConstant
import com.a2z.app.util.FunCompose
import com.a2z.app.util.VoidCallback
import com.a2z.app.util.extension.prefixRS
import com.usdk.apiservice.aidl.dock.serialport.Parity

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BaseReportItem(
    statusId: Int?,
    leftSideDate: String?,
    leftSideId: String?,
    centerHeading1: String?,
    centerHeading2: String?,
    centerHeading3: String?,
    rightAmount: String?,
    rightCBal: String? = null,
    rightStatus: String?,
    isPrint: Boolean = false,
    isComplaint: Boolean = false,
    isCheckStatus: Boolean = false,
    onPrint: VoidCallback? = null,
    onCheckStatus: VoidCallback? = null,
    onComplaint: VoidCallback? = null,
    actionButton: FunCompose? = null,
    isCard: Boolean = true,
    expandListItems: List<Pair<String, String?>>
) {

    val isExpanded = remember { mutableStateOf(false) }
    Card(modifier = Modifier
        .clickable {
            isExpanded.value = !isExpanded.value
        }
        .padding(horizontal = if (isCard) 12.dp else 8.dp, vertical = 4.dp),
        elevation = if (isCard) 2.dp else 0.dp, shape = MaterialTheme.shapes.small) {

        var colModifier = Modifier.padding(vertical = 5.dp)
        if (!isCard) colModifier =
            Modifier
                .border(1.dp, PrimaryColor, MaterialTheme.shapes.small)
                .padding(vertical = 5.dp)

        Column(modifier = colModifier) {
            BuildItemVisibleContent(
                statusId = statusId,
                leftSideDate = leftSideDate,
                leftSideId = leftSideId,
                centerHeading1 = centerHeading1,
                centerHeading2 = centerHeading2,
                centerHeading3 = centerHeading3,
                rightAmount = rightAmount,
                rightStatus = rightStatus,
                rightCBal = rightCBal,
                actionButton = actionButton
            )
            AnimatedContent(targetState = isExpanded.value) { targetExpanded ->
                if (targetExpanded) BuildExpandableContent(expandListItems)
            }
            BuildLedgerActionButton(
                isPrint, isCheckStatus, isComplaint, isExpanded,
                onPrint, onComplaint, onCheckStatus
            )
        }
    }


}

@Composable
fun BuildLedgerActionButton(
    isPrint: Boolean,
    isCheckStatus: Boolean,
    isComplain: Boolean,
    isExpanded: MutableState<Boolean>,
    onPrint: VoidCallback? = null,
    onComplain: VoidCallback? = null,
    onCheckStatus: VoidCallback? = null,
) {

    Box(modifier = Modifier.fillMaxWidth()) {
        if (isPrint && !isExpanded.value) {
            IconButton(
                onClick = {
                    onPrint?.invoke()
                },
                modifier = Modifier.align(Alignment.Center),
            ) {
                Icon(
                    imageVector = Icons.Default.Print,
                    contentDescription = "Print",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        if (isCheckStatus && isExpanded.value) {
            val align: Alignment =
                if (isComplain) Alignment.CenterStart else Alignment.Center
            OutlinedButton(onClick = {
                onCheckStatus?.invoke()
            }, modifier = Modifier.align(align)) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Check Status")
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Check Status")
            }

        }


        if (isComplain && isExpanded.value) {
            val align = if (isCheckStatus) Alignment.CenterEnd else Alignment.Center
            OutlinedButton(onClick = {
                onComplain?.invoke()
            }, modifier = Modifier.align(align)) {
                Icon(imageVector = Icons.Default.Message, contentDescription = "Make Complain")
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Make Complain")
            }
        }

    }


}

@Composable
private fun BuildItemVisibleContent(
    statusId: Int?,
    leftSideDate: String?,
    leftSideId: String?,
    centerHeading1: String?,
    centerHeading2: String?,
    centerHeading3: String?,
    rightAmount: String?,
    rightStatus: String?,
    rightCBal: String?,
    actionButton: FunCompose?,
) {
    Row(Modifier.padding(8.dp)) {
        Column(Modifier.weight(1f)) {
            if (leftSideDate.orEmpty().isNotEmpty()) Text(
                text = leftSideDate.toString(),
                style = MaterialTheme.typography.body2.copy(color = Color.Gray,
                fontSize = 12.sp)
            )
            if (leftSideId.orEmpty().isNotEmpty()) Text(
                text = leftSideId.toString(),
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp
            )
        }
        Column(
            Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            if (centerHeading1.orEmpty().isNotEmpty()) Text(
                text = centerHeading1.toString(),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center
            )
            if (centerHeading2.orEmpty()
                    .isNotEmpty()
            ) Divider(modifier = Modifier.padding(vertical = 5.dp))
            if (centerHeading2.orEmpty().isNotEmpty()) Text(
                text = centerHeading2.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                color = Color.Gray,
                fontSize = 14.sp
            )

            if (centerHeading3.orEmpty().isNotBlank()) Divider(
                modifier = Modifier.padding(vertical = 5.dp)
            )
            if (centerHeading3.orEmpty().isNotEmpty()) Text(
                text = centerHeading3.toString(),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        Column(Modifier.weight(1.2f), horizontalAlignment = Alignment.End) {
            if (rightStatus.orEmpty().isNotEmpty()) Text(
                text = rightStatus.toString(),
                color = ReportUtil.getColorFromId(statusId),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold

            )
            if (rightAmount.orEmpty().isNotEmpty()) Text(
                text = AppConstant.RUPEE_SYMBOL + rightAmount,
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp

                    )
            )
            if (rightCBal.orEmpty().isNotEmpty()) Text(
                text =rightCBal.toString().prefixRS(),
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Gray

                )
            )

            actionButton?.invoke()
        }

    }
}


@Composable
private fun BuildExpandableContent(reports: List<Pair<String, String?>>) {
    Column(
        Modifier
            .padding(12.dp)
            .background(
                shape = MaterialTheme.shapes.medium, color =
                Color.LightGray.copy(alpha = 0.2f)
            )
            .padding(12.dp)
    ) {

        reports.forEach {
            if (it.first.orEmpty().isNotEmpty() &&
                it.second.orEmpty().isNotEmpty()
            ) ReportLFComponent(
                titleLeft = it.first.toString(),
                valueRight = it.second,

            )
        }

    }


}