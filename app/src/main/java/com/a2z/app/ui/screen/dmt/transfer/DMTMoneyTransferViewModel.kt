package com.a2z.app.ui.screen.dmt.transfer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.dmt.BankDownCheckResponse
import com.a2z.app.data.model.dmt.DmtCommissionResponse
import com.a2z.app.data.model.dmt.TransactionDetail
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.repository.DMTRepository
import com.a2z.app.data.repository.TransactionRepository
import com.a2z.app.data.repository.UpiRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.screen.dmt.util.DMTType
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.safeParcelable
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DMTMoneyTransferViewModel @Inject constructor(
    private val repository: DMTRepository,
    private val upiRepository: UpiRepository,
    private val transactionRepository: TransactionRepository,
    private val appPreference: AppPreference,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    private val args: MoneyTransferArgs = savedStateHandle.safeParcelable("moneyTransferArgs")!!
    val moneySender = args.moneySender
    val beneficiary = args.beneficiary
    val dmtType = args.dmtType

    var mpinType = mutableStateOf(MoneyTransferMPinType.COMMISSION)

    val input = MoneyTransferInput()

    var mpin: String? = null

    val transactionType = mutableStateOf(MoneyTransactionType.IMPS)
    private val _bankDownResultFlow = resultShareFlow<BankDownCheckResponse>()
    private val _chargeResultFlow = resultShareFlow<DmtCommissionResponse>()


    val chargeState = mutableStateOf<DmtCommissionResponse?>(null)
    val confirmDialogState = mutableStateOf(false)
    val mpinDialogVisibleState = mutableStateOf(false)
    private val _walletTransactionResultFlow = resultShareFlow<TransactionDetailResponse>()
    private val _upiTransactionResultFlow = resultShareFlow<TransactionDetail>()

    var channel: String = "2"

    val bankDownText = mutableStateOf("")
    var impsAllow = true

    init {
        fetchBankDown()
        viewModelScope.launch {
            _bankDownResultFlow.getLatest {

                if (it.status == 1) {

                    val bankDown = it.bankDownCheck
                    if (bankDown?.isTxnAllowed == true) {

                        if (bankDown.isImpsAllowed == true) {
                            transactionType.value = MoneyTransactionType.IMPS
                            channel = "2"
                        } else {
                            impsAllow = false
                            channel = "1"
                            transactionType.value = MoneyTransactionType.NEFT
                            bankDownText.value = bankDown.message ?: ""

                        }
                    }

                } else if (it.status == 404 && beneficiary.bankName == "BANK UPI") {
                } else {
                    failureDialog(it.message) {
                        navigateUpWithResult()
                    }
                }

            }
        }

        viewModelScope.launch {
            _chargeResultFlow.getLatest {
                if (it.status == 1) {
                    chargeState.value = it

                    successBanner(
                        title = "Commission/Charge Fetched",
                        message = it.message
                    )
                } else {
                    chargeState.value = null
                    failureDialog(it.message)
                }
            }
        }

        viewModelScope.launch {
            _walletTransactionResultFlow.getLatest(
                success = {
                    if (it.status == 1) {
                        if (
                            it.data?.status == 1 ||
                            it.data?.status == 2 ||
                            it.data?.status == 3 ||
                            it.data?.status == 34 ||
                            it.data?.status == 37
                        ) {
                            navigateTo(NavScreen.DMTTxnScreen.passArgs(it.data!!))
                        } else alertDialog(it.message.toString())
                    } else alertDialog(it.message.toString())
                },
                failure = {
                    pendingDialog("Transaction is pending, please check report history") {
                        gotoMainDashboard()
                    }
                },
                progress = {
                    transactionProgressDialog()
                })
        }
        viewModelScope.launch {
            _upiTransactionResultFlow.getLatest(
                success = {
                    if (
                        it.status == 1 || it.status == 2 || it.status == 3 ||
                        it.status == 34 || it.status == 37
                    ) {
                        navigateTo(NavScreen.UPITxnScreen.passArgs(it))
                    } else alertDialog(it.message.toString())
                },
                failure = {
                    pendingDialog("Transaction is pending, please check report history") {
                        gotoMainDashboard()
                    }
                },
                progress = {
                    transactionProgressDialog()
                })
        }
    }


    private fun fetchBankDown() {
        val param = hashMapOf(
            "bank_name" to beneficiary.bankName.orEmpty(),
        )
        val paramUpi = hashMapOf(
            "upiId" to beneficiary.accountNumber.orEmpty(),
        )

        suspend fun callApi() = when (dmtType) {
            DMTType.UPI -> upiRepository.bankDownCheck(paramUpi)
            else -> repository.bankDownCheck(param)
        }

        callApiForShareFlow(
            flow = _bankDownResultFlow,
            call = { callApi() }
        )
    }

    fun fetchCharge() {
        val param = hashMapOf(
            "amount" to input.amount.getValue(),
            "txnChargeApiName" to "PAYTM",
            "txn_pin" to mpin.orEmpty(),
        )
        callApiForShareFlow(
            flow = _chargeResultFlow,
            call = { repository.commissionCharge(param) }
        )
    }

    fun onTransactionChange(type: MoneyTransactionType) {
        if (!impsAllow) return
        transactionType.value = type

    }

    fun proceedTransaction() {
        val walletParam = hashMapOf(
            "beneName" to beneficiary.name.orEmpty(),
            "bank_account" to beneficiary.accountNumber.orEmpty(),
            "account_number" to beneficiary.accountNumber.orEmpty(),//dmt3
            "mobile_number" to moneySender.mobileNumber.orEmpty(),
            "mobile" to moneySender.mobileNumber.orEmpty(),//dmt3
            "sender_name" to moneySender.firstName.orEmpty() + " " + moneySender.lastName.toString(),//dmt3
            "amount" to input.amount.getValue(),
            "channel" to channel,
            "txn_pin" to mpin.orEmpty(),
            "a2z_bene_id" to beneficiary.a2zBeneId.orEmpty(),
            "beneficiary_id" to beneficiary.a2zBeneId.orEmpty(),//dmt3
            "latitude" to appPreference.latitude,
            "longitude" to appPreference.longitude,
        )

        val upiParam = hashMapOf(
            "amount" to input.amount.getValue(),
            "bene_id" to beneficiary.id.orEmpty(),
            "sender_number" to moneySender?.mobileNumber.orEmpty(),
            "latitude" to appPreference.latitude,
            "longitude" to appPreference.longitude,
            "txn_pin" to mpin.orEmpty(),
        )

        suspend fun callApi() = when (dmtType) {
            DMTType.WALLET_1 -> transactionRepository.wallet1Transaction(walletParam)
            DMTType.WALLET_2 -> transactionRepository.wallet2Transaction(walletParam)
            DMTType.WALLET_3 -> transactionRepository.wallet3Transaction(walletParam)
            DMTType.DMT_3 -> transactionRepository.dmt3Transaction(walletParam)
            DMTType.UPI -> throw Exception("Unsupported dmt type")
        }

        if (dmtType == DMTType.UPI)
            callApiForShareFlow(
                flow = _upiTransactionResultFlow,
                call = { transactionRepository.upiTransaction(upiParam) },
                handleException = false,
                popUpScreen = false
            )
        else callApiForShareFlow(
            flow = _walletTransactionResultFlow,
            call = { callApi() },
            handleException = false,
            popUpScreen = false
        )
    }

}

enum class MoneyTransferMPinType {
    COMMISSION, TRANSFER
}

enum class MoneyTransactionType {
    IMPS, NEFT
}

data class MoneyTransferInput(
    val amount: InputWrapper = InputWrapper { AppValidator.amountValidation(it, minAmount = 2.0) }
) : BaseInput(amount)