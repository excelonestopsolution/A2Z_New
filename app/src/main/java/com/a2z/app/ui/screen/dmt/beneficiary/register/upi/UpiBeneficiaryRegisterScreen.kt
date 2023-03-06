package com.a2z.app.ui.screen.dmt.beneficiary.register.upi

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.dialog.ConfirmActionDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.util.extension.singleResult
import com.a2z.app.util.AppUtil
import com.a2z.app.util.extension.showToast
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UpiBeneficiaryRegisterScreen(
    navBackStackEntry: NavBackStackEntry,
) {

    val viewModel: UpiBeneficiaryRegisterViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit, block = {
        val qrcode = navBackStackEntry.singleResult<String>("qrcode")
        val upiJson = AppUtil.urlToJson(qrcode) ?: return@LaunchedEffect

        val upiId = upiJson.optString("pa")
        val upiName = upiJson.optString("pn").replace("%20", " ")

        if (upiId.isNotEmpty() && upiId.contains("@")) {

            viewModel.selectedBankState.value = null
            viewModel.input.upiId.setValue("")
            viewModel.input.upiId.setValue(upiId)
            if (upiName.isNotEmpty()
                && !upiName.toLowerCase(Locale.ROOT).contains("paytm merchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("phonepe merchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("phonepemerchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("googlepaymerchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("google pay merchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("googlepay merchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("bharatpemerchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("bharatpaymerchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("bharat pay merchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("bharatpay merchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("whatsapp merchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("whatsappmerchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("amazonpaymerchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("amazonpay merchant")
                && !upiName.toLowerCase(Locale.ROOT).contains("amazon pay merchant")
                && !upiName.contains("*")
            ) {
                viewModel.actionType.value = UpiBeneficiaryRegisterViewModel.ActionType.REGISTER
                viewModel.input.name.setValue(upiName)
                viewModel.nameValidation.value = true
                viewModel.verificationDoneByApi = false
            }

        } else {
            context.showToast("Unable to fetch information please fill data manually!")
        }
    })


    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Upi Beneficiary Register") }) {



        BaseContent(viewModel) {

            ObsComponent(flow = viewModel.vpaListResultFlow) {
                AppFormUI(

                    button = {
                    Button(
                        enabled = viewModel.input.isValidObs.value,
                        onClick = {
                            viewModel.onProceedButtonClick()
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(text = viewModel.buttonText)
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

                                val input = viewModel.input

                                AppTextField(
                                    readOnly = !viewModel.inputEnable,
                                    value = input.upiId.getValue(),
                                    label = "Upi Id",
                                    onChange = { input.upiId.onChange(it) },
                                    error = input.upiId.formError()
                                )
                                AppTextField(
                                    value = input.name.getValue(),
                                    label = "Beneficiary Name",
                                    onChange = { input.name.onChange(it) },
                                    error = input.name.formError(),
                                    readOnly = !viewModel.inputEnable,
                                )

                                Row(
                                    modifier = Modifier.padding(vertical = 5.dp)
                                ) {
                                    Text("Extensions : ")
                                    if (viewModel.selectedBankState.value == null
                                        || viewModel.selectedBankState.value!!.name == "Bank Upi"
                                    ) Text(
                                        "11122233444@icici",
                                        color = PrimaryColor
                                    )
                                }



                                if (viewModel.selectedBankState.value != null
                                    && viewModel.selectedBankState.value?.name != "Bank Upi"
                                ) FlowRow(
                                    mainAxisAlignment = FlowMainAxisAlignment.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    viewModel.upiExtensions.forEach {

                                        val backgroundColor = viewModel.getExtensionColor()
                                        val contentColor = Color.White

                                        Button(
                                            onClick = {
                                                viewModel.onSelectExtension(it)
                                            }, modifier = Modifier.padding(
                                                horizontal = 4.dp
                                            ),
                                            shape = RoundedCornerShape(4.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = backgroundColor,
                                                contentColor = contentColor
                                            )
                                        ) {
                                            Text(it.name.toString())
                                        }
                                    }
                                }


                                if (viewModel.actionType.value
                                    == UpiBeneficiaryRegisterViewModel.ActionType.VERIFY
                                ) Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.align(CenterHorizontally)
                                ) {
                                    Card(
                                        shape = CircleShape,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable {

                                                viewModel.navigateTo(NavScreen.QRScanScreen.route)
                                            }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.QrCode,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .size(40.dp)
                                        )
                                    }
                                    Text(
                                        text = "Scan QR",
                                        color = PrimaryColor,
                                        fontWeight = FontWeight.W500,
                                        fontSize = 12.sp,
                                    )
                                }

                                if (viewModel.actionType.value
                                    == UpiBeneficiaryRegisterViewModel.ActionType.REGISTER
                                ) Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.align(CenterHorizontally)
                                ) {
                                    Card(
                                        shape = CircleShape,
                                        backgroundColor = RedColor,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable {
                                                if(!viewModel.verificationDoneByApi)
                                                viewModel.onReset()
                                                else viewModel.confirmResetDialog.value = true
                                            }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .size(32.dp)
                                        )
                                    }
                                    Text(
                                        text = "Reset Verification\nDetails",
                                        color = PrimaryColor,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.W500,
                                        fontSize = 12.sp,
                                    )
                                }
                            }
                        )
                    ))

                ConfirmActionDialog(
                    title = "Confirm Verification ? ",
                    description = "Verification for upi id ${viewModel.input.upiId.getValue()} ?"
                            + "\n\nIt will charge for rs ${viewModel.chargeAmount.value.toString()}",
                    buttonText = "Verify",
                    state = viewModel.confirmVerificationDialog,
                ) {

                    viewModel.verifyUpiId()
                }

                ConfirmActionDialog(
                    title = "Confirm Reset ? ",
                    description = "Are you sure! to reset verification details",
                    buttonText = "Reset Detail",
                    state = viewModel.confirmResetDialog,
                ) {

                    viewModel.onReset()
                }

            }
        }


    }
}