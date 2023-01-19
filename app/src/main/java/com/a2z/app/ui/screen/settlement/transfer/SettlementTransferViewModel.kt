package com.a2z.app.ui.screen.settlement.transfer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.settlement.SettlementAddedBank
import com.a2z.app.data.repository.TransactionRepository
import com.a2z.app.nav.NavScreen
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
class SettlementTransferViewModel @Inject constructor(
    private val repository: TransactionRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    val bank: SettlementAddedBank = savedStateHandle.safeParcelable("bank")!!

    val input = SettlementTransferInput()

    val confirmDialogState = mutableStateOf(false)

    private val _transferResultFlow = resultShareFlow<AppResponse>()

    init {
        viewModelScope.launch {
            _transferResultFlow.getLatest(
                progress = { transactionProgressDialog() },
                failure = { pendingDialog("Transaction in Pending! please check report.") },
                success = {
                    if (it.status == 1) successDialog(it.message) {
                        navigateTo(NavScreen.DashboardScreen.route, true)
                    } else failureDialog(it.message)
                }
            )
        }
    }

    fun transfer() {

        val param = hashMapOf(
            "id" to bank.id.toString(),
            "beneName" to bank.name.toString(),
            "ifsc" to bank.ifscCode.toString(),
            "bank_account" to bank.accountNumber.toString(),
            "amount" to input.amount.getValue(),
            "channel" to "2",
        )
        callApiForShareFlow(
            flow = _transferResultFlow,
            call = { repository.settlementTransfer(param) }
        )

    }

}

data class SettlementTransferInput(
    val amount: InputWrapper = InputWrapper { AppValidator.amountValidation(it) }
) : BaseInput(amount)