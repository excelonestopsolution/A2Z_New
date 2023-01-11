package com.a2z.app.ui.screen.settlement.bank_list

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.a2z.app.data.model.settlement.SettlementAddedBank
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.util.extension.singleResult
import com.a2z.app.util.VoidCallback
import com.a2z.app.util.extension.notAvailable


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettlementBankScreen(navBackStackEntry: NavBackStackEntry) {
    val viewModel: SettlementBankViewModel = hiltViewModel()

    LaunchedEffect(key1 = Unit){
        val bankAdded = navBackStackEntry.singleResult<Boolean>("bank_added")
        if (bankAdded == true) {
            viewModel.fetchBankList()
        }
    }


    Scaffold(
        topBar = {
            NavTopBar(title = "Settlement Bank List")
        }
    ) {
        BaseContent(viewModel) {
            ObsComponent(flow = viewModel.bankListResultFlow) {
                LazyColumn {
                    item {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)

                                        .background(GreenColor, CircleShape)
                                        .padding(8.dp)
                                        .clickable {viewModel.onAddNewBank() }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Add New Settlement Bank",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Divider(modifier = Modifier.padding(vertical = 12.dp))
                        }
                    }
                    items(it.data!!) {
                        BuildListItem(it,viewModel)
                    }
                }
            }
        }

    }
}

@Composable
private fun BuildListItem(it: SettlementAddedBank,viewModel: SettlementBankViewModel) {

    val (statusText, statusColor) = when (it.statusId) {
        1 -> Pair("Active", GreenColor)
        3 -> Pair("Pending for\napproval", GreenColor)
        else -> Pair("Rejected", RedColor)
    }

    val (uploadButton, screening, sendButton) = when (it.statusId) {
        1 -> Triple(false, false, true)
        3 -> Triple(it.documentStatus == 0, it.documentStatus != 0, false)
        else -> Triple(false, false, false)
    }


    Column {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                BuildBankIcon()

                BuildBankInfo(it)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    if (sendButton) BuildSendIcon{
                        viewModel.onSend(it)
                    }

                    if (uploadButton) BuildUploadDocument(
                        icon = Icons.Default.FileUpload,
                        text = "Upload\nDocument",
                        onUploadClick = {
                            viewModel.onUploadDocument(it)
                        }
                    )
                    if (screening) BuildUploadDocument(
                        icon = Icons.Default.DocumentScanner,
                        text = "Upload\nScreening",
                    )
                    Text(
                        text = statusText,
                        color = statusColor,
                    )
                }
            }
            BuildRemarkText(it.remark)
        }
        Divider(Modifier.padding(vertical = 8.dp))
    }

}

@Composable
private fun BuildSendIcon(callback : VoidCallback) {
    Icon(
        imageVector = Icons.Default.Send,
        contentDescription = null,
        modifier = Modifier
            .clip(shape = CircleShape)
            .border(
                width = 1.dp,
                color = Color.DarkGray,
                shape = CircleShape
            )
            .rotate(-15f)
            .clickable {
                callback.invoke()
            }
            .padding(8.dp)
    )
}

@Composable
private fun BuildRemarkText(
    remark: String?
) {
    if (remark != null && remark.trim().isNotEmpty())
        Text(
            text = remark.toString(), fontSize = 12.sp, fontWeight = FontWeight.W500,
            color = PrimaryColorDark.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = 12.dp)
        )
}


@Composable
fun RowScope.BuildBankInfo(settlementAddedBank: SettlementAddedBank) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(5.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = settlementAddedBank.name.toString().notAvailable(),
            fontWeight = FontWeight.SemiBold, color = PrimaryColorDark
        )
        Text(
            text = settlementAddedBank.bankName.toString(),
            fontWeight = FontWeight.SemiBold, fontSize = 12.sp,
            color = PrimaryColorDark.copy(alpha = 0.7f)
        )
        Text(
            text = "A/C No. " + settlementAddedBank.accountNumber.toString(),
            fontSize = 12.sp, fontWeight = FontWeight.W500
        )

    }
}


@Composable
fun BuildUploadDocument(
    icon: ImageVector,
    text: String,
    onUploadClick: VoidCallback? = null
) {

    val color = if (onUploadClick != null) PrimaryColorDark else GreenColor

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 5.dp)
            .clip(CircleShape)
            .border(
                color = color,
                width = 1.dp,
                shape = CircleShape
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                onUploadClick?.invoke()
            }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text, fontSize = 12.sp, color = color,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W500,
        )
    }


}

@Composable
private fun BuildBankIcon() {
    Icon(
        imageVector = Icons.Default.AccountBalance,
        contentDescription = null,
        tint = PrimaryColorDark,
        modifier = Modifier
            .clip(CircleShape)
            .size(52.dp)
            .padding(4.dp)
            .border(width = 1.dp, color = PrimaryColor, shape = CircleShape)
            .padding(8.dp)
    )
}