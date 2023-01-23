package com.a2z.app.ui.screen.fund.payment_return

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.data.model.fund.PaymentReturnDetailResponse
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.common.AmountTextField
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.dialog.BaseConfirmDialog
import com.a2z.app.ui.theme.BackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ParentPaymentReturnScreen() {

    val viewModel: ParentPaymentReturnViewModel = hiltViewModel()
    val input = viewModel.input


    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Parent Payment Return") }
    ) {
        BaseContent(viewModel) {
            ObsComponent(flow = viewModel.detailResultFlow) {
                if (it.status == 1) {
                    viewModel.encReceiverId = it.data?.encReceiverId.orEmpty()
                    FormContent(input, viewModel, it)
                } else EmptyListComponent()
            }
        }
    }

}

@Composable
private fun FormContent(
    input: ParentPaymentReturnViewModel.FormInput,
    viewModel: ParentPaymentReturnViewModel,
    it: PaymentReturnDetailResponse
) {
    AppFormUI(
        showWalletCard = true,
        button = {
            Button(
                enabled = input.isValidObs.value,
                onClick = { viewModel.onSubmit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(text = "Submit")
            }
        }, cardContents = listOf(

            AppFormCard {

                TitleValueHorizontally(title = "ID", value = it.data?.id.toString())
                TitleValueHorizontally(
                    title = "Name",
                    value = it.data?.name.toString()
                )
                TitleValueHorizontally(
                    title = "Mobile",
                    value = it.data?.mobile.toString()
                )

                Spacer(modifier = Modifier.height(16.dp))

                AmountTextField(
                    value = input.amount.getValue(),
                    isOutline = true,
                    onChange = { input.amount.onChange(it) },
                    error = input.amount.formError()
                )

                AppTextField(
                    value = input.remark.getValue(),
                    label = "Remark", isOutline = true,
                    onChange = { input.remark.onChange(it) },
                    error = input.remark.formError()
                )

            }

        ))
    BaseConfirmDialog(
        state = viewModel.confirmDialogVisibleState,
        amount = input.amount.getValue(),
        titleValues = listOf(
            "Name" to it.data?.name.toString(),
            "Mobile" to it.data?.mobile.toString()
        )
    ) {
        viewModel.mpinDialogVisibleState.value = true
    }
    MpinInputComponent(
        visibleState = viewModel.mpinDialogVisibleState,
        amount = input.amount.getValue(),
        onSubmit = {
            viewModel.onPinSubmit(it)
        })
}