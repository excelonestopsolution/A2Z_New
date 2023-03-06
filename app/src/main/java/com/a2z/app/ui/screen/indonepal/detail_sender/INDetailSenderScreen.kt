package com.a2z.app.ui.screen.indonepal.detail_sender

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person3
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.a2z.app.data.model.indonepal.INSender
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.TitleValueHorizontally
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.util.extension.safeParcelable

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun INDetailSenderScreen(navBackStackEntry: NavBackStackEntry) {

    val sender = navBackStackEntry.safeParcelable<INSender>("sender")!!

    val navController = LocalNavController.current

    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Sender Details") }) {
        AppFormUI(
            showWalletCard = false,
            button = {
                Button(
                    shape = MaterialTheme.shapes.small.copy(all = CornerSize(0.dp)),
                    onClick = {
                        navController.navigate(
                            NavScreen.INBeneficiaryScreen.passArgs(sender)
                        )
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Person3,
                        contentDescription = null,
                        Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text =
                        "Show Beneficiary List"
                    )
                }
            },
            cardContents = listOf(
                AppFormCard(
                    title = "User Details",
                    contents = {

                        val availableLimit =
                            "D:" + sender.availableDayLimit + " M:" + sender.availableMonthLimit + " Y:" + sender.availableYearLimit
                        val totalLimit =
                            "D:" + sender.totalDayLimit + " M:" + sender.totalMonthLimit + " Y:" + sender.totalYearLimit
                        val usedLimit =
                            "D:" + sender.dayLimit + " M:" + sender.monthLimit + " Y:" + sender.yearLimit

                        val kycStatus = if (sender.ekyc_status == "1") "Success" else "Pending"
                        val onboardingStatus =
                            if (sender.onboarding_status == "1") "Success" else "Pending"

                        TitleValueHorizontally(title = "Sender Name", value = sender.name)
                        TitleValueHorizontally(title = "Available Limit", value = availableLimit)
                        TitleValueHorizontally(title = "Total Limit", value = totalLimit)
                        TitleValueHorizontally(title = "Used Limit", value = usedLimit)
                        TitleValueHorizontally(title = "Kyc Status", value = kycStatus)
                        TitleValueHorizontally(title = "Oboarding Status", value = onboardingStatus)
                    }
                ),
                AppFormCard {
                    TitleValueHorizontally(title = "Sender ID", value = sender.customerId)
                    TitleValueHorizontally(title = "Sender DOB", value = sender.dob)
                    TitleValueHorizontally(title = "Sender Work Place", value = sender.employer)
                    TitleValueHorizontally(title = "Proof Type", value = sender.idType)
                    TitleValueHorizontally(title = "Proof Detail", value = sender.idNumber)
                    TitleValueHorizontally(title = "Income Source", value = sender.incomeSource)
                    TitleValueHorizontally(title = "Sender State", value = sender.state)
                    TitleValueHorizontally(title = "Sender District", value = sender.district)
                    TitleValueHorizontally(title = "Sender City", value = sender.city)
                    TitleValueHorizontally(title = "Sender Address", value = sender.address)
                }
            ))
    }
}