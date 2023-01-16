package com.a2z.app.ui.screen.auth.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.PasswordTextField
import com.a2z.app.ui.theme.GreenColor

@Composable
fun UserRegistrationFinalContent() {

    val viewModel : UserRegistrationViewModel = hiltViewModel()

    val detail = viewModel.finalResponseState.value?.details

    Column() {

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                BuildItem(
                    title = "Mobile Number",
                    value = detail?.mobile.toString(),
                    icon = Icons.Default.PhoneAndroid
                )

                BuildItem(
                    title = "Email Id",
                    value = detail?.email.toString(),
                    icon = Icons.Default.Message
                )

                BuildItem(
                    title = "Pan Card",
                    value = detail?.pan_card.toString(),
                    icon = Icons.Default.CreditCard
                )

                BuildItem(
                    title = "Name (as per pan card)",
                    value = detail?.name.toString(),
                    icon = Icons.Default.Person
                )

                Button(onClick = { viewModel.onResetPanVerification() }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Reset Pan Verification")
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
        Card(
            Modifier
                .fillMaxWidth()) {

            val input = viewModel.finalInput

            Column(modifier = Modifier.padding(16.dp)) {
                AppTextField(
                    value = input.shopName.getValue(),
                    label = "Shop Name",
                    error = input.shopName.formError(),
                    onChange = {input.shopName.onChange(it)
                    }
                )
                AppTextField(
                    value = input.shopAddress.getValue(),
                    label = "Shop Address",
                    error = input.shopAddress.formError(),
                    onChange = {input.shopAddress.onChange(it)
                    }
                )
                PasswordTextField(
                    value = input.password.getValue(),
                    label = "Password",
                    error = input.password.formError(),
                    onChange = {input.password.onChange(it)
                    }
                )
                PasswordTextField(
                    value = input.confirmPassword.getValue(),
                    label = "Confirm Password",
                    error = input.confirmPassword.formError(),
                    onChange = {input.confirmPassword.onChange(it)
                    }
                )
            }
        }
    }
}

@Composable
private fun BuildItem(title: String, value: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.padding(vertical = 4.dp)) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text(text = title, color = Color.Gray)
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = value, fontWeight = FontWeight.SemiBold, color = Color.Black)
            }
        }

        Image(
            painter = painterResource(id = R.drawable.icon_sucess), contentDescription = null,
            modifier = Modifier.size(24.dp), colorFilter = ColorFilter.tint(GreenColor)
        )
    }
}