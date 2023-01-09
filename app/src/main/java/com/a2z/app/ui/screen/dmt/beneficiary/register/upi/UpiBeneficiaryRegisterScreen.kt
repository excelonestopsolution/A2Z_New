package com.a2z.app.ui.screen.dmt.beneficiary.register.upi

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.theme.RedColor
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UpiBeneficiaryRegisterScreen() {

    val viewModel: UpiBeneficiaryRegisterViewModel = hiltViewModel()

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Upi Beneficiary Register") }) {


        BaseContent(viewModel) {

            ObsComponent(flow = viewModel.vpaListResultFlow) {
                AppFormUI(button = {
                    Button(
                        onClick = {
                            viewModel.navigateTo(NavScreen.QRScanScreen.route)
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(text = "Register UPI Beneficiary")
                    }
                },
                    showWalletCard = false,
                    cardContents = listOf(
                        AppFormCard(
                            title = "Upi Providers",
                            contents = {
                                FlowRow(
                                    mainAxisAlignment = FlowMainAxisAlignment.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    it.upiBank?.forEach {

                                        val backgroundColor =
                                            if (viewModel.selectedBankState.value == it)
                                                MaterialTheme.colors.primary else Color.Gray.copy(
                                                alpha = 1f
                                            )
                                        val contentColor =
                                            if (viewModel.selectedBankState.value == it)
                                                Color.White else Color.White

                                        val style = ButtonDefaults.buttonColors(
                                            backgroundColor = backgroundColor,
                                            contentColor = contentColor
                                        )

                                        Button(
                                            onClick = {
                                                viewModel.onSelectBank(it)
                                            }, modifier = Modifier.padding(
                                                horizontal = 4.dp
                                            ),
                                            colors = style,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(it.name.toString())
                                        }
                                    }
                                }

                            }
                        ),
                        AppFormCard(
                            contents = {
                                AppTextField(value = "", label = "Upi Id")
                                AppTextField(value = "", label = "Beneficiary Name")

                                Row(
                                    modifier = Modifier.padding(vertical = 5.dp)
                                ) {
                                    Text("Extension : ")
                                    Text(
                                        "11122233444@icici",
                                        color = RedColor
                                    )
                                }

                                if (viewModel.selectedBankState.value != null
                                    && viewModel.selectedBankState.value?.name != "Bank Upi"
                                ) FlowRow(
                                    mainAxisAlignment = FlowMainAxisAlignment.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    viewModel.upiExtension.forEach {

                                        Button(
                                            onClick = {
                                                viewModel.onSelectExtension(it)
                                            }, modifier = Modifier.padding(
                                                horizontal = 4.dp
                                            ),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(it.name.toString())
                                        }
                                    }
                                }

                                Card(
                                    shape = CircleShape,
                                    modifier = Modifier.padding(8.dp)
                                        .align(CenterHorizontally)
                                        .clickable {
                                            viewModel.navigateTo(NavScreen.QRScanScreen.route)
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = null,
                                        modifier = Modifier.padding(8.dp).size(40.dp)
                                    )
                                }
                                Text(text = "Scan QR",
                                    color = PrimaryColor,
                                    fontWeight = FontWeight.W500,
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(CenterHorizontally)
                                )
                            }
                        )
                    ))
            }
        }


    }
}