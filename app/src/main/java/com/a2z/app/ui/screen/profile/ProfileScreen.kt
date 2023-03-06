package com.a2z.app.ui.screen.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.TitleValueHorizontally
import com.a2z.app.ui.component.common.AppNetworkImage
import com.a2z.app.ui.dialog.OTPVerifyDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.util.extension.prefixRS
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen() {
    val viewModel: ProfileViewModel = hiltViewModel()
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "User Profile") }
    ) {
        BaseContent(viewModel) {

            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                shape = MaterialTheme.shapes.small,

                ) {

                val user = viewModel.user!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AppNetworkImage(
                        url = user.profilePicture,
                        shape = CircleShape, size = 90,
                        border = true,
                    )

                    Text(
                        text = user.name, style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryColor,
                        )
                    )
                    Text(
                        text = user.shopName, style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryColor.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    )


                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .border(
                                width = 1.dp,
                                color = PrimaryColorDark,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(8.dp)
                    ) {

                        BuildMobileEmailVerifyComponent(
                            title = "Mobile",
                            value = user.mobile,
                            isVerified = user.isMobileVerified == 1
                        )

                        Divider(Modifier.padding(vertical = 5.dp))

                        BuildMobileEmailVerifyComponent(
                            title = "Email",
                            value = user.email,
                            isVerified = user.isEmailVerified == 1
                        )
                        Divider(Modifier.padding(vertical = 5.dp))

                        FlowRow(mainAxisAlignment = FlowMainAxisAlignment.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()) {
                          if(user.isMobileVerified != 1)  OutlinedButton(onClick = {
                              viewModel.requestMobileVerifyOtp()
                          }) {
                                Text(text = "Verify Mobile")
                            }

                          if(user.isEmailVerified != 1)
                              OutlinedButton(onClick = {
                              viewModel.requestEmailVerifyOtp()
                          }) {
                                Text(text = "Verify Email")
                            }
                           /* OutlinedButton(onClick = { *//*TODO*//* }) {
                                Text(text = "Change Email")
                            }*/
                        }

                    }

                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .border(
                                width = 1.dp,
                                color = PrimaryColorDark,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(8.dp)
                    ) {
                        TitleValueHorizontally(
                            title = "Balance",
                            value = user.userBalance.prefixRS()
                        )
                        TitleValueHorizontally(title = "Role", value = user.roleTitle)
                        TitleValueHorizontally(title = "Address", value = user.address)
                        TitleValueHorizontally(title = "Shop Address", value = user.shopAddress)
                        TitleValueHorizontally(title = "Joining Date", value = user.joiningDate)
                        TitleValueHorizontally(title = "Last Update", value = user.lastUpdate)
                    }


                }

            }

            OTPVerifyDialog(
                state = viewModel.showOtpDialogState,
                onAction = {
                    viewModel.onOtpSubmit(it)
            })

        }
    }


}

@Composable
private fun BuildMobileEmailVerifyComponent(
    title: String, value: String,isVerified : Boolean
) {

    Row {
        Text(text = title, modifier = Modifier.weight(1f))
        Text(" : ")
        Text(text = value, modifier = Modifier.weight(2f))
        Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = null,
            tint = if (isVerified) GreenColor else Color.LightGray,
            modifier = Modifier.size(20.dp)
        )

    }



}