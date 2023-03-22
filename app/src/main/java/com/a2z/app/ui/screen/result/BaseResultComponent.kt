package com.a2z.app.ui.screen.result

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.data.model.dmt.DmtTransactionDetail
import com.a2z.app.data.model.dmt.MiniStatement
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.common.AppNetworkImage
import com.a2z.app.ui.component.BackPressHandler
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.CollectLatestWithScope
import com.a2z.app.ui.screen.AppViewModel
import com.a2z.app.ui.theme.*
import com.a2z.app.ui.util.pdf.ReceiptDownloadUtil
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.AppConstant
import com.a2z.app.util.VoidCallback
import com.a2z.app.util.storage.StorageHelper

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BaseResultComponent(
    statusId: Int,
    message: String,
    status: String,
    dateTime: String,
    amountTopText: String,
    amountBelowText: String,
    amount: String,
    availableBalance: String? = null,
    isPaymentAmount: Boolean = true,
    serviceIconRes: Int? = null,
    serviceIconNet: String? = null,
    iconSize: Int = 60,
    dmtInfo: ArrayList<DmtTransactionDetail>? = null,
    vararg titleValues: List<Pair<String, String>>,
    statement: ArrayList<MiniStatement>? = null,
    commissionAmount: Boolean = false,
    backPressHandle: Boolean = true
) {

    val context = LocalContext.current
    val navController = LocalNavController.current
    var scrollView: ScrollView? = null

    val viewModel: TxnResultViewModel = hiltViewModel()


    Scaffold(
        backgroundColor = BackgroundColor
    ) { _ ->

        BaseContent(viewModel) {


            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {


                AndroidView(factory = {
                    val view = LayoutInflater.from(context)
                        .inflate(R.layout.view_receipt, null, false)
                    view
                }, update = {
                    scrollView = it.findViewById(R.id.scroll_view)

                    val mainView = ComposeView(context = context).apply {
                        this.setContent {
                            BuildContent(
                                statusId = statusId,
                                message = message,
                                status = status,
                                dateTime = dateTime,
                                serviceName = amountTopText,
                                providerName = amountBelowText,
                                amount = amount,
                                availableBalance = availableBalance,
                                dmtInfo = dmtInfo,
                                isPaymentAmount = isPaymentAmount,
                                serviceIconRes = serviceIconRes,
                                serviceIconNet = serviceIconNet,
                                titleValues = titleValues,
                                iconSize = iconSize,
                                statement = statement
                            )
                        }
                    }
                    val spaceViewBottom = TextView(context).apply {
                        layoutParams =
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                300
                            )
                    }
                    val spaceViewTop = TextView(context).apply {
                        layoutParams =
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                150
                            )
                    }

                    val linearLayout = LinearLayout(context).apply {

                        //layout params
                        this.orientation = LinearLayout.VERTICAL
                        this.layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        //add views
                        this.addView(spaceViewTop)
                        this.addView(mainView)
                        this.addView(spaceViewBottom)
                    }

                    scrollView?.let { sv ->
                        if (sv.childCount == 0) sv.addView(linearLayout)
                    }


                }, modifier = Modifier.padding(horizontal = 12.dp))


                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(0.1f))
                        .padding(start = 12.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                ) {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.cancel),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            navController.navigate(NavScreen.DashboardScreen.route) {
                                popUpTo(NavScreen.DashboardScreen.route) {
                                    inclusive = true
                                }
                            }

                        },
                        shape = CircularShape,
                        modifier = Modifier.height(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home, contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "Home")
                    }
                }
                BuildShareButtons(
                    onShare = { saveAndShare(scrollView!!) },
                    onWhatsapp = { viewModel.whatsAppDialog.value = true },
                    onDownload = {
                        viewModel.downloadReceiptData()
                    })

            }


            CollectLatestWithScope(flow = viewModel.resultFlow, callback = {
                when (it) {
                    is ResultType.Failure -> viewModel.failureDialog("Opps something went wrong")
                    is ResultType.Loading -> viewModel.progressDialog("Downloading...")
                    is ResultType.Success -> {
                        viewModel.response = it.data
                        viewModel.dismissDialog()
                        if (!commissionAmount)
                            ReceiptDownloadUtil.download(
                                context = context,
                                data = it.data,
                                txnResultPrintReceiptType = viewModel.receiptType,
                                commission = null
                            )
                        else viewModel.commissionAmountDialog.value = true
                    }
                }
            })


            DMTCommissionDialog(state = viewModel.commissionAmountDialog, onProceed = {
                ReceiptDownloadUtil.download(
                    context = context,
                    data = viewModel.response,
                    txnResultPrintReceiptType = viewModel.receiptType,
                    commission = it
                )
            })

            WhatsAppSendDialog(state = viewModel.whatsAppDialog,
                withContact = {
                    saveAndShare(scrollView!!, true)
                }, withNumber = {
                    saveAndShare(scrollView!!, true, whatsappNumber = it)
                })
        }
    }

}


private fun getStatusValue(statusId: Int) = when (statusId) {
    1, 24 -> Pair(GreenColor, R.drawable.icon_sucess)
    2 -> Pair(RedColor, R.drawable.icon_failed)
    3 -> Pair(YellowColor, R.drawable.icon_pending)
    else -> Pair(PrimaryColorDark, R.drawable.icon_pending)
}

@Composable
private fun BuildContent(
    statusId: Int,
    message: String,
    status: String,
    dateTime: String,
    serviceName: String,
    providerName: String,
    amount: String,
    availableBalance: String?,
    serviceIconRes: Int?,
    serviceIconNet: String?,
    isPaymentAmount: Boolean = true,
    iconSize: Int,
    vararg titleValues: List<Pair<String, String>>,
    dmtInfo: ArrayList<DmtTransactionDetail>? = null,
    statement: ArrayList<MiniStatement>? = null,

    ) {

    val (color, statusIconRes) = getStatusValue(statusId)

    @Composable
    fun BuildHeaderSection() {
        Image(
            painter = painterResource(id = statusIconRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            status, style = TextStyle(
                color = color,
                fontWeight = FontWeight.Bold, fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        )
        Text(
            message, textAlign = TextAlign.Center, fontSize = 12.sp,
            color = Color.Gray,
        )
        Text(
            dateTime, style = TextStyle(
                fontSize = 12.sp, color = Color.Gray
            )
        )
    }

    @Composable
    fun BuildTitleValue(title: String, value: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
        ) {
            Text(
                text = title, style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.weight(0.7f)
            )
            Text(
                text = "  :  ",
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = value, style = MaterialTheme.typography.subtitle1.copy(
                    color = Color.Black.copy(alpha = 0.7f),
                ), modifier = Modifier.weight(1f)
            )
        }
    }

    @Composable
    fun BuildAmountAndProviderInfo(iconSize: Int) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                Text(
                    serviceName,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                )


                Text(
                    "₹ $amount",
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    providerName,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )


            }

            if (serviceIconNet != null && serviceIconRes != null)
                throw Exception("Service Icon Local and Network both are not null")

            if (serviceIconNet != null)
                AppNetworkImage(url = serviceIconNet)

            if (serviceIconRes != null)
                Image(
                    painter = painterResource(id = serviceIconRes),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(PrimaryColor),
                    modifier = Modifier.size(iconSize.dp),
                    contentScale = ContentScale.FillBounds
                )
        }
    }

    @Composable
    fun BuildPaymentAmount(amount: String) {

        Row {
            Text(
                "Payment Amount", style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "₹ $amount",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )
        }

        Row {
            Text(
                "-Wallet", style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Normal
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "₹ $amount",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal)
            )
        }
    }


    @Composable
    fun BuildAvailableAmount(amount: String) {

        Row {
            Text(
                "Available Balance", style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "₹ $amount",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )
        }
    }

    @Composable
    fun BuildMiniStatement(statement: ArrayList<MiniStatement>) {

        @Composable
        fun RowScope.BuildStatementHeaderTitle(
            value: String,
            weight: Float = 1f,
            textAlign: TextAlign = TextAlign.Center,
            color: Color = Color.Black

        ) {
            Text(
                text = value,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(weight),
                textAlign = textAlign,
                fontSize = 14.sp,
                color = color
            )
        }


        Row {
            BuildStatementHeaderTitle(
                value = "Txn Time",
                weight = 0.7f, textAlign = TextAlign.Start,
            )
            BuildStatementHeaderTitle(value = "Narration", textAlign = TextAlign.Start)
            BuildStatementHeaderTitle(
                value = "Amount",
                weight = 0.7f, textAlign = TextAlign.End
            )
        }

        statement.forEach {
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            Row {
                BuildStatementHeaderTitle(
                    value = it.txnTime.toString(),
                    weight = 0.7f, textAlign = TextAlign.Start,
                    color = PrimaryColor
                )
                BuildStatementHeaderTitle(
                    value = it.narration.toString(),
                    textAlign = TextAlign.Start,
                    color = PrimaryColor
                )
                BuildStatementHeaderTitle(
                    value = AppConstant.RUPEE_SYMBOL + it.amount + " " + it.txnType,
                    weight = 0.7f, textAlign = TextAlign.End,
                    color = if (it.txnType.toLowerCase() == "cr") GreenColor else RedColor
                )
            }
        }
    }



    Box(
        modifier = Modifier.padding(
            horizontal = 5.dp,
            vertical = 3.dp
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_txn),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            val viewModel: AppViewModel = hiltViewModel()
            BuildHeaderSection()
            Divider(Modifier.padding(vertical = 16.dp))

            BuildAmountAndProviderInfo(iconSize)

            Divider(Modifier.padding(vertical = 16.dp))

            titleValues.forEach {

                it.forEach {
                    if (it.second.isNotEmpty() && it.second != "null" && it.second.toLowerCase()
                        != "na" && it.second.toLowerCase() != "n/a"
                    )
                        BuildTitleValue(
                            title = it.first,
                            value = it.second
                        )
                }

                Divider(Modifier.padding(vertical = 16.dp))
            }



            if (dmtInfo != null) BuildDmtTransactionDetail(dmtInfo)

            if (isPaymentAmount) BuildPaymentAmount(amount)
            if (availableBalance != null) BuildAvailableAmount(availableBalance)

            Divider(Modifier.padding(vertical = 16.dp))
            if (statement != null && statement.isNotEmpty()) BuildMiniStatement(statement!!)
            if (statement != null && statement.isNotEmpty()) Divider(Modifier.padding(vertical = 16.dp))

            BuildTitleValue(
                title = "Shop Name",
                value = viewModel.appPreference.user?.shopName.toString()
            )
            BuildTitleValue(
                title = "contact",
                value = viewModel.appPreference.user?.mobile.toString()
            )

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Power by",
                style = TextStyle(fontStyle = FontStyle.Italic, color = Color.Black)
            )
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
                Modifier
                    .wrapContentHeight()
                    .width(100.dp)
            )
        }
    }


}

@Composable
private fun BuildDmtTransactionDetail(dmtInfo: java.util.ArrayList<DmtTransactionDetail>) {
    Column {


        Row {
            Text(
                text = "Txn Id", modifier = Modifier.weight(0.8f),
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Amount",
                modifier = Modifier.weight(0.8f),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Bank Ref",
                modifier = Modifier.weight(1.2f),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Status",
                modifier = Modifier.weight(0.8f),
                textAlign = TextAlign.End,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        dmtInfo.forEach {
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Text(
                    text = it.txnId.toString(), modifier = Modifier.weight(0.8f),
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = AppConstant.RUPEE_SYMBOL + it.amount.toString(),
                    modifier = Modifier.weight(0.8f),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = it.bankRef.toString(),
                    modifier = Modifier.weight(1.2f),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = it.statusDesc.toString(),
                    modifier = Modifier.weight(0.8f),
                    textAlign = TextAlign.End,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colors.primary
                )
            }

        }

        Divider(Modifier.padding(vertical = 16.dp))
    }

}


@Composable
private fun BoxScope.BuildShareButtons(
    onShare: VoidCallback,
    onWhatsapp: VoidCallback,
    onDownload: VoidCallback
) {

    @Composable
    fun ButtonComponent(
        callback: VoidCallback,
        icon: ImageVector, text: String,
        color: Color = PrimaryColor
    ) {
        Button(
            onClick = callback, shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = color,
                contentColor = Color.White
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon, contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text)
            }
        }
    }

    @Composable
    fun DownloadButton() {
        OutlinedButton(onClick = onDownload, shape = CircleShape) {
            Row {
                Icon(imageVector = Icons.Default.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Download Receipt")
            }
        }
    }

    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        DownloadButton()
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            ButtonComponent(
                callback = onShare,
                icon = Icons.Default.Share,
                text = "Share"
            )
            Spacer(modifier = Modifier.weight(1f))
            ButtonComponent(
                callback = onWhatsapp,
                icon = Icons.Default.Whatsapp,
                text = "Whatsapp",
                color = GreenColor
            )
        }

    }


}

private fun saveAndShare(
    scrollView: ScrollView,
    whatsapp: Boolean = false,
    whatsappNumber: String? = null
) {
    scrollView.setBackgroundColor(ContextCompat.getColor(scrollView.context, R.color.black))
    val bitmap: Bitmap? = StorageHelper.getBitmapFromView(scrollView = scrollView)
    scrollView.setBackgroundColor(
        ContextCompat.getColor(
            scrollView.context,
            R.color.background_color
        )
    )
    val imageFileUri: Uri = StorageHelper.saveImageToCacheDirectory(
        context = scrollView.context,
        bitmap = bitmap,
        fileName = "transaction_receipt.jpg",

        )
    StorageHelper.shareImage(imageFileUri, scrollView.context, whatsapp, whatsappNumber)

}


