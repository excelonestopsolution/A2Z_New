package com.a2z.app.ui.screen.dmt.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.data.model.dmt.Beneficiary
import com.a2z.app.data.model.dmt.UpiStaticMessage
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.util.AppConstant
import com.a2z.app.util.VoidCallback

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UpiVerifyAccountDialog(
    state: MutableState<Boolean>,
    upiMessage: UpiStaticMessage?,
    beneficiary: Beneficiary,
    onConfirmWithoutVerify: VoidCallback,
    onConfirmVerifyUpiId: VoidCallback,

    ) {

    val confirmUnVerifiedDialogState = rememberStateOf(value = false)
    val verifyTxnDialogState = rememberStateOf(value = false)


    if (state.value) Dialog(
        onDismissRequest = {
            state.value = false
        }, properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Verify UPI Account",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )

            Divider(modifier = Modifier.padding(16.dp))
            Image(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                colorFilter = ColorFilter.tint(RedColor)
            )
            Spacer(modifier = Modifier.height(16.dp))

            upiMessage?.declarationMessage?.forEach {
                Text(
                    text = it,
                    textAlign = TextAlign.Start,
                    lineHeight = 22.sp,
                    color = Color.Red.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(
                    onClick = {
                        state.value = false
                        confirmUnVerifiedDialogState.value = true
                    },
                ) {
                    Text(text = "Skip")
                }

                Spacer(modifier = Modifier.weight(1f))


                Button(
                    onClick = {
                        state.value = false
                        verifyTxnDialogState.value = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = GreenColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Proceed with " + AppConstant.RUPEE_SYMBOL + " 1")
                }
            }


        }

    }

    ConfirmUnVerifyAccountDialog(
        state = confirmUnVerifiedDialogState,
        upiMessage = upiMessage,
        onCancel = {
            confirmUnVerifiedDialogState.value = false
            state.value = true
        },
        onProceed = {
            confirmUnVerifiedDialogState.value = false
            onConfirmWithoutVerify.invoke()
        })

    UpiVerifyTxnDialog(
        state = verifyTxnDialogState,
        upiMessage = upiMessage,
        beneficiary = beneficiary,
        onProceed = { onConfirmVerifyUpiId.invoke() }
    )


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ConfirmUnVerifyAccountDialog(
    state: MutableState<Boolean>,
    upiMessage: UpiStaticMessage?,
    onCancel: VoidCallback,
    onProceed: VoidCallback
) {


    if (state.value) Dialog(
        onDismissRequest = {
            state.value = false
        }, properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Account Not Verified",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )

            Divider(modifier = Modifier.padding(16.dp))
            Image(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                colorFilter = ColorFilter.tint(RedColor)
            )
            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = upiMessage?.warningMessage?.get(1) ?: "",
                textAlign = TextAlign.Start,
                lineHeight = 22.sp,
                color = Color.Red.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(
                    onClick = { onCancel.invoke() },
                ) {
                    Text(text = "Cancel")
                }

                Spacer(modifier = Modifier.weight(1f))


                Button(
                    onClick = { onProceed.invoke() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = GreenColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Confirm & Proceed")
                }
            }


        }

    }


}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun UpiVerifyTxnDialog(
    state: MutableState<Boolean>,
    upiMessage: UpiStaticMessage?,
    beneficiary: Beneficiary,
    onProceed: VoidCallback
) {

    /* val titleValues = listOf(
         Pair("Amount",upiMessage?.chargeDetails?.txnAmount.toString()),
         Pair("Txn Charge",upiMessage?.chargeDetails?.txnCharge.toString()),
         Pair("Total",upiMessage?.chargeDetails?.totalAmount.toString()),
     )*/

    val titleValues = listOf(
        Pair("Sender Mobile", beneficiary.mobileNumber ?: ""),
        Pair("UPI Id", beneficiary.accountNumber ?: ""),
        Pair("Name", beneficiary.name ?: ""),
        Pair("Amount", upiMessage?.chargeDetails?.txnAmount.toString()),
        Pair("Txn Charge", upiMessage?.chargeDetails?.txnCharge.toString()),
        Pair("Total", upiMessage?.chargeDetails?.totalAmount.toString()),

        )

    @Composable
    fun BuildTitleValue(title: String, value: String) {

        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Row {
                Text(
                    text = title, style = MaterialTheme.typography.subtitle2.copy(
                        color = Color.Black.copy(alpha = 0.5f),
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text(text = "    :    ", fontWeight = FontWeight.W500)
                Text(
                    text = value, style = MaterialTheme.typography.subtitle1.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier.weight(2.5f)
                )
            }
            Divider()
        }

    }


    if (state.value) Dialog(
        onDismissRequest = {
            state.value = false
        }, properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Transaction Detail",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )

            Divider(modifier = Modifier.padding(16.dp))


            titleValues.forEach {

                if (it.second.isNotEmpty())
                    BuildTitleValue(title = it.first, value = it.second)

            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(
                    onClick = { state.value = false },
                ) {
                    Text(text = "Close")
                }

                Spacer(modifier = Modifier.weight(1f))


                Button(
                    onClick = {
                        state.value = false
                        onProceed.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = GreenColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Confirm & Pay")
                }
            }


        }

    }


}