package com.a2z.app.ui.screen.indonepal.beneficiary_add

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.dialog.SpinnerSearchDialog
import com.a2z.app.ui.theme.BackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun INBeneficiaryAddScreen() {

    val viewModel: INBeneficiaryAddViewModel = hiltViewModel()



    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Add Beneficiary") }
    ) {

        BaseContent(viewModel) {

            val input = viewModel.input
            AppFormUI(
                showWalletCard = false,
                button = {
                    Button(
                        enabled = viewModel.input.isValidObs.value,
                        onClick = { viewModel.onProceed() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(text = "Add Beneficiary")
                    }
                },
                cardContents = listOf(
                    AppFormCard {
                        AppTextField(
                            value = input.name.formValue(),
                            label = "Full Name",
                            onChange = { input.name.onChange(it) },
                            error = input.name.formError(),
                            downText = "Receiver full name"
                        )
                        AppDropDownMenu(
                            selectedState = viewModel.genderState,
                            label = "Sender Gender",
                            list = viewModel.staticData.gender.map { it.name },
                            downText = "Receiver Gender"
                        )

                        MobileTextField(value = input.mobile.formValue(), onChange = {
                            input.mobile.onChange(
                                it
                            )
                        }, error = input.mobile.formError(),
                        )


                        AppDropDownMenu(
                            selectedState = viewModel.relationShipState,
                            label = "Relation Ship",
                            list = viewModel.staticData.relationShip.map { it.name },
                            downText = "Relationship with receiver"
                        )

                        AppTextField(
                            value = input.address.formValue(),
                            label = "Address",
                            onChange = { input.address.onChange(it) },
                            error = input.address.formError(),
                            downText = "Address of receiver"
                        )


                        AppDropDownMenu(
                            selectedState = viewModel.paymentModeState,
                            label = "Payment Type",
                            list = viewModel.staticData.paymentMode.map { it.name },
                            onSelect = {
                                viewModel.validateAccountNumber.value = it == "Account Deposit"
                                Unit
                            }
                        )


                    },
                    AppFormCard(
                        title = "Bank Details",
                        isVisible = viewModel.paymentModeState.value == "Account Deposit"
                    ) {
                        AppTextField(
                            value = input.accountNumber.formValue(),
                            label = "Account Number",
                            onChange = { input.accountNumber.onChange(it) },
                            error = input.accountNumber.formError(),
                        )
                        DropDownTextField(
                            value = viewModel.bankState.value,
                            onClick = {
                                viewModel.bankDialogState.value = true
                            },
                            hint = "Select Bank",
                            downText = "Select Nepal Bank",
                            paddingValues = PaddingValues(horizontal = 0.dp, vertical = 5.dp)
                        )

                        DropDownTextField(
                            value = viewModel.branchState.value,
                            onClick = {
                                viewModel.branchDialog.value = true
                            },
                            hint = "Select Branch",
                            downText = "Select Bank Branch",
                            paddingValues = PaddingValues(horizontal = 0.dp, vertical = 5.dp)
                        )
                    }
                )
            )

        }

    }

    SpinnerSearchDialog(
        state = viewModel.bankDialogState,
        list = viewModel.staticData.bankName.map { it.name }.toList() as ArrayList<String>,
        onClick = {
            viewModel.bankState.value = it
            viewModel.fetchBranchList(it)
        }
    )

    SpinnerSearchDialog(
        state = viewModel.branchDialog,
        mutableList = viewModel.branchList.map { it.branchName } as MutableList<String>,
        list = arrayListOf(),
        onClick = {
            viewModel.branchState.value = it
        }
    )
}