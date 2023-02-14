package com.a2z.app.ui.screen.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.data.local.AppPreference
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.common.AppNetworkImage
import com.a2z.app.ui.screen.members.list.MemberListType
import com.a2z.app.ui.theme.*
import com.a2z.app.ui.util.UserRole
import com.a2z.app.util.VoidCallback

@Composable
fun ColumnScope.DashboardDrawerWidget(viewModel: DashboardViewModel) {

    val navController = LocalNavController.current
    val appPreference = viewModel.appPreference
    val role = LocalUserRole.current

    Box(
        modifier = Modifier
            .weight(1f)
            .background(color = PrimaryColorDark.copy(0.1f))
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            HeaderContent(appPreference)

            BuildExpandMenu(
                menuHeading = "Pin & Password",
                vector = Icons.Default.Password,
                menuList = listOf(
                    Pair("Change Pin") {
                        navController.navigate(NavScreen.ChangePinScreen.route)
                    },
                    Pair("Change Password") {
                        navController.navigate(NavScreen.ChangePasswordScreen.route)
                    },
                )
            )

         if(role == UserRole.RETAILER)   BuildExpandMenu(
                menuHeading = "User Kyc",
                drawable = com.a2z.app.R.drawable.kyc_icon,
                menuList = listOf(
                    Pair("Document Kyc") {
                        navController.navigate(NavScreen.DocumentKycScreen.route)
                    },
                    Pair("Aadhaar Kyc") {
                        navController.navigate(NavScreen.AadhaarKycScreen.route)
                    },
                    Pair("AEPS Kyc") {
                        navController.navigate(NavScreen.AEPSKycScreen.route)
                    },
                )
            )

         if(role in UserRole.DISTRIBUTOR..UserRole.MD)   BuildExpandMenu(
                menuHeading = "Members",
                drawable = com.a2z.app.R.drawable.user,
                menuList = listOf(
                    Pair("Retailers") {
                        navController.navigate(
                            NavScreen.MemberListScreen.passArgs(
                                isTransfer = false,
                                memberType = MemberListType.RETAILER
                            )
                        )
                    },
                    if (LocalUserRole.current == UserRole.MD) Pair("Distributors") {
                        navController.navigate(
                            NavScreen.MemberListScreen.passArgs(
                                isTransfer = false,
                                memberType = MemberListType.DISTRIBUTOR
                            )
                        )
                    } else Pair("") {},

                    )
            )
          if(role == UserRole.RETAILER)  BuildExpandMenu(
                menuHeading = "Fund Request",
                drawable = com.a2z.app.R.drawable.money_bag,
                menuList = listOf(
                    Pair("New Parent Request") {
                        navController.navigate(NavScreen.FundRequestScreen.route)
                    },
                    Pair("Return Parent Payment") {
                        navController.navigate(NavScreen.ParentPaymentReturnScreen.route)
                    },
                )
            )



         if(role == UserRole.RETAILER)   BuildExpandMenu(
                menuHeading = "All Reports",
                drawable = com.a2z.app.R.drawable.icon_report,
                menuList = listOf(
                    Pair("M-ATM / M-POS") {
                        navController.navigate(NavScreen.MATMRequestReportScreen.route)
                    },
                    Pair("AEPS Report") {
                        navController.navigate(NavScreen.AEPSReportScreen.route)
                    },

                    Pair("Fund Report") {
                        navController.navigate(NavScreen.FundReportScreen.route)
                    },
                    Pair("DT Report") {
                        navController.navigate(NavScreen.DTReportScreen.route)
                    },
                    Pair("PG Report") {
                        navController.navigate(NavScreen.PGReportScreen.route)
                    },
                    Pair("Complaints") {
                        navController.navigate(NavScreen.ComplaintScreen.route)
                    },
                )
            )
            if(role == UserRole.DISTRIBUTOR || role ==UserRole.MD)   BuildExpandMenu(
                menuHeading = "All Reports",
                drawable = com.a2z.app.R.drawable.icon_report,
                menuList = listOf(
                    Pair("Ledger Report") {
                        navController.navigate(NavScreen.NetworkLedgerReport.route)
                    },
                    Pair("Recharge Report") {
                        navController.navigate(NavScreen.NetworkRechargeReport.route)
                    },
                    Pair("Account Statement") {
                        navController.navigate(NavScreen.AccountStatementReport.route)
                    },
                )
            )

           if(role in UserRole.DISTRIBUTOR..UserRole.MD) BuildExpandMenu(
                menuHeading = "Payments",
                drawable = com.a2z.app.R.drawable.icon_payment,
                menuList = listOf(
                    Pair("Fund Transfer Retailer") {
                        navController.navigate(
                            NavScreen.MemberListScreen.passArgs(
                                memberType = MemberListType.RETAILER,
                                isTransfer = true
                            )
                        )
                    },
                   if(role == UserRole.MD) Pair("Fund Transfer Distributor") {
                        navController.navigate(
                            NavScreen.MemberListScreen.passArgs(
                                memberType = MemberListType.DISTRIBUTOR,
                                isTransfer = true
                            )
                        )
                    } else Pair(""){},
                    Pair("Fund Transfer Report") {
                        navController.navigate(NavScreen.FundTransferReportScreen.route)
                    },
                    Pair("Payment Report") {
                        navController.navigate(NavScreen.PaymentReportScreen.route)
                    }

                )
            )
         if(role == UserRole.RETAILER)   BuildSingleItemMenu(
                text = "AEPS Settlement",
                drawable = com.a2z.app.R.drawable.settlement,
                callback = {
                    navController.navigate(NavScreen.SettlementBankScreen.route)
                }
            )


            BuildExpandMenu(
                menuHeading = "More...",
                vector = Icons.Default.ReadMore,
                menuList = listOf(
                    Pair("User Agreement") {
                        navController.navigate(NavScreen.UserAgreementScreen.route)
                    },

                    Pair("Commission Scheme") {
                        navController.navigate(NavScreen.CommissionScreen.route)
                    },
                  if(role ==UserRole.RETAILER)  Pair("M-ATM Device") {
                        navController.navigate(NavScreen.DeviceOrderScreen.route)
                    } else Pair(""){},
                )
            )


        }
    }
}

@Composable
private fun HeaderContent(appPreference: AppPreference) {

    val navController = LocalNavController.current
    BoxWithConstraints(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .width(this.maxWidth)
                .height(this.maxHeight),
            backgroundColor = PrimaryColorDark

        ) {

            Canvas(
                modifier = Modifier
                    .width(this.maxWidth)
                    .height(this.maxHeight)
                    .clip(MaterialTheme.shapes.medium),
                onDraw = {
                    val path = Path().apply {
                        this.lineTo(0f, 0f)
                        this.lineTo(0f, 176.dp.toPx())
                        this.lineTo(300.dp.toPx(), 176.dp.toPx())
                        this.lineTo(0f, 0f)

                    }

                    this.drawPath(path, PrimaryColor)
                })

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.1f))
                    .padding(16.dp),

                ) {

                Row(verticalAlignment = CenterVertically) {
                    AppNetworkImage(
                        url = appPreference.user?.profilePicture.toString(),
                        shape = CircleShape, size = 70,
                        border = true,
                        onClick = {
                            navController.navigate(NavScreen.ProfileScreen.route)
                        }
                    )

                    Column() {
                        Text(
                            text = appPreference.user?.name.toString(),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = appPreference.user?.roleTitle.toString(),
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        )
                    }
                }

                Text(text = "App Version 1.1", fontSize = 12.sp)
            }

        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun BuildExpandMenu(
    menuHeading: String,
    vector: ImageVector? = null,
    drawable: Int? = null,
    menuList: List<Pair<String, VoidCallback>>
) {
    val visible = remember { mutableStateOf(false) }

    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(300)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeOut(
            animationSpec = tween(300)
        )
    }


    val transition = updateTransition(targetState = visible.value, label = "")

    val arrowRotationDegree by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "Label",
        targetValueByState = { if (it) 180f else 0f })

    Card(
        modifier = Modifier
            .padding(vertical = 2.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .clickable {
                visible.value = !visible.value
            },
        shape = MaterialTheme.shapes.small
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                if (vector != null) Icon(
                    imageVector = vector, contentDescription = null,
                    Modifier.size(32.dp)
                )
                if (drawable != null) Image(
                    painter = painterResource(id = drawable),
                    contentDescription = null,
                    Modifier
                        .size(32.dp)
                        .clip(MaterialTheme.shapes.small)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = menuHeading, modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDownCircle, contentDescription = null,
                    modifier = Modifier.rotate(arrowRotationDegree),
                    tint = Color.Gray
                )
            }

            AnimatedVisibility(
                visible = visible.value,
                initiallyVisible = false,
                enter = enterTransition,
                exit = exitTransition
            ) {

                Column {
                    Spacer(modifier = Modifier.padding(8.dp))
                    var count = 0
                    menuList.forEachIndexed { index, it ->
                        if(it.first.isNotEmpty())
                        {
                            count++
                            Row(verticalAlignment = CenterVertically) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = count.toString(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .clip(CircularShape)
                                        .height(28.dp)
                                        .width(28.dp)
                                        .padding(4.dp)
                                        .background(
                                            color = PrimaryColorDark,
                                            shape = CircularShape
                                        )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = it.first, modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { it.second.invoke() }
                                        .padding(8.dp),
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryColor,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
private fun BuildSingleItemMenu(
    text: String,
    vector: ImageVector? = null,
    drawable: Int? = null,
    callback: VoidCallback

) {

    Card(
        modifier = Modifier
            .padding(vertical = 2.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .clickable { callback.invoke() },
        shape = MaterialTheme.shapes.small
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                if (vector != null) Icon(imageVector = vector, contentDescription = null)
                if (drawable != null) Image(
                    painter = painterResource(id = drawable),
                    contentDescription = null,
                    Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = text, modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

        }
    }
}

