package com.a2z.app.ui.screen.dmt.sender.search

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.screen.matm.MatmTransactionType
import com.a2z.app.ui.screen.matm.MatmViewModel
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.util.AppConstant
import com.a2z.app.util.VoidCallback


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchSenderScreen() {

    val viewModel: SearchSenderViewModel = hiltViewModel()
    Scaffold(
        topBar = { NavTopBar(title = "Search Sender") },
        backgroundColor = BackgroundColor,

        ) {
        BaseContent(viewModel) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {

                SearchSenderByComponent()

                if (viewModel.senderBeneficiaries.value.isNotEmpty())
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(top = 12.dp)
                    ) {
                        LazyColumn {

                            items(viewModel.senderBeneficiaries.value) {

                                val primaryColor = MaterialTheme.colors.primary

                                Column(modifier = Modifier.clickable {
                                    viewModel.searchType.value = SenderSearchType.MOBILE
                                    viewModel.input.number.setValue(it.mobileNumber)
                                    viewModel.senderBeneficiaries.value = listOf()
                                    viewModel.onSearchClick()
                                }) {
                                    Column(modifier = Modifier.padding(12.dp)) {

                                        Text(
                                            it.bankName ?: "",
                                            style = MaterialTheme.typography.subtitle1.copy(
                                                color = primaryColor,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )


                                        Text(
                                            it.name,
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = primaryColor.copy(alpha = 0.8f)
                                            ),
                                        )
                                        Text(
                                            "Ifsc : " + it.ifscCode,
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                color = primaryColor.copy(alpha = 0.8f)
                                            ),
                                        )

                                        Text(
                                            "Bal  : "+AppConstant.RUPEE_SYMBOL + it.remainingLimit.toString(),
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                color = primaryColor.copy(alpha = 0.8f)
                                            ),
                                        )



                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Linked with : ${it.mobileNumber}",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.DarkGray
                                            )
                                        )
                                        if (it.lastSuccessTime != null) Text(
                                            "Last Success : " + it.lastSuccessTime,
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                color = GreenColor.copy(alpha = 0.8f)
                                            ),
                                        )

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


@Composable
private fun SearchSenderByComponent() {

    val viewModel: SearchSenderViewModel = hiltViewModel()
    Card {

        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Search By",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                SearchButton(searchType = SenderSearchType.MOBILE) {
                    viewModel.onSearchTypeClick(SenderSearchType.MOBILE)
                }
                SearchButton(searchType = SenderSearchType.ACCOUNT) {
                    viewModel.onSearchTypeClick(SenderSearchType.ACCOUNT)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            AppTextField(
                keyboardType = KeyboardType.Number,
                value = viewModel.input.number.getValue(),
                label = viewModel.inputLabel,
                isOutline = true,
                maxLength = viewModel.numberValidationLength.value,
                onChange = { viewModel.input.number.onChange(it) },
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
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                })

        }


    }

}

@Composable
private fun RowScope.SearchButton(
    searchType: SenderSearchType,
    onClick: VoidCallback
) {


    val title = when (searchType) {
        SenderSearchType.MOBILE -> "Mobile"
        SenderSearchType.ACCOUNT -> "Account"
    }

    val viewModel: SearchSenderViewModel = hiltViewModel()

    val backgroundColor = if (viewModel.searchType.value == searchType)
        MaterialTheme.colors.primary else Color.Gray.copy(alpha = 0.7f)
    val contentColor = if (viewModel.searchType.value == searchType)
        Color.White else Color.White

    val style = ButtonDefaults.buttonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 5.dp),
        colors = style,
        shape = CircleShape
    ) {
        Text(text = title, fontSize = 12.sp)
    }
}