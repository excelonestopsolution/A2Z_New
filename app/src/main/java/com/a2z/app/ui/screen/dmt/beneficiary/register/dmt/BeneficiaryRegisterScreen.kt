package com.a2z.app.ui.screen.dmt.beneficiary.register.dmt

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.DropDownTextField
import com.a2z.app.ui.dialog.SpinnerSearchDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.PrimaryColorDark


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BeneficiaryRegisterScreen() {

    val viewModel: BeneficiaryRegisterViewModel = hiltViewModel()

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = {
            NavTopBar(title = "Beneficiary Register")
        }
    ) {
        BaseContent(viewModel) {

            ObsComponent(flow = viewModel.bankListResultFlow) {
                val input = viewModel.input

                AppFormUI(
                    button = {
                        Button(
                            enabled = input.isValidObs.value && viewModel.selectedBank.value != null,
                            onClick = {
                                viewModel.registerBeneficiary()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        ) {
                            Text(text = "Register Beneficiary")
                        }
                    },
                    cardContents = listOf(
                        buildSenderInfo(viewModel),
                        AppFormCard(
                            contents = {

                                DropDownTextField(
                                    value = viewModel.selectedBank.value?.bankName ?: "",
                                    paddingValues = PaddingValues(
                                        vertical = 5.dp,
                                        horizontal = 0.dp
                                    ),
                                    hint = "Select Bank",
                                    downText = "Select Bank",
                                    onClick = {
                                        viewModel.spinnerDialogState.value = true
                                    })

                                AppTextField(
                                    value = input.ifscCode.getValue(),
                                    label = "IFSC Code",
                                    maxLength = 11,
                                    downText = "11 characters ifsc code",
                                    onChange = { input.ifscCode.onChange(it) },
                                    error = input.ifscCode.formError()
                                )


                                AppTextField(
                                    value = input.accountNumber.getValue(),
                                    label = "Account Number",
                                    maxLength = 20,
                                    downText = "6 to 20 digits account number",
                                    keyboardType = KeyboardType.Number,
                                    onChange = { input.accountNumber.onChange(it) },
                                    error = input.accountNumber.formError(),
                                    trailingIcon = {
                                        Button(
                                            enabled = viewModel.customValidation(),
                                            onClick = {
                                                viewModel.onVerifyClick()
                                            },
                                            contentPadding = PaddingValues(
                                                horizontal = 12.dp
                                            ),
                                            shape = CircularShape,
                                            modifier = Modifier
                                                .wrapContentSize()
                                                .padding(end = 8.dp)
                                        ) {
                                            Text(text = "Verify")
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                imageVector = Icons.Default.Verified,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                )

                                AppTextField(
                                    value = input.name.getValue(),
                                    label = "Beneficiary Name",
                                    downText = "Enter valid beneficiary name",
                                    onChange = { input.name.onChange(it) },
                                    error = input.name.formError()
                                )
                            }
                        )
                    ),
                    showWalletCard = false
                )

                SpinnerSearchDialog(
                    state = viewModel.spinnerDialogState,
                    list = viewModel.bankList.map { it.bankName.toString() }
                        .toList() as ArrayList<String>,
                    onClick = {
                        viewModel.onBankSelect(it)
                    }
                )
            }

        }
    }
}

@Composable
private fun buildSenderInfo(viewModel: BeneficiaryRegisterViewModel) =
    AppFormCard(
        title = "Sender Detail",
        contents = {

            Text(
                text = viewModel.moneySender.firstName + " " + viewModel.moneySender.lastName,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColorDark,

                    )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Mob   : ${viewModel.moneySender.mobileNumber}",
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray,
                fontSize = 14.sp
            )

        }

    )