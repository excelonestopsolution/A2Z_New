package com.a2z.app.ui.screen.report.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.ui.screen.report.ReportUtil
import com.a2z.app.util.AppConstant


@Composable
fun BuildExpandableContent(report: LedgerReport) {

    Column(
        Modifier
            .padding(12.dp)
            .background(shape = MaterialTheme.shapes.medium, color = MaterialTheme.colors.primary)
            .padding(12.dp)
    ) {

        ReportLFComponent(titleLeft = "Mobile Number", valueRight = report.senderNumber)
        ReportLFComponent(titleLeft = "Bank Name", valueRight = report.bankName)
        ReportLFComponent(titleLeft = "IFSC Code", valueRight = report.ifsc)
        ReportLFComponent(titleLeft = "Beneficiary Name", valueRight = report.beneName)
        ReportLFComponent(titleLeft = "Reference Id", valueRight = report.operatorId)
        ReportLFComponent(titleLeft = "Transaction Type", valueRight = report.txnType)
        ReportLFComponent(
            titleLeft = "Opening Balance",
            valueRight = AppConstant.RUPEE_SYMBOL + report.opBalance
        )
        ReportLFComponent(
            titleLeft = "Txn Amount",
            valueRight = AppConstant.RUPEE_SYMBOL + report.amount
        )
        ReportLFComponent(
            titleLeft = "Credit Amount",
            valueRight = AppConstant.RUPEE_SYMBOL + report.credit
        )
        ReportLFComponent(
            titleLeft = "Debit Amount",
            valueRight = AppConstant.RUPEE_SYMBOL + report.debit
        )
        ReportLFComponent(
            titleLeft = "TDS Amount",
            valueRight = AppConstant.RUPEE_SYMBOL + report.tds
        )
        ReportLFComponent(
            titleLeft = "GST Amount",
            valueRight = AppConstant.RUPEE_SYMBOL + report.gst
        )
        ReportLFComponent(
            titleLeft = "Closing Balance",
            valueRight = AppConstant.RUPEE_SYMBOL + report.clBalance
        )
        ReportLFComponent(titleLeft = "Provider", valueRight = report.providerName)
        ReportLFComponent(titleLeft = "Remark", valueRight = report.remark)

    }


}

@Composable
fun BuildItemVisibleContent(report: LedgerReport) {
    Row(Modifier.padding(8.dp)) {
        Column(Modifier.weight(1f)) {
            Text(
                text = report.txnTime.toString(),
                style = MaterialTheme.typography.body2
            )
            Text(
                text = report.id.toString(),
                color = MaterialTheme.colors.primary
            )
        }
        Column(
            Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = report.number.toString(),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
            Divider(modifier = Modifier.padding(vertical = 5.dp))
            Text(
                text = report.serviceName.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2
            )

            val senderName = report.beneName ?: report.billerName ?: ""
            if (senderName.isNotBlank()) Divider(
                modifier = Modifier.padding(vertical = 5.dp)
            )
            if (senderName.isNotBlank()) Text(
                text = report.senderNumber.toString(),
                textAlign = TextAlign.Center
            )
        }
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.End) {
            Text(
                text = report.statusDesc.toString(),
                color = ReportUtil.getColorFromId(report.statusId)

            )
            Text(
                text = AppConstant.RUPEE_SYMBOL + report.amount.toString(),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold,

                    )
            )
        }

    }
}

@Composable
fun BuildLedgerActionButton(report: LedgerReport, isExpanded: MutableState<Boolean>) {

    Box(modifier = Modifier.fillMaxWidth()) {
        if (report.isPrint && !isExpanded.value) {
            IconButton(
                onClick = { /*TODO*/ }, modifier = Modifier.align(Center),
            ) {
                Icon(
                    imageVector = Icons.Default.Print,
                    contentDescription = "Print",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        if (report.isCheckStatus && isExpanded.value) {
            val align: Alignment = if (report.isComplain) CenterStart else Alignment.Center
            OutlinedButton(onClick = { }, modifier = Modifier.align(align)) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Check Status")
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Check Status")
            }

        }


        if (report.isComplain && isExpanded.value) {
            val align = if (report.isCheckStatus) Alignment.CenterEnd else Alignment.Center
            OutlinedButton(onClick = { }, modifier = Modifier.align(align)) {
                Icon(imageVector = Icons.Default.Message, contentDescription = "Make Complain")
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Make Complain")
            }
        }

    }


}