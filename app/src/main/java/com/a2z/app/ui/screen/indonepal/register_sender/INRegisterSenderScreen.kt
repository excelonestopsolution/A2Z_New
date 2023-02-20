package com.a2z.app.ui.screen.indonepal.register_sender

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.util.extension.removeDateSeparator


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun INRegisterSenderScreen() {
    val viewModel: INRegisterSenderViewModel = hiltViewModel()
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Register Sender") }
    ) {

        BaseContent(viewModel) {
            val input = viewModel.input
            AppFormUI(
                showWalletCard = false,
                button = {
                    Button(
                        enabled = viewModel.input.isValidObs.value && viewModel.validateOtp.value,
                        onClick = { viewModel.onProceed() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(text = "Register Sender")
                    }
                },
                cardContents = listOf(
                    AppFormCard(title = "Fill Details") {
                        AppTextField(
                            value = input.fullName.formValue(),
                            label = "Full Name",
                            onChange = { input.fullName.onChange(it) },
                            error = input.fullName.formError()
                        )
                        AppDropDownMenu(
                            selectedState = viewModel.genderState,
                            label = "Sender Gender",
                            list = viewModel.staticData.gender.map { it.name }
                        )

                        DateTextField(
                            value = input.senderDob.formValue(),
                            label = "Sender Date of Birth",
                            onChange = { input.senderDob.onChange(it) },
                            error = input.senderDob.formError(),
                            onDateSelected = { input.senderDob.onChange(it.removeDateSeparator()) })


                        AppTextField(
                            value = input.senderWorkPlace.formValue(),
                            label = "Sender Work Place",
                            onChange = { input.senderWorkPlace.onChange(it) },
                            error = input.senderWorkPlace.formError()
                        )
                        AppTextField(
                            value = input.senderEmailId.formValue(),
                            label = "Sender Email Id",
                            onChange = { input.senderEmailId.onChange(it) },
                            error = input.senderEmailId.formError()
                        )
                        AppDropDownMenu(
                            selectedState = viewModel.senderProofTypeState,
                            label = "Sender Proof Type",
                            list = viewModel.staticData.senderProofType.map { it.name }
                        )
                        AppTextField(
                            value = input.senderProofType.formValue(),
                            label = "Sender Proof Detail",
                            onChange = { input.senderProofType.onChange(it) },
                            error = input.senderProofType.formError()
                        )
                        AppDropDownMenu(
                            selectedState = viewModel.senderIncomeSourceState,
                            label = "Sender Income Source",
                            list = viewModel.staticData.incomeSource.map { it.name }
                        )

                        AppDropDownMenu(
                            selectedState = viewModel.countryStateState,
                            label = "Sender State",
                            list = viewModel.staticData.stateLists.map { it.name },
                            onSelect = {viewModel.onCountryStateChanged(it)}
                        )

                        AppDropDownMenu(
                            selectedState = viewModel.districtState,
                            label = "Sender District",
                            list = viewModel.districtList.map { it.distic },

                        )


                        AppTextField(
                            value = input.senderCity.formValue(),
                            label = "Sender City",
                            onChange = { input.senderCity.onChange(it) },
                            error = input.senderCity.formError()
                        )
                        AppTextField(
                            value = input.senderAddress.formValue(),
                            label = "Sender Address",
                            onChange = { input.senderAddress.onChange(it) },
                            error = input.senderAddress.formError()
                        )

                        Row (verticalAlignment = Alignment.Bottom){
                            Box(modifier = Modifier.weight(1f)) {
                                PinTextField(
                                    value = input.otp.formValue(),
                                    onChange = { input.otp.onChange(it) },
                                    error = input.otp.formError()
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Button(
                                onClick = {
                                    viewModel.validateOtp.value = false
                                    val result = input.validate()
                                    if(result){
                                        viewModel.onSendOtp()
                                    }

                                }) {
                                Text(text = "Send OTP")
                            }

                        }
                    }
                )
            )
        }


    }


}