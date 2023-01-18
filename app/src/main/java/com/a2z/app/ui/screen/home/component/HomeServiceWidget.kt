package com.a2z.app.ui.screen.home.component

import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.a2z.app.R
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BankDownComponent
import com.a2z.app.ui.dialog.BankDownDialog
import com.a2z.app.ui.screen.dmt.util.DMTType
import com.a2z.app.ui.screen.home.HomeViewModel
import com.a2z.app.ui.screen.utility.util.OperatorType
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.util.FunCompose
import com.a2z.app.util.VoidCallback


@Composable
fun HomeServiceWidget() {
    Column {
        BuildMoneyAndPaymentService()
        BuildMoneyAEPService()
        BuildRechargeAndUtilityService()
    }
}

@Composable
private fun BuildMoneyAEPService() {
    val navController = LocalNavController.current

    ServiceCard {
        val color = Color(0xFF044274)
        Column {
            BuildServiceTitle("AEPS & MATM Service")
            Column(modifier = Modifier.padding(8.dp)) {

                Row {
                    BuildIconAndIconTitle(
                        title = "AEPS 1",
                        icon = R.drawable.ic_launcher_aeps2,
                        padding = 14.dp,
                        color = color,
                        onClick = {
                            navController.navigate(NavScreen.AepsScreen.route)
                        }
                    )
                    BuildIconAndIconTitle(
                        title = "AEPS 2",
                        icon = R.drawable.ic_launcher_aeps2,
                        padding = 14.dp,
                        color = color
                    )
                    BuildIconAndIconTitle(
                        title = "M-ATM",
                        icon = R.drawable.ic_launcher_matm,
                        padding = 12.dp,
                        color = color,
                        onClick = {
                            navController.navigate(
                                NavScreen.MatmScreen.passArgs(
                                    isMPos = false
                                )
                            )
                        }
                    )
                    BuildIconAndIconTitle(
                        title = "M-POS",
                        icon = R.drawable.ic_launcher_matm,
                        padding = 12.dp,
                        color = color,
                        onClick = {
                            navController.navigate(
                                NavScreen.MatmScreen.passArgs(
                                    isMPos = true
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BuildMoneyAndPaymentService() {
    val navController = LocalNavController.current

    ServiceCard {
        val color = Color(0xFF044274)
        Column {
            BuildServiceTitle("Money & Payment")
            Column(modifier = Modifier.padding(8.dp)) {
                Row {
                    BuildIconAndIconTitle(
                        title = "Wallet 1",
                        icon = R.drawable.ic_launcher_money,
                        color = color,
                        onClick = {
                            navController.navigate(
                                NavScreen.DmtSenderSearchScreen.passArgs(
                                    dmtType = DMTType.WALLET_1
                                )
                            )
                        }

                    )
                    BuildIconAndIconTitle(
                        title = "Wallet 2",
                        icon = R.drawable.ic_launcher_money,
                        color = color,
                        onClick = {
                            navController.navigate(
                                NavScreen.DmtSenderSearchScreen.passArgs(
                                    dmtType = DMTType.WALLET_2
                                )
                            )
                        }
                    )
                    BuildIconAndIconTitle(
                        title = "Wallet 3",
                        icon = R.drawable.ic_launcher_money,
                        color = color,
                        onClick = {
                            navController.navigate(
                                NavScreen.DmtSenderSearchScreen.passArgs(
                                    dmtType = DMTType.WALLET_3
                                )
                            )
                        }
                    )
                    BuildIconAndIconTitle(
                        title = "DMT 1",
                        icon = R.drawable.ic_launcher_money,
                        color = color,
                        onClick = {
                            navController.navigate(
                                NavScreen.DmtSenderSearchScreen.passArgs(
                                    dmtType = DMTType.DMT_3
                                )
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {

                    BuildIconAndIconTitle(
                        title = "UPI",
                        icon = R.drawable.ic_launcher_money,
                        color = color,
                        onClick = {
                            navController.navigate(
                                NavScreen.DmtSenderSearchScreen.passArgs(
                                    dmtType = DMTType.UPI
                                )
                            )
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.weight(1f))
                }

                val viewModel: HomeViewModel = hiltViewModel()
                BankDownComponent(viewModel.bankDownResponseState)

            }
        }
    }

}


@Composable
private fun BuildRechargeAndUtilityService() {
    val navController = LocalNavController.current
    val color = Color(0xFF044274)
    ServiceCard {
        Column {
            BuildServiceTitle("Recharge & Utility")
            Column(modifier = Modifier.padding(8.dp)) {
                Row {
                    BuildIconAndIconTitle(title = "Electricity",
                        icon = R.drawable.ic_launcher_electricity,
                        color = color,
                        onClick = {
                            navigateToOperatorScreen(navController, OperatorType.ELECTRICITY)
                        })
                    BuildIconAndIconTitle(title = "Water",
                        icon = R.drawable.ic_launcher_water,
                        color = color,
                        onClick = {
                            navigateToOperatorScreen(navController, OperatorType.WATER)
                        })
                    BuildIconAndIconTitle(title = "Gas",
                        icon = R.drawable.ic_launcher_gas,
                        color = color,
                        onClick = {
                            navigateToOperatorScreen(navController, OperatorType.GAS)
                        })
                    BuildIconAndIconTitle(title = "Broadband",
                        icon = R.drawable.ic_launcher_broadband,
                        color = color,
                        onClick = {

                            navigateToOperatorScreen(navController, OperatorType.BROADBAND)
                        })

                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    BuildIconAndIconTitle(title = "Prepaid",
                        icon = R.drawable.ic_launcher_mobile,
                        color = color,
                        onClick = {
                            navigateToOperatorScreen(navController, OperatorType.PREPAID)
                        })
                    BuildIconAndIconTitle(title = "Insurance",
                        icon = R.drawable.ic_launcher_insurence,
                        color = color,
                        onClick = {
                            navigateToOperatorScreen(navController, OperatorType.INSURANCE)
                        })
                    BuildIconAndIconTitle(title = "Loan Repayment",
                        icon = R.drawable.ic_launcher_insurence,
                        color = color,
                        onClick = {
                            navigateToOperatorScreen(navController, OperatorType.LOAN_REPAYMENT)
                        })
                    BuildIconAndIconTitle(title = "Postpaid",
                        icon = R.drawable.ic_launcher_mobile,
                        color = color,
                        onClick = {
                            navigateToOperatorScreen(navController, OperatorType.POSTPAID)
                        })

                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    BuildIconAndIconTitle(title = "DTH",
                        icon = R.drawable.ic_launcher_dth,
                        color = color,
                        onClick = {
                            navigateToOperatorScreen(navController, OperatorType.DTH)
                        })
                    BuildIconAndIconTitle(title = "FasTag",
                        icon = R.drawable.ic_launcher_fastag,
                        color = color,
                        onClick = {
                            navigateToOperatorScreen(navController, OperatorType.FAS_TAG)
                        })
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.weight(1f))

                }
            }
        }
    }
}


private fun navigateToOperatorScreen(navController: NavHostController, operatorType: OperatorType) {
    navController.navigate(NavScreen.OperatorScreen.passArgs(operatorType))
}

@Composable
private fun RowScope.BuildIconAndIconTitle(
    title: String,
    @DrawableRes icon: Int,
    padding: Dp = 4.dp,
    color: Color,
    onClick: VoidCallback = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(1.dp)
            .weight(1f)
    ) {
        Box(modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .clickable { onClick() }) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(padding),
                colorFilter = ColorFilter.tint(color = color)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.W500,
                color = Color.Black.copy(alpha = 0.7f)
            ),
            textAlign = TextAlign.Center,

            )
    }
}

@Composable
private fun BuildServiceTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.W500, fontSize = 18.sp),
        modifier = Modifier

            .padding(12.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Start
    )
}


@Composable
private fun ServiceCard(content: FunCompose) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) { content() }
}
