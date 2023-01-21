package com.a2z.app.ui.screen.fund.payment_return

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.fund.PaymentReturnDetailResponse
import com.a2z.app.data.repository.FundRepository
import com.a2z.app.data.repository.TransactionRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.dialog.StatusDialog
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.ui.util.resource.StatusDialogType
import com.a2z.app.util.AppUtil
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentPaymentReturnViewModel @Inject constructor(
    private val repository: FundRepository,
    private val transactionRepository: TransactionRepository,
) : BaseViewModel() {

    val input = FormInput()
    val mpinDialogVisibleState = mutableStateOf(false)
    val confirmDialogVisibleState = mutableStateOf(false)
    val detailResultFlow = resultStateFlow<PaymentReturnDetailResponse>()
    private val _transactionResultFlow = resultShareFlow<AppResponse>()

    var encReceiverId = ""

    init {
        fetchDetail()

        viewModelScope.launch {
            _transactionResultFlow.getLatest(
                progress = {
                    transactionProgressDialog()
                }
            ) {
                if (it.status == 1) successDialog(it.message) {
                    navigateTo(NavScreen.DashboardScreen.route,true)
                }
                else alertDialog(it.message)
            }
        }
    }

    private fun fetchDetail() {
        callApiForStateFlow(
            flow = detailResultFlow,
            call = { repository.fetchParentPaymentReturnDetail() },

            )
    }

    fun onSubmit() {
        confirmDialogVisibleState.value = true
    }

    fun onPinSubmit(mpin: String) {
        val param = hashMapOf(
            "amount" to input.amount.getValue(),
            "remark" to input.remark.getValue(),
            "txn_pin" to mpin,
            "encReceiverId" to encReceiverId,
        )
        callApiForShareFlow(
            flow =_transactionResultFlow,
            call = { transactionRepository.parentPaymentFundReturn(param) }
        )
    }

    data class FormInput(
        val amount: InputWrapper = InputWrapper {
            AppValidator.amountValidation(
                minAmount = 2.0,
                maxAmount = 1000000.0,
                inputAmount = it
            )
        },
        val remark: InputWrapper = InputWrapper { AppValidator.empty(it) }
    ) : BaseInput(amount, remark)

}