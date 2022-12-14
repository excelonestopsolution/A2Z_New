package com.a2z.app.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.a2z.app.InitialRouteType
import com.a2z.app.MainViewModel
import com.a2z.app.ui.component.AppQRScanScreen
import com.a2z.app.ui.screen.aeps.AepsScreen
import com.a2z.app.ui.screen.auth.change.password.ChangePasswordScreen
import com.a2z.app.ui.screen.auth.change.pin.ChangePinScreen
import com.a2z.app.ui.screen.auth.login.LoginScreen
import com.a2z.app.ui.screen.auth.verification.LoginOtpScreen
import com.a2z.app.ui.screen.dashboard.DashboardScreen
import com.a2z.app.ui.screen.dmt.beneficiary.info.BeneficiaryListInfoScreen
import com.a2z.app.ui.screen.dmt.beneficiary.register.dmt.BeneficiaryRegisterScreen
import com.a2z.app.ui.screen.dmt.beneficiary.register.upi.UpiBeneficiaryRegisterScreen
import com.a2z.app.ui.screen.dmt.sender.register.SenderRegisterScreen
import com.a2z.app.ui.screen.dmt.sender.search.SearchSenderScreen
import com.a2z.app.ui.screen.dmt.transfer.DMTMoneyTransferScreen
import com.a2z.app.ui.screen.fund.bank.FundBankListScreen
import com.a2z.app.ui.screen.fund.method.FundMethodScreen
import com.a2z.app.ui.screen.fund.request.FundRequestScreen
import com.a2z.app.ui.screen.matm.MatmScreen
import com.a2z.app.ui.screen.util.permission.PermissionScreen
import com.a2z.app.ui.screen.qrcode.ShowQRCodeScreen
import com.a2z.app.ui.screen.r2r.R2RTransferScreen
import com.a2z.app.ui.screen.result.*
import com.a2z.app.ui.screen.settlement.add_bank.SettlementAddBankScreen
import com.a2z.app.ui.screen.settlement.bank_list.SettlementBankScreen
import com.a2z.app.ui.screen.settlement.transfer.SettlementTransferScreen
import com.a2z.app.ui.screen.test.TestScreen
import com.a2z.app.ui.screen.util.DeviceLockScreen
import com.a2z.app.ui.screen.utility.bill.BillPaymentScreen
import com.a2z.app.ui.screen.utility.operator.OperatorScreen
import com.a2z.app.ui.screen.utility.recharge.RechargeScreen
import com.a2z.app.ui.theme.LocalNavController


@Composable
fun MainNav(viewModel: MainViewModel, initialRouteType: InitialRouteType) {

    val startDestination = when (initialRouteType) {
        InitialRouteType.LOGIN_PAGE -> NavScreen.LoginScreen.route
        InitialRouteType.DASHBOARD_PAGE -> NavScreen.DashboardScreen.route
        InitialRouteType.DEVICE_LOCK_PAGE -> NavScreen.DeviceLockScreen.route
    }



    NavHost(
        navController = LocalNavController.current,
        startDestination = NavScreen.SettlementBankScreen.route,
        route = "main-root"
    ) {

        composable(
            route = NavScreen.DeviceLockScreen.route,
            content = {
                DeviceLockScreen()
            })



        composable(
            route = NavScreen.LoginScreen.route,
            content = { LoginScreen(it) })

        composable(
            route = NavScreen.LoginOtpScreen.route
        ) {
            val mobileNumber: String = it.arguments?.getString("mobile") ?: ""
            LoginOtpScreen(mobileNumber)
        }
        composable(route = NavScreen.DashboardScreen.route, content = { DashboardScreen() })

        composable(
            route = NavScreen.OperatorScreen.route,
            content = { OperatorScreen() }
        )
        composable(
            route = NavScreen.RechargeScreen.route,
            content = { RechargeScreen() })
        composable(
            route = NavScreen.BillPaymentScreen.route,
            content = { BillPaymentScreen() })
        composable(
            route = NavScreen.RechargeTxnScreen.route,
            content = {
                RechargeResultScreen(it)
            }
        )
        composable(
            route = NavScreen.BillPaymentTxnScreen.route,
            content = {
                BillPaymentResultScreen(it)
            }
        )
        composable(
            route = NavScreen.DMTTxnScreen.route,
            content = {
                DMTResultScreen(it)
            }
        )

        composable(
            route = NavScreen.UPITxnScreen.route,
            content = {
                UPIResultScreen(it)
            }
        )

        composable(
            route = NavScreen.MATMTxnScreen.route,
            content = {
                MatmResultScreen(it)
            }
        )

        composable(
            route = NavScreen.FundMethodScreen.route,
            content = { FundMethodScreen() }
        )

        composable(
            route = NavScreen.FundBankListScreen.route,
            content = {
                FundBankListScreen()
            }
        )

        composable(
            route = NavScreen.FundRequestScreen.route,
            content = {
                FundRequestScreen()
            }
        )
        composable(
            route = NavScreen.PermissionScreen.route,
            content = {
                PermissionScreen()
            }
        )
        composable(route = NavScreen.ShowQRScreen.route, content = { ShowQRCodeScreen() })
        composable(route = NavScreen.AepsScreen.route, content = { AepsScreen() })
        composable(route = NavScreen.MatmScreen.route, content = { MatmScreen() })
        composable(route = NavScreen.DmtSenderSearchScreen.route, content = {
            SearchSenderScreen(it)
        })

        composable(route = NavScreen.DmtBeneficiaryListInfoScreen.route, content = {
            BeneficiaryListInfoScreen(it)
        })
        composable(route = NavScreen.DmtBeneficiaryRegisterScreen.route, content = {
            BeneficiaryRegisterScreen()
        })

        composable(route = NavScreen.UpiBeneficiaryRegisterScreen.route, content = {
            UpiBeneficiaryRegisterScreen(it)
        })

        composable(route = NavScreen.DmtSenderRegisterScreen.route, content = {
            SenderRegisterScreen()
        })

        composable(route = NavScreen.DMTMoneyTransferScreen.route, content = {
            DMTMoneyTransferScreen()
        })



        composable(route = NavScreen.QRScanScreen.route, content = {
            AppQRScanScreen()
        })


        composable(route = NavScreen.R2RTransferScreen.route, content = {
            R2RTransferScreen()
        })


        composable(route = NavScreen.ChangePasswordScreen.route, content = {
            ChangePasswordScreen()
        })


        composable(route = NavScreen.ChangePinScreen.route, content = {
            ChangePinScreen()
        })


        composable(route = NavScreen.SettlementBankScreen.route, content = {
            SettlementBankScreen(it)
        })

        composable(route = NavScreen.SettlementBankAddScreen.route, content = {
            SettlementAddBankScreen()
        })

        composable(route = NavScreen.SettlementTransferScreen.route, content = {
            SettlementTransferScreen()
        })



        composable(route = NavScreen.TestScreen.route, content = { TestScreen() })
    }
}