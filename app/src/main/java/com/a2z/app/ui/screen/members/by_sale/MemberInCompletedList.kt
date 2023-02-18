package com.a2z.app.ui.screen.members.by_sale

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.data.model.member.RegisterCompleteUser
import com.a2z.app.data.model.member.RegisterInCompleteUser
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.AppProgress
import com.a2z.app.ui.component.EmptyListComponent
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.theme.YellowColor

@Composable
fun MemberInCompletedList(viewModel: MemberCreatedBySaleViewModel = hiltViewModel()) {
    val pagingState = viewModel.inCompletePagingState
    val navController = LocalNavController.current
    LazyColumn {
        items(viewModel.inCompletePagingState.items.size) { index ->

            val it = pagingState.items[index]
            pagingState.shouldLoadNext(index) {
                viewModel.fetchInCompletedUsers()
            }

            BuildItem(it){
                navController.navigate(NavScreen.RegistrationTypeScreen.passArgs(
                    shouldMap = true,
                    mobileNumber = it.mobile
                ))
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
}

@Composable
private fun BuildItem(user: RegisterInCompleteUser, callback: (RegisterInCompleteUser) -> Unit) {

    fun getItemStatus(item: String) = when (item) {
        "YES" -> 1
        "NO" -> 2
        "PENDING" -> 3
        else -> 0
    }
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 3.dp)
            .clickable {
                callback.invoke(user)
            },
        shape = MaterialTheme.shapes.small
    ) {

        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = "MOB : ${user.mobile}",
                fontWeight = FontWeight.Bold
            )
            Divider(modifier = Modifier.padding(vertical = 5.dp))

            BuildSubItem("Mobile Verified", getItemStatus(user.isMobileVerified))
            BuildSubItem("Email Verified", getItemStatus(user.isEmailVerified))
            BuildSubItem("Pan Verified", getItemStatus(user.isPanVerified))

        }
    }

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