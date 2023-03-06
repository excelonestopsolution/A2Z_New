package com.a2z.app.ui.screen.home.retailer.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.a2z.app.R
import com.a2z.app.data.model.dmt.BankDownResponse
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BankDownComponent
import com.a2z.app.ui.screen.aeps.AepsType
import com.a2z.app.ui.screen.dmt.util.DMTType
import com.a2z.app.ui.screen.home.retailer.RetailerHomeViewModel
import com.a2z.app.ui.screen.utility.util.OperatorType
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.util.FunCompose
import com.a2z.app.util.VoidCallback


@Composable
fun HomeServiceWidget() {
    Column {
        BuildMoneyAndPaymentService()
        PGAndTravelService()
        BuildMoneyAEPService()
        BuildRechargeService()
        BuildUtilityService()
    }
}

@Composable
private fun BuildMoneyAEPService() {
    val navController = LocalNavController.current
    val viewModel: RetailerHomeViewModel = hiltViewModel()

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
                            if (viewModel.dmtAndAEPSKycPendingState.value)
                                viewModel.checkKycInfo()
                            else navController.navigate(
                                NavScreen.AepsScreen.passArgs(
                                    aepsType = AepsType.AEPS_1
                                )
                            )
                        }
                    )
                    BuildIconAndIconTitle(
                        title = "AEPS 3",
                        icon = R.drawable.ic_launcher_aeps2,
                        padding = 14.dp,
                        color = color,
                        onClick = {
                            if (viewModel.dmtAndAEPSKycPendingState.value)
                                viewModel.checkKycInfo()
                            else navController.navigate(
                                NavScreen.AepsScreen.passArgs(
                                    aepsType = AepsType.AEPS_3
                                )
                            )
                        }
                    )
                    BuildIconAndIconTitle(
                        title = "M-ATM",
                        icon = R.drawable.ic_launcher_matm,
                        padding = 12.dp,
                        color = color,
                        onClick = {
                            if (viewModel.dmtAndAEPSKycPendingState.value)
                                viewModel.checkKycInfo()
                            else navController.navigate(
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
                            if (viewModel.dmtAndAEPSKycPendingState.value)
                                viewModel.checkKycInfo()
                            else navController.navigate(
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
    val viewModel: RetailerHomeViewModel = hiltViewModel()

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

                            if (viewModel.dmtKycPendingState.value)
                                viewModel.checkKycInfo()
                            else navController.navigate(
                                NavScreen.DmtSenderSearchScreen.passArgs(
                                    dmtType = DMTType.WALLET_1,
                                    bankDown = viewModel.bankDownResponseState.value
                                        ?: BankDownResponse(
                                            status = 2,
                                            message = ""
                                        )
                                )
                            )
                        }

                    )
                    BuildIconAndIconTitle(
                        title = "Wallet 2",
                        icon = R.drawable.ic_launcher_money,
                        color = color,
                        onClick = {
                            if (viewModel.dmtKycPendingState.value)
                                viewModel.checkKycInfo()
                            else navController.navigate(
                                NavScreen.DmtSenderSearchScreen.passArgs(
                                    dmtType = DMTType.WALLET_2,
                                    bankDown = viewModel.bankDownResponseState.value
                                        ?: BankDownResponse(
                                            status = 2,
                                            message = ""
                                        )
                                )
                            )
                        }
                    )
                    BuildIconAndIconTitle(
                        title = "Wallet 3",
                        icon = R.drawable.ic_launcher_money,
                        color = color,
                        onClick = {
                            if (viewModel.dmtKycPendingState.value)
                                viewModel.checkKycInfo()
                            else navController.navigate(
                                NavScreen.DmtSenderSearchScreen.passArgs(
                                    dmtType = DMTType.WALLET_3,
                                    bankDown = viewModel.bankDownResponseState.value
                                        ?: BankDownResponse(
                                            status = 2,
                                            message = ""
                                        )
                                )
                            )
                        }
                    )
                    BuildIconAndIconTitle(
                        title = "DMT 1",
                        icon = R.drawable.ic_launcher_money,
                        color = color,
                        onClick = {
                            if (viewModel.dmtKycPendingState.value)
                                viewModel.checkKycInfo()
                            else navController.navigate(
                                NavScreen.DmtSenderSearchScreen.passArgs(
                                    dmtType = DMTType.DMT_3,
                                    bankDown = viewModel.bankDownResponseState.value
                                        ?: BankDownResponse(
                                            status = 2,
                                            message = ""
                                        )
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
                            if (viewModel.dmtKycPendingState.value)
                                viewModel.checkKycInfo()
                            else navController.navigate(
                                NavScreen.DmtSenderSearchScreen.passArgs(
                                    dmtType = DMTType.UPI,
                                    bankDown = BankDownResponse(
                                        status = 2,
                                        message = "",
                                    )
                                )
                            )
                        }
                    )

                    BuildIconAndIconTitle(
                        title = "UPI 2",
                        icon = R.drawable.ic_launcher_money,
                        color = color,
                        onClick = {
                            if (viewModel.dmtKycPendingState.value)
                                viewModel.checkKycInfo()
                            else navController.navigate(
                                NavScreen.DmtSenderSearchScreen.passArgs(
                                    dmtType = DMTType.UPI_2,
                                    bankDown = BankDownResponse(
                                        status = 2,
                                        message = "",
                                    )
                                )
                            )
                        }
                    )

                    BuildIconAndIconTitle(
                        title = "Indo Nepal",
                        icon = R.drawable.ic_launcher_money,
                        color = color,
                        onClick = {
                            if (viewModel.appPreference.user?.indoNepal == "0")
                                navController.navigate(NavScreen.INServiceActivationScreen.route)
                            else navController.navigate(NavScreen.INSearchSenderScreen.route)
                        }
                    )


                    Spacer(modifier = Modifier.weight(1f))

                }
                Spacer(modifier = Modifier.height(8.dp))

                BankDownComponent(viewModel.bankDownResponseState)

            }
        }
    }

}

@Composable
private fun PGAndTravelService() {
    val navController = LocalNavController.current
    val viewModel: RetailerHomeViewModel = hiltViewModel()

    ServiceCard {
        val color = Color(0xFF044274)
        Column {
            BuildServiceTitle("Pan Card & Travel")
            Column(modifier = Modifier.padding(8.dp)) {

                Row{
                    BuildIconAndIconTitle(
                        title = "Flight Hotel",
                        icon = R.drawable.flight_hotel,
                        color = color,
                        padding = 10.dp,
                        onClick = {
                            viewModel.flightHotelRedirectUrl()
                        }
                    )
                    BuildIconAndIconTitle(
                        title = "Pan Service",
                        icon = R.drawable.pan_icon,
                        color = color,
                        padding = 10.dp,
                        onClick = {
                            if (viewModel.appPreference.user?.isPanCardActivated == 1)
                                viewModel.panAutoLogin()
                            else navController.navigate(NavScreen.PanServiceScreen.route)
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

}


@Composable
private fun BuildRechargeService() {
    val navController = LocalNavController.current
    val color = Color(0xFF044274)
    ServiceCard {
        Column {
            BuildServiceTitle("Recharge")
            Column(modifier = Modifier.padding(8.dp)) {

                Row {
                    BuildIconAndIconTitle(title = "Prepaid",
                        icon = R.drawable.ic_launcher_mobile,
                        color = color,
                        onClick = {
                            navigateToOperatorScreen(navController, OperatorType.PREPAID)
                        })
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


                }

            }
        }
    }
}


@Composable
private fun BuildUtilityService() {
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

                    BuildIconAndIconTitle(title = "Insurance",
                        icon = R.drawable.ic_launcher_insurence,
                        color = color,
                        onClick = {
                            navigateToOperatorScreen(navController, OperatorType.INSURANCE)
                        })
                    BuildIconAndIconTitle(title = "Loan Repayment",
                        icon = R.drawable.loan,
                        color = color,
                        onClick = {
                            navigateToOperatorScreen(navController, OperatorType.LOAN_REPAYMENT)
                        }, padding = 12.dp)
                    BuildIconAndIconTitle(title = "Postpaid",
                        icon = R.drawable.ic_launcher_mobile,
                        color = color,
                        onClick = {
                            navigateToOperatorScreen(navController, OperatorType.POSTPAID)
                        })
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
