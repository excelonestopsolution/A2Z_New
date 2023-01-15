package com.a2z.app.ui.screen.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.data.local.AppPreference
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.common.AppNetworkImage
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.util.VoidCallback

@Composable
fun ColumnScope.DashboardDrawerWidget(appPreference: AppPreference) {

    val navController = LocalNavController.current

    Box(
        modifier = Modifier
            .weight(1f)
            .background(color = PrimaryColorDark.copy(0.1f))
    ) {
        Column(Modifier.scrollable(rememberScrollState(), Orientation.Vertical)) {

            Spacer(modifier = Modifier.height(8.dp))
            BuildNavLogo(appPreference)
            Spacer(modifier = Modifier.height(8.dp))

            BuildSingleMenu(
                "Change Password",
                Icons.Default.Password
            ) {
                navController.navigate(NavScreen.ChangePasswordScreen.route)
            }
            BuildSingleMenu("Change Pin", Icons.Default.Security) {
                navController.navigate(NavScreen.ChangePinScreen.route)
            }
            BuildSingleMenu("AEPS Settlement", Icons.Default.Fingerprint) {
                navController.navigate(NavScreen.SettlementBankScreen.route)
            }

            BuildSingleMenu("Document Kyc", Icons.Default.CloudUpload) {
                navController.navigate(NavScreen.DocumentKycScreen.route)
            }

            BuildSingleMenu("My Commission", Icons.Default.Money) {
                navController.navigate(NavScreen.CommissionScreen.route)
            }

            BuildSingleMenu("M-ATM Service", drawable = com.a2z.app.R.drawable.ic_launcher_matm) {
                navController.navigate(NavScreen.DeviceOrderScreen.route)
            }

            BuildSingleMenu("User Agreement", drawable = com.a2z.app.R.drawable.icon_report) {
                navController.navigate(NavScreen.UserAgreementScreen.route)
            }

        }
    }
}


@Composable
private fun BuildSingleMenu(
    title: String,
    vector: ImageVector? = null,
    drawable: Int? = null,
    onClick: VoidCallback,
) {

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small
    ) {
        Row(modifier = Modifier.clickable { onClick() }.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically) {
            if (vector != null) Icon(imageVector = vector, contentDescription = null)
            if (drawable != null) Image(
                painter = painterResource(id = drawable),
                contentDescription = null,
                Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title, style = MaterialTheme.typography.h6.copy(
                    fontSize = 16.sp
                )
            )
        }
    }
}


@Composable
private fun BuildNavLogo(appPreference: AppPreference) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = MaterialTheme.shapes.small,
        backgroundColor = PrimaryColorDark
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            AppNetworkImage(
                url = appPreference.user?.profilePicture.toString(),
                shape = CircleShape, size = 90
            )
            Text(
                text = appPreference.user?.name ?: "",
                fontWeight = FontWeight.SemiBold, color = Color.White
            )
            Text(
                text = appPreference.user?.roleTitle ?: "",
                color = Color.White.copy(0.7f),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}