package com.a2z.app.ui.screen.indonepal.beneficiary_list

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.data.model.dmt.Beneficiary
import com.a2z.app.data.model.indonepal.INBeneficiary
import com.a2z.app.data.model.indonepal.INSender
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.EmptyListComponent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.theme.PrimaryColorDark


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun INBeneficiaryListScreen() {

    val viewModel: INBeneficiaryListViewModel = hiltViewModel()
    val navController = LocalNavController.current
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Beneficiary List") }) {_->
        BaseContent(viewModel) {
            ObsComponent(flow = viewModel.listResultFlow) {
                if (it.status == 1) {
                    if (it.data!!.isEmpty()) EmptyListComponent()
                    else {
                        LazyColumn {
                            items(it.data) {
                                BuildListItem(beneficiary = it){
                                    navController.navigate(NavScreen.INTransferScreen.passArgs(
                                        sender = viewModel.sender,
                                        beneficiary = it
                                    ))
                                }
                            }
                        }
                    }
                } else viewModel.showObsAlertDialog(it.message)
            }
        }
    }
}

@Composable
private fun BuildListItem(
    beneficiary: INBeneficiary,
    callback : (INBeneficiary)->Unit
) {

    val isVisible = remember {
        mutableStateOf(false)
    }


    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 12.dp)
            .clickable {
                isVisible.value = !isVisible.value
            }
    ) {

        Row(
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = 12.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f),) {
                Text(
                    text = beneficiary.name.orEmpty(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColorDark
                )
                Text(
                    text = beneficiary.paymentMode.orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryColorDark.copy(alpha = 0.8f)
                )

                if (beneficiary.bank_name != null ) Text(
                    text = "Bank Name : ${beneficiary.bank_name}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryColorDark.copy(alpha = 0.8f)
                )
                if (beneficiary.account_number != null) Text(
                    text = "Account : ${beneficiary.account_number}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryColorDark.copy(alpha = 0.8f)
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
                            callback.invoke(beneficiary)
                        }
                        .padding(8.dp)
                )

                Text(
                    text = "Pay",
                    fontWeight = FontWeight.SemiBold
                )
            }

        }
    }


}