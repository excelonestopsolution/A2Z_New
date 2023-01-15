package com.a2z.app.ui.screen.fund.request

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.data.model.fund.FundRequestBank
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.component.permission.PermissionComponent
import com.a2z.app.ui.dialog.ImageDialog
import com.a2z.app.ui.screen.util.permission.AppPermissionList
import com.a2z.app.ui.screen.util.permission.PermissionType
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.util.FileUtil
import com.a2z.app.util.extension.removeDateSeparator

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FundRequestScreen() {
    val viewModel: FundRequestViewModel = hiltViewModel()
    val navController = LocalNavController.current
    BaseContent(viewModel) {
        Scaffold(
            backgroundColor = BackgroundColor,
            topBar = { NavTopBar(title = "Fund Request") }) { _ ->


            val input = viewModel.input
            val dateInput = input.dateInput
            val amountInput = input.amountInput
            val bankRefInput = input.bankRefInput
            val paymentModeInput = input.paymentModeInput
            val uploadSlipInput = input.slipUploadInput

            val context = LocalContext.current.applicationContext



            AppFormUI(
                showWalletCard = false,
                button = {
                    AppButton(
                        isEnable = input.isValidObs.value,
                        text = "Proceed",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) { viewModel.makeFundRequest() }
                }, cardContents = listOf(

                    AppFormCard {
                        if (viewModel.fundRequestBank != null)
                            BuildTopSection(bank = viewModel.fundRequestBank!!)
                    },

                    AppFormCard(
                        contents = {
                            AppTextField(
                                value = paymentModeInput.formValue(),
                                label = "Payment Type",
                                error = paymentModeInput.formError(),
                                readOnly = true,
                                onChange = { paymentModeInput.onChange(it) }
                            )
                            DateTextField(
                                value = dateInput.formValue(),
                                label = "Payment Date",
                                onChange = { dateInput.onChange(it) },
                                error = dateInput.formError(),
                                onDateSelected = { dateInput.onChange(it.removeDateSeparator()) })
                            AmountTextField(
                                value = amountInput.formValue(),
                                error = amountInput.formError(),
                                onChange = { amountInput.onChange(it) }
                            )
                            AppTextField(
                                value = bankRefInput.formValue(),
                                label = viewModel.getRefPlaceholder() ?: "Bank Ref",
                                error = bankRefInput.formError(),
                                onChange = { bankRefInput.onChange(it) }
                            )

                            PickCameraAndGalleryImage(
                                onResult = {
                                    viewModel.selectedFileUri.value = it
                                    val file = FileUtil.getFile(context, it)
                                    viewModel.selectedFile = file
                                    uploadSlipInput.setValue(file?.name.toString())
                                },
                                content = { capture ->
                                    PermissionComponent(
                                        permissions = AppPermissionList.cameraStorages()
                                            .map { it.permission }) { child ->
                                        FileTextField(
                                            value = uploadSlipInput.formValue(),
                                            label = "Upload Slip",
                                            hint = "Select File",
                                            error = uploadSlipInput.formError(),
                                            onTrailingClick = {
                                                viewModel.isImageDialogOpen.value = true
                                            }
                                        ) {
                                            val childResult = child.invoke()
                                            if (childResult) capture.invoke()
                                            else {
                                                navController.navigate(
                                                    NavScreen.PermissionScreen.passData(
                                                        permissionType = PermissionType.CameraAndStorage
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            )

                        }
                    )
                ))

            ImageDialog(viewModel.isImageDialogOpen, viewModel.selectedFileUri)


        }
    }
}

@Composable
private fun BuildTopSection(bank: FundRequestBank) {


    Column {

        val primaryColor = MaterialTheme.colors.primary

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = primaryColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    bank.bankName ?: "",
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = primaryColor,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    ("Branch : " + bank.branchName), style = TextStyle(
                        fontSize = 12.sp,
                        color = primaryColor.copy(alpha = 0.8f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TitleValueVertically(title = "Account Number", value = bank.account_number)
        TitleValueVertically(title = "Ifsc Code", value = bank.ifsc)

        Spacer(modifier = Modifier.height(8.dp))
        if (bank.charges != null && bank.charges!!.isNotEmpty()) {
            Text(
                bank.charges ?: "", style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Red.copy(alpha = 0.8f)
                )
            )
        }
    }

}