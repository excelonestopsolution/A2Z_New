package com.a2z.app.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.a2z.app.MainViewModel
import com.a2z.app.ui.screen.kyc.document.DocumentKycScreen
import com.a2z.app.ui.component.AppQRScanScreen
import com.a2z.app.ui.screen.aeps.AepsScreen
import com.a2z.app.ui.screen.auth.change.password.ChangePasswordScreen
import com.a2z.app.ui.screen.auth.change.pin.ChangePinScreen
import com.a2z.app.ui.screen.auth.forget.login_id.ForgotLoginIdScreen
import com.a2z.app.ui.screen.auth.forget.password.ForgotPasswordScreen
import com.a2z.app.ui.screen.auth.login.LoginScreen
import com.a2z.app.ui.screen.auth.registration.register.UserRegistrationScreen
import com.a2z.app.ui.screen.auth.registration.register_type.RegistrationTypeScreen
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
import com.a2z.app.ui.screen.fund.payment_gateway.PaymentGatewayScreen
import com.a2z.app.ui.screen.fund.payment_return.ParentPaymentReturnScreen
import com.a2z.app.ui.screen.fund.request.FundRequestScreen
import com.a2z.app.ui.screen.fund.upi_payment.UpiPaymentScreen
import com.a2z.app.ui.screen.kyc.aadhaar.AadhaarKycScreen
import com.a2z.app.ui.screen.kyc.aeps.AEPSKycScreen
import com.a2z.app.ui.screen.matm.MatmScreen
import com.a2z.app.ui.screen.members.list.MemberListScreen
import com.a2z.app.ui.screen.profile.ProfileScreen
import com.a2z.app.ui.screen.util.permission.PermissionScreen
import com.a2z.app.ui.screen.qrcode.ShowQRCodeScreen
import com.a2z.app.ui.screen.r2r.R2RTransferScreen
import com.a2z.app.ui.screen.report.account_statement.AccountStatementReportScreen
import com.a2z.app.ui.screen.report.aeps.AEPSReportScreen
import com.a2z.app.ui.screen.report.dt.DTReportScreen
import com.a2z.app.ui.screen.report.fund.FundReportScreen
import com.a2z.app.ui.screen.report.fund_transfer.FundTransferReportReportScreen
import com.a2z.app.ui.screen.report.matm.MATMReportScreen
import com.a2z.app.ui.screen.report.payment.PaymentReportScreen
import com.a2z.app.ui.screen.report.pg.PGReportScreen
import com.a2z.app.ui.screen.result.*
import com.a2z.app.ui.screen.settlement.add_bank.SettlementAddBankScreen
import com.a2z.app.ui.screen.settlement.bank_list.SettlementBankScreen
import com.a2z.app.ui.screen.settlement.transfer.SettlementTransferScreen
import com.a2z.app.ui.screen.util.DeviceLockScreen
import com.a2z.app.ui.screen.util.agreement.UserAgreementScreen
import com.a2z.app.ui.screen.util.commission.MyCommissionScreen
import com.a2z.app.ui.screen.util.complaint.ComplaintScreen
import com.a2z.app.ui.screen.util.device_order.DeviceOrderTabScreen
import com.a2z.app.ui.screen.util.pan_service.PanServiceScreen
import com.a2z.app.ui.screen.util.scheme_detail.SchemeDetailScreen
import com.a2z.app.ui.screen.utility.bill.BillPaymentScreen
import com.a2z.app.ui.screen.utility.operator.OperatorScreen
import com.a2z.app.ui.screen.utility.recharge.RechargeScreen
import com.a2z.app.ui.theme.LocalNavController


@Composable
fun MainNav(viewModel: MainViewModel) {


    NavHost(
        navController = LocalNavController.current,
        startDestination = NavScreen.LoginScreen.route,
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
            route = NavScreen.AEPSTxnScreen.route,
            content = {
                AEPSResultScreen(it)
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
            route = NavScreen.UpiPaymentScreen.route,
            content = { UpiPaymentScreen() }
        )
        composable(
            route = NavScreen.PaymentGatewayScreen.route,
            content = { PaymentGatewayScreen(viewModel) }
        )
        composable(
            route = NavScreen.ParentPaymentReturnScreen.route,
            content = { ParentPaymentReturnScreen() }
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



        composable(route = NavScreen.DocumentKycScreen.route, content = { DocumentKycScreen() })
        composable(route = NavScreen.AEPSKycScreen.route, content = { AEPSKycScreen() })
        composable(route = NavScreen.AadhaarKycScreen.route, content = { AadhaarKycScreen() })
        composable(route = NavScreen.CommissionScreen.route, content = { MyCommissionScreen() })
        composable(route = NavScreen.SchemeDetailScreen.route, content = { SchemeDetailScreen() })
        composable(route = NavScreen.PanServiceScreen.route, content = { PanServiceScreen() })
        composable(route = NavScreen.DeviceOrderScreen.route, content = { DeviceOrderTabScreen() })
        composable(route = NavScreen.UserAgreementScreen.route, content = { UserAgreementScreen() })
        composable(
            route = NavScreen.UserRegistrationScreen.route,
            content = { UserRegistrationScreen() })
        composable(
            route = NavScreen.RegistrationTypeScreen.route,
            content = { RegistrationTypeScreen() })
        composable(
            route = NavScreen.ForgotPasswordScreen.route,
            content = { ForgotPasswordScreen() })
        composable(route = NavScreen.ForgotLoginIdScreen.route, content = { ForgotLoginIdScreen() })
        composable(route = NavScreen.ComplaintScreen.route, content = { ComplaintScreen() })
        composable(
            route = NavScreen.MATMRequestReportScreen.route,
            content = { MATMReportScreen() })
        composable(route = NavScreen.AEPSReportScreen.route, content = { AEPSReportScreen() })
        composable(route = NavScreen.FundReportScreen.route, content = { FundReportScreen() })
        composable(route = NavScreen.DTReportScreen.route, content = { DTReportScreen() })
        composable(route = NavScreen.PGReportScreen.route, content = { PGReportScreen() })
        composable(
            route = NavScreen.FundTransferReportScreen.route,
            content = { FundTransferReportReportScreen() })
        composable(
            route = NavScreen.PaymentReportScreen.route,
            content = { PaymentReportScreen() })
        composable(
            route = NavScreen.AccountStatementReport.route,
            content = { AccountStatementReportScreen() })
        composable(route = NavScreen.ProfileScreen.route, content = { ProfileScreen() })
        composable(route = NavScreen.MemberListScreen.route, content = { MemberListScreen() })
    }
}