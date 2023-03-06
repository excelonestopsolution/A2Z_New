package com.a2z.app.ui.screen.r2r

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.TitleValueVertically
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.dialog.BaseConfirmDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.CircularShape

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun R2RTransferScreen() {

    val viewModel: R2RTransferViewModel = hiltViewModel()
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "R2R Transfer") }
    ) {


        BaseContent(viewModel) {
            AppFormUI(
                button = {
                    Button(
                        enabled = viewModel.r2rRetailer.value != null
                                && viewModel.input.isValidObs.value,
                        onClick = {
                            viewModel.confirmDialogState.value = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text("Transfer")
                    }
                },
                cardContents = listOf(
                    AppFormCard(
                        title = "Search Retailer Mobile",
                        contents = {
                           // BuildTransactionType(viewModel = viewModel)

                            AppTextField(
                                keyboardType = KeyboardType.Number,
                                value = viewModel.input.number.getValue(),
                                label = viewModel.inputLabel,
                                isOutline = true,
                                maxLength = viewModel.numberValidationLength.value,
                                onChange = {
                                    viewModel.input.number.onChange(it)
                                    viewModel.amountValidation.value = false
                                    viewModel.r2rRetailer.value = null
                                },
                                error = viewModel.input.number.formError(),
                                trailingIcon = {
                                    Button(
                                        enabled = viewModel.input.isValidObs.value,
                                        onClick = {
                                            viewModel.onSearchClick()
                                        },
                                        contentPadding = PaddingValues(
                                            horizontal = 12.dp
                                        ),
                                        shape = CircularShape,
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(end = 8.dp)
                                    ) {
                                        Text(text = "Search")
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null
                                        )
                                    }
                                })

                        }
                    ),
                    AppFormCard(
                        title = "Retailer Details",
                        isVisible = viewModel.r2rRetailer.value != null,
                        contents = {
                            TitleValueVertically(
                                title = "Retailer Name",
                                value = viewModel.r2rRetailer.value!!.name
                            )
                            TitleValueVertically(
                                title = "Shop Name",
                                value = viewModel.r2rRetailer.value!!.shopName
                            )
                            TitleValueVertically(
                                title = "Mobile Number",
                                value = viewModel.r2rRetailer.value!!.mobile
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            AmountTextField(
                                value = viewModel.input.amount.getValue(),
                                onChange = { viewModel.input.amount.onChange(it) },
                                error = viewModel.input.amount.formError(),
                                isOutline = true,
                                downText = "Enter Transfer Amount"
                            )
                            AppTextField(
                                value = viewModel.input.remark.getValue(),
                                label = "Remark",
                                hint = "Optional",
                                onChange = {viewModel.input.remark.onChange(it)}
                            )
                        }
                    )
                )
            )
            BaseConfirmDialog(
                state = viewModel.confirmDialogState,
                amount = viewModel.input.amount.getValue(), titleValues = listOf(
                    "Mobile Number" to viewModel.r2rRetailer.value?.mobile.toString(),
                    "Retailer Name" to viewModel.r2rRetailer.value?.name.toString(),
                    "Shop Name" to viewModel.r2rRetailer.value?.shopName.toString(),
                )
            ) {
                viewModel.transfer()
            }


        }


    }
}

@Composable
fun ColumnScope.BuildTransactionType(viewModel: R2RTransferViewModel) {

    Row {
        BuildRadioButton(R2RTransferSearchType.MOBILE) {
            viewModel.onSearchTypeChange(it)
        }
        BuildRadioButton(R2RTransferSearchType.ID) {
            viewModel.onSearchTypeChange(it)
        }
    }

}

@Composable
private fun RowScope.BuildRadioButton(
    type: R2RTransferSearchType,
    onChange: (R2RTransferSearchType) -> Unit
) {
    val viewModel: R2RTransferViewModel = hiltViewModel()

    val title = when (type) {
        R2RTransferSearchType.MOBILE -> "Mobile"
        R2RTransferSearchType.ID -> "ID"
    }


    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
        RadioButton(selected = viewModel.searchType.value == type, onClick = {
            onChange.invoke(type)
        })
        Text(text = title)
    }
}
