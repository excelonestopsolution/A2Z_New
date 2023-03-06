package com.a2z.app.ui.screen.fund.payment_gateway

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.MainActivity
import com.a2z.app.MainViewModel
import com.a2z.app.PGResultType
import com.a2z.app.R
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.CollectLatestWithScope
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.common.AmountTextField
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.common.MobileTextField
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.util.AppUtil
import com.razorpay.Checkout
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PaymentGatewayScreen(mainViewModel: MainViewModel) {

    val viewModel: PaymentGatewayViewModel = hiltViewModel()

    Scaffold(
        topBar = { NavTopBar(title = "Payment Gateway") },
        backgroundColor = BackgroundColor
    ) {

        val context = LocalContext.current
        val activity = context as MainActivity
        LaunchedEffect(key1 = Unit, block = {
            Checkout.preload(context.applicationContext)
        })

        LaunchedEffect(key1 = Unit, block = {
            viewModel.initiatePaymentFlow.collectLatest {
                val checkout = Checkout()
                checkout.setImage(R.drawable.app_logo)
                checkout.setKeyID(viewModel.initiateData?.key.toString())
                checkout.open(activity, it)
            }
        })


        BaseContent(viewModel) {
            val input = viewModel.input
            AppFormUI(
                showWalletCard = false, button = {},
                cardContents = listOf(AppFormCard {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.fund_payment_gateway),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))

                        Column(horizontalAlignment = Alignment.Start) {
                            Text(text = "Credit your wallet with")
                            Text(
                                text = "Payment Gateway",
                                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    MobileTextField(
                        value = input.mobile.getValue(),
                        onChange = { input.mobile.onChange(it) },
                        isOutline = true,
                        error = input.mobile.formError()
                    )

                    AmountTextField(
                        value = input.amount.getValue(),
                        isOutline = true,
                        onChange = { input.amount.onChange(it) },
                        error = input.mobile.formError()
                    )



                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        enabled = input.isValidObs.value,
                        onClick = {
                            viewModel.initiateTransaction()
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(text = "Proceed")
                    }

                })
            )

        }


        PgResultDialog(state = viewModel.resultDialogState) {
            viewModel.navigateUpWithResult()
        }

        CollectLatestWithScope(flow = mainViewModel.pgResult, callback = {
            when (it) {

                is PGResultType.Failure -> {
                    viewModel.resultDialogState.value = it
                    AppUtil.logger(
                        "PaymentGatewayScreen : Failure -> " +
                                "\nerrorCode : ${it.errorCode}" +
                                " \n errorResponse ${it.response} "
                    )

                }
                is PGResultType.Success -> {
                    viewModel.resultDialogState.value = it
                    AppUtil.logger(
                        "PaymentGatewayScreen : Success -> " +
                                "\n${it.razorpayPaymentId} " +
                                AppUtil.logger(
                                    "PaymentGatewayScreen : Success result : " +
                                            " userContact : ${it.data?.userContact}" +
                                            " userEmail : ${it.data?.userEmail}" +
                                            " paymentId : ${it.data?.paymentId}" +
                                            " orderId : ${it.data?.orderId}" +
                                            " signature : ${it.data?.signature}" +
                                            " data : ${it.data?.data}" +
                                            " externalWallet : ${it.data?.externalWallet}"
                                )
                    )
                }
                else -> {}
            }
        })
    }

}