package com.a2z.app.ui.screen.members.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.data.model.member.Member
import com.a2z.app.ui.component.TitleValueHorizontally
import com.a2z.app.ui.component.common.AmountTextField
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.util.extension.prefixRS

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MemberFundTransferDialog(
    viewModel: MemberListViewModel,
    onProceed: (String, String) -> Unit
) {

    if (viewModel.showTransferDialogState.value) Dialog(
        onDismissRequest = {
            viewModel.showTransferDialogState.value = false
                           },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        LaunchedEffect(key1 = Unit, block = {
            viewModel.transferFormInput.amount.setValue("")
            viewModel.transferFormInput.amount.clearFormError()
            viewModel.transferFormInput.remark.setValue("")
            viewModel.transferFormInput.remark.clearFormError()
        })

        val input = viewModel.transferFormInput


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(MaterialTheme.shapes.small)
                .background(Color.White)
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Fund Transfer",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )

            Divider(modifier = Modifier.padding(16.dp))
            BuildRequestInfo(viewModel.member.value)

            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .border(1.dp, PrimaryColor, MaterialTheme.shapes.small)
            ) {

                AmountTextField(
                    value = input.amount.getValue(),
                    onChange = { input.amount.onChange(it) },
                    isOutline = false,
                    error = input.amount.formError()
                )

                AppTextField(
                    value = input.remark.getValue(),
                    label = "Remark",
                    onChange = { input.remark.onChange(it) },
                    error = input.remark.formError()
                )
            }


            Spacer(modifier = Modifier.height(16.dp))


            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { viewModel.showTransferDialogState.value = false },
                    modifier = Modifier.height(52.dp)
                ) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (input.validate()) {
                            viewModel.showTransferDialogState.value = false
                            onProceed.invoke(
                                input.amount.getValue(),
                                input.remark.getValue()
                            )
                        }
                    }, modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                ) {
                    Text(text = "Confirm & Transfer")
                }
            }

        }
    }

}

@Composable
private fun BuildRequestInfo(item: Member?) {

    Column(
        modifier = Modifier
            .border(1.dp, PrimaryColor, MaterialTheme.shapes.small)
            .padding(8.dp)
    ) {

        TitleValueHorizontally(title = "Amount", value = item?.balance?.prefixRS())
        TitleValueHorizontally(title = "Agent Name", value = item?.name)
        TitleValueHorizontally(title = "Shop Name", value = item?.shopName)
        TitleValueHorizontally(title = "Mobile Number", value = item?.mobile)

    }

}