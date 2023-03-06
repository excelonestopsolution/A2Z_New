package com.a2z.app.ui.screen.settlement.add_bank

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.HtmlText
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.DropDownTextField
import com.a2z.app.ui.dialog.SpinnerSearchDialog
import com.a2z.app.ui.theme.BackgroundColor


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettlementAddBankScreen() {

    val viewModel: SettlementAddBankViewModel = hiltViewModel()
    val input = viewModel.input

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Settlement Bank Add") }
    ) {
        BaseContent(viewModel) {
            ObsComponent(flow = viewModel.bankListResultFlow) {
                viewModel.bankList = it.data!!.bankList
                AppFormUI(
                    showWalletCard = false,
                    button = {
                        Button(
                            enabled = input.isValidObs.value && viewModel.selectedBank.value != null,
                            onClick = { viewModel.addBank() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        ) {
                            Text(text = "Add Settlement Bank")
                        }
                    }, cardContents = listOf(
                        AppFormCard(
                            title = "Note : ",
                            contents = {
                                val text = viewModel.parseToHTMLTextString(it.data!!.information)
                                HtmlText(html = text)
                            }
                        ),
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
                                    onChange = { input.ifscCode.onChange(it) },
                                    label = "Universal IFSC Code",
                                    error = input.ifscCode.formError(),
                                    maxLength = 11
                                )

                                AppTextField(
                                    value = input.accountNumber.getValue(),
                                    onChange = { input.accountNumber.onChange(it) },
                                    label = "Account Number",
                                    error = input.accountNumber.formError(),
                                    keyboardType = KeyboardType.Number,
                                    maxLength = 20,
                                )

                                AppTextField(
                                    value = input.confirmAccount.getValue(),
                                    onChange = { input.confirmAccount.onChange(it) },
                                    label = "Confirm Account",
                                    error = input.confirmAccount.formError(),
                                    keyboardType = KeyboardType.Number,
                                    maxLength = 20,
                                )

                            }
                        )
                    ))


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