package com.a2z.app.ui.screen.dmt.beneficiary.info

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.a2z.app.data.model.dmt.Beneficiary
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.EmptyListComponent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.common.SearchTextField
import com.a2z.app.ui.dialog.ConfirmActionDialog
import com.a2z.app.ui.dialog.OTPVerifyDialog
import com.a2z.app.ui.screen.dmt.util.DMTType
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.util.extension.singleResult
import com.a2z.app.util.AppConstant


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BeneficiaryListInfoScreen(navBackStackEntry: NavBackStackEntry) {
    val viewModel: BeneficiaryListInfoViewModel = hiltViewModel()

    Scaffold(
        topBar = { NavTopBar(title = "Beneficiary List") },
        backgroundColor = BackgroundColor
    )
    {

        BaseContent(viewModel) {
            ObsComponent(flow = viewModel.beneficiaryFlow) {

                Column() {
                    HeaderComponent()
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 8.dp)

                    ) {

                        if (viewModel.showBeneficiaryList.isNotEmpty())
                            LazyColumn() {
                                itemsIndexed(
                                    viewModel.showBeneficiaryList,

                                    ) { index, beneficiary ->
                                    BuildListItem(
                                        beneficiary,
                                        index,
                                        viewModel,
                                        onDelete = { viewModel.onDelete(it) },
                                        onVerify = { viewModel.onVerify(it) }
                                    )
                                }
                            }
                        else EmptyListComponent()
                    }
                }

            }

            ConfirmActionDialog(
                state = viewModel.confirmDeleteDialogState,
                title = "Confirm Delete ?",
                description = "You are sure! to delete" +
                        " ${viewModel.beneficiaryState.value?.accountNumber}. This action can't be undo.",
                buttonText = "Delete"
            ) {
                viewModel.deleteBeneficiary()
            }

            ConfirmActionDialog(
                state = viewModel.confirmVerifyDialogState,
                title = "Confirm Verify ?",
                description = "Confirm verification for account number" +
                        " ${viewModel.beneficiaryState.value?.accountNumber}",
                buttonText = "Verify"
            ) {
                viewModel.onVerificationConfirm()
            }

            OTPVerifyDialog(
                state = viewModel.otpVerifyDialogState,
                otpLength = 4
            ) {
                viewModel.deleteBeneficiaryOtp(it)
            }
        }


    }

    LaunchedEffect(key1 = Unit) {
        val isRegister = navBackStackEntry.singleResult<Boolean>("isRegistered")
        if (isRegister == true) viewModel.fetchBeneficiary()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun BuildListItem(
    beneficiary: Beneficiary,
    index: Int,
    viewModel: BeneficiaryListInfoViewModel,
    onDelete: (Beneficiary) -> Unit,
    onVerify: (Beneficiary) -> Unit
) {

    val isVisible = remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier
        .background(color = if (index == 0 && viewModel.swipeState.value) PrimaryColorDark.copy(0.1f) else Color.White)
        .clickable {
            isVisible.value = !isVisible.value
        }) {

        val dmtType = viewModel.dmtType
        val isUpi = dmtType == DMTType.UPI

        Row(
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = 12.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = if (beneficiary.bankVerified == 1
                    || beneficiary.upiBankVerified == 1
                ) GreenColor else Color.Gray,
                modifier = Modifier.size(20.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = beneficiary.name.orEmpty(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (beneficiary.bankVerified == 1 || beneficiary.upiBankVerified == 1) GreenColor else Color.Gray
                )
                if (!isUpi) Text(
                    text = beneficiary.bankName.orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryColorDark
                )

                val accountTitle = if (isUpi) "" else "Account    : "
                Text(
                    text = accountTitle + beneficiary.accountNumber.orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryColorDark
                )
                val ifscTitle = if (isUpi) "Provider  : " else "Ifsc Code : "
                val ifscValue = if (isUpi) beneficiary.bankName else beneficiary.ifsc
                if (ifscValue.toString().isNotEmpty()) Text(
                    text = ifscTitle + ifscValue.orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryColorDark
                )
                if (!beneficiary.lastSuccessTime.isNullOrEmpty()) Text(
                    text = "Last Success : " + beneficiary.lastSuccessTime.orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = GreenColor
                )
            }


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .border(
                            width = 1.dp,
                            color = Color.DarkGray,
                            shape = CircleShape
                        )
                        .rotate(-15f)
                        .clickable {
                            viewModel.onSendClick(beneficiary)
                        }.padding(8.dp)
                )

                Text(
                    text = "Send",
                    fontWeight = FontWeight.SemiBold
                )
            }

        }

        AnimatedContent(targetState = isVisible.value) { target ->
            if (target)
                Column {

                    val buttonPaddingValue = PaddingValues(vertical = 0.dp, horizontal = 12.dp)
                    Divider()
                    Row(Modifier.padding(vertical = 8.dp, horizontal = 12.dp)) {
                        Button(
                            onClick = { onDelete.invoke(beneficiary) },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = RedColor,
                                contentColor = Color.White,

                                ), contentPadding = buttonPaddingValue
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "Delete")
                        }
                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = { onVerify.invoke(beneficiary) },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = GreenColor,
                                contentColor = Color.White
                            ), contentPadding = buttonPaddingValue
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Verify")
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = if (beneficiary.bankVerified == 1 || beneficiary.upiBankVerified == 1) "Re-verified" else "Verified")
                        }
                    }
                }
        }

        Divider()

    }
}

@Composable
private fun HeaderComponent() {
    val viewModel: BeneficiaryListInfoViewModel = hiltViewModel()
    Card(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = viewModel.moneySender.firstName + " " + viewModel.moneySender.lastName,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryColorDark,

                            )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Mob   : ${viewModel.moneySender.mobileNumber}",
                        fontWeight = FontWeight.Normal,
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Limit : ${AppConstant.RUPEE_SYMBOL} ${viewModel.moneySender.usedLimit}",
                        fontWeight = FontWeight.Normal,
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(
                        shape = CircleShape,
                        backgroundColor = GreenColor,
                        modifier = Modifier.clickable {
                            if (viewModel.dmtType == DMTType.UPI) viewModel.navigateTo(
                                NavScreen.UpiBeneficiaryRegisterScreen.passArgs(
                                    moneySender = viewModel.moneySender
                                )
                            )
                            else viewModel.navigateTo(
                                NavScreen.DmtBeneficiaryRegisterScreen.passArgs(
                                    moneySender = viewModel.moneySender
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp),
                            tint = Color.White
                        )

                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    val addNewText = if (viewModel.dmtType == DMTType.UPI)
                        "Add New\nID" else "Add New\nBeneficiary"
                    Text(
                        text = addNewText,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }

            val searchValue = remember {
                mutableStateOf("")
            }

            SearchTextField(
                paddingValues = PaddingValues(horizontal = 0.dp, vertical = 4.dp),
                value = searchValue.value,
                onQuery = { query ->
                    searchValue.value = query

                    if (query.isEmpty()) {
                        viewModel.showBeneficiaryList.clear()
                        viewModel.showBeneficiaryList.addAll(
                            viewModel.mainBeneficiaryList
                        )
                    } else {

                        val filterList = viewModel.mainBeneficiaryList.filter {
                            viewModel.filterListCondition(it, query)
                        }
                        viewModel.showBeneficiaryList.clear()
                        viewModel.showBeneficiaryList.addAll(filterList)


                    }

                },
                onClear = {
                    searchValue.value = ""
                    viewModel.showBeneficiaryList.clear()
                    viewModel.showBeneficiaryList.addAll(
                        viewModel.mainBeneficiaryList
                    )
                })
        }
    }

}