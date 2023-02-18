package com.a2z.app.ui.screen.members.by_sale

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.data.model.member.RegisterCompleteUser
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.AppProgress
import com.a2z.app.ui.component.EmptyListComponent
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.theme.YellowColor

@Composable
fun MemberCompletedList(viewModel: MemberCreatedBySaleViewModel = hiltViewModel()) {
    val pagingState = viewModel.completePagingState
    LazyColumn {
        items(viewModel.completePagingState.items.size) { index ->

            val it = pagingState.items[index]
            pagingState.shouldLoadNext(index) {
                viewModel.fetchCompletedUsers()
            }

            BuildItem(it) {
                viewModel.userInfoDialogState.value = it
            }

        }
        item {
            if (pagingState.items.isEmpty() &&
                !pagingState.isLoading
            ) {
                Box(
                    modifier = Modifier.fillParentMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyListComponent()
                }
            }
        }
        item {
            val modifier = if (pagingState.page == 1)
                Modifier.fillParentMaxSize() else Modifier
                .fillMaxWidth()
                .height(100.dp)
            if (pagingState.isLoading && pagingState.exception == null) Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                AppProgress()
            }
        }
        item {
            val modifier = if (pagingState.page == 1)
                Modifier.fillParentMaxSize() else Modifier
                .fillMaxWidth()
                .height(100.dp)
            if (pagingState.exception != null) Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Error, contentDescription = null,
                        tint = RedColor,
                        modifier = Modifier.size(52.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = pagingState.exception!!.message.toString(),
                        fontSize = 14.sp, color = RedColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    UserInfoDialog()
}

@Composable
private fun BuildItem(user: RegisterCompleteUser, callback: (RegisterCompleteUser) -> Unit) {

    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 3.dp)
            .clickable { callback.invoke(user) },
        shape = MaterialTheme.shapes.small
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.userDetails)
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Mob : ${user.mobile}",
                    fontWeight = FontWeight.Bold
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Info, contentDescription = null,
                    Modifier.size(24.dp)
                )
                Text(
                    text = "InCompleted",
                    fontSize = 12.sp, fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Text(
                    text = "Kyc Status",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }
        }

    }

}


@Composable
private fun UserInfoDialog() {

    fun getItemStatus(item: String?) = when (item) {
        "YES" -> 1
        "NO" -> 2
        "PENDING" -> 3
        else -> 0
    }


    @Composable
    fun BuildSubItem(title: String, status: Int) {

        val (color, icon, text) = when (status) {
            1 -> Triple(GreenColor, Icons.Default.Check, "YES")
            2 -> Triple(RedColor, Icons.Default.Info, "NO")
            else -> Triple(YellowColor, Icons.Default.Info, "PENDING")
        }

        Row {
            Text(
                text = title,
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                ),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = " :    ",
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                ),
            )
            Icon(
                imageVector = icon, contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = color
                ),
                modifier = Modifier.weight(1.5f)
            )
        }

    }

    val viewModel: MemberCreatedBySaleViewModel = hiltViewModel()
    val state = viewModel.userInfoDialogState


    if (state.value != null) Dialog(onDismissRequest = {
        viewModel.userInfoDialogState.value = null
    }) {
        val item = state.value

        val isKycInComplete = item?.panVerified == "YES" &&
                item.aadhaarKyc == "YES" &&
                item.documentKyc == "YES" &&
                item.aepsKyc == "YES"


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "User Details!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )

            Divider(modifier = Modifier.padding(16.dp))


            Column(modifier = Modifier.padding(12.dp)) {


                Text("Email : ${item?.email}")
                Text("Shop  : ${item?.shopName}")

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                BuildSubItem("Pan Verified", getItemStatus(item?.panVerified))
                BuildSubItem("Aadhaar Kyc", getItemStatus(item?.aadhaarKyc))
                BuildSubItem("Document Kyc", getItemStatus(item?.documentKyc))
                BuildSubItem("Aeps Kyc", getItemStatus(item?.aepsKyc))

            }


            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {

                    if (!isKycInComplete) {
                        val userId = if(item?.id == null) "" else item.id.toString()
                        if (item?.aadhaarKyc == "NO") {
                            viewModel.navigateTo(NavScreen.AadhaarKycScreen.passArgs(userId))
                        } else if (item?.documentKyc == "NO") {
                            viewModel.navigateTo(NavScreen.DocumentKycScreen.passArgs(userId))
                        } else if (item?.panVerified == "NO")
                            viewModel.alertDialog("Please contact with admin for pan number verification")
                        else if(item?.aepsKyc == "NO")
                            viewModel.alertDialog("Please contact with admin for AEPS kyc")
                    }
                    state.value = null
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = if (isKycInComplete) "Done" else {

                        if (item?.aadhaarKyc == "NO") "Proceed Aadhaar Kyc"
                        else if (item?.documentKyc == "NO") "Proceed Document Kyc"
                        else "Done"
                    }

                )
            }

        }
    }
}