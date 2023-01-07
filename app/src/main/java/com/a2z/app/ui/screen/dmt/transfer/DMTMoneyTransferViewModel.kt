package com.a2z.app.ui.screen.dmt.transfer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.dmt.BankDownCheckResponse
import com.a2z.app.data.model.dmt.DmtCommissionResponse
import com.a2z.app.data.repository.DMTRepository
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
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    private val args: MoneyTransferArgs = savedStateHandle.safeParcelable("moneyTransferArgs")!!
    val moneySender = args.moneySender
    val beneficiary = args.beneficiary
    val dmtType = args.dmtType

    val input = MoneyTransferInput()

    val transactionType = mutableStateOf(MoneyTransactionType.IMPS)
    private val _bankDownResultFlow = resultShareFlow<BankDownCheckResponse>()
    private val _chargeResultFlow = resultShareFlow<DmtCommissionResponse>()

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
                if(it.status == 1){
                    successBanner(
                        title = "Commission/Charge Fetched",
                        message = it.message
                    )
                }
                else failureDialog(it.message)
            }
        }
    }


    private fun fetchBankDown() {
        val param = hashMapOf(
            "bank_name" to beneficiary.bankName.orEmpty(),
        )
        callApiForShareFlow(
            flow = _bankDownResultFlow,
            call = { repository.bankDownCheck(param) }
        )
    }
    fun fetchCharge(mpin: String) {
        val param = hashMapOf(
            "amount" to input.amount.getValue(),
            "txnChargeApiName" to "PAYTM",
            "txn_pin" to mpin,
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

}

enum class MoneyTransactionType {
    IMPS, NEFT
}

data class MoneyTransferInput(
    val amount: InputWrapper = InputWrapper { AppValidator.amountValidation(it) }
) : BaseInput(amount)