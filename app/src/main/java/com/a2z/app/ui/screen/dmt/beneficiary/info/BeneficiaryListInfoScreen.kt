package com.a2z.app.ui.screen.dmt.beneficiary.info

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.Composable
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
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.SearchTextField
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.util.AppConstant


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BeneficiaryListInfoScreen() {
    Scaffold(
        topBar = { NavTopBar(title = "Beneficiary List") },
        backgroundColor = BackgroundColor
    )
    {
        val viewModel: BeneficiaryListInfoViewModel = hiltViewModel()

        BaseContent(viewModel) {
            ObsComponent(flow = viewModel.beneficiaryFlow) {

                Column {
                    HeaderComponent()
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 8.dp)
                    ) {

                        if (it.status == 22 && it.data != null && it.data!!.isNotEmpty()) {
                            viewModel.mainBeneficiaryList = it.data!!

                            viewModel.showBeneficiaryList.addAll(it.data!!)
                            LazyColumn {
                                items(it.data!!) {

                                    Column(

                                    ) {

                                        Row(
                                            modifier = Modifier.padding(
                                                vertical = 12.dp,
                                                horizontal = 12.dp
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Verified,
                                                contentDescription = null,
                                                tint = if (it.bankVerified == 1) GreenColor else Color.Gray,
                                                modifier = Modifier.size(20.dp)
                                            )

                                            Column() {
                                                Text(
                                                    text = it.name.orEmpty(),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (it.bankVerified == 1) GreenColor else Color.Gray
                                                )
                                                Text(
                                                    text = it.bankName.orEmpty(),
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = PrimaryColorDark
                                                )
                                                Text(
                                                    text = "Account    : " + it.accountNumber.orEmpty(),
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = PrimaryColorDark
                                                )
                                                Text(
                                                    text = "Ifsc Code : " + it.ifsc.orEmpty(),
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = PrimaryColorDark
                                                )
                                                if (!it.lastSuccessTime.isNullOrEmpty()) Text(
                                                    text = "Last Success : " + it.lastSuccessTime.orEmpty(),
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    color = GreenColor
                                                )
                                            }
                                            Spacer(modifier = Modifier.weight(1f))

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
                                                        .padding(8.dp)
                                                )

                                                Text(
                                                    text = "Send",
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }

                                        }

                                        Divider()

                                    }
                                }
                            }
                        }
                    }

                }
            }
        }


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
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Mob   : ${viewModel.moneySender.mobileNumber}",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Limit : ${AppConstant.RUPEE_SYMBOL} ${viewModel.moneySender.usedLimit}",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(
                        shape = CircleShape,
                        backgroundColor = GreenColor
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp),
                            tint = Color.White
                        )

                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Add New\nBeneficiary",
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
                onQuery = {

                },
                onClear = {
                    searchValue.value = ""
                })
        }
    }

}