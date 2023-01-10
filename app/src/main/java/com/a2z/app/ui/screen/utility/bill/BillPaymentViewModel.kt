package com.a2z.app.ui.screen.utility.bill

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.provider.Operator
import com.a2z.app.data.model.utility.BillFetchInfo
import com.a2z.app.data.model.utility.BillFetchInfoResponse
import com.a2z.app.data.model.utility.BillPaymentResponse
import com.a2z.app.data.repository.TransactionRepository
import com.a2z.app.data.repository.UtilityRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.screen.utility.util.BillPaymentAction
import com.a2z.app.ui.screen.utility.util.BillPaymentUtil
import com.a2z.app.ui.screen.utility.util.OperatorType
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.safeParcelable
import com.a2z.app.ui.util.extension.safeSerializable
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.AppUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillPaymentViewModel @Inject constructor(
    private val repository: UtilityRepository,
    private val transactionRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle,
    appPreference: AppPreference
) : BaseViewModel() {

    val location: Location? = null
    private val operatorType: OperatorType = savedStateHandle.safeSerializable("operatorType")!!
    val operator: Operator = savedStateHandle.safeParcelable("operator")!!
    val util = BillPaymentUtil(operator, appPreference, operatorType)

    val spinnerDialogState = mutableStateOf(false)
    val selectedState = mutableStateOf("BBPS Provider 1")
    val confirmDialogState = mutableStateOf(false)


    private val billPaymentResponse = MutableSharedFlow<ResultType<BillPaymentResponse>>()
    private val billFetchResponse = MutableSharedFlow<ResultType<BillFetchInfoResponse>>()

    init {
        viewModelScope.launch { billPaymentResult() }
        viewModelScope.launch { fetchBillResult() }
    }

    private suspend fun fetchBillResult() {
        billFetchResponse.getLatest {

            AppUtil.logger("hello dev : bill fet")

            val status = it.status
            val message = it.message
            val info = it.info

            if (status == 1) {
                util.fetchBillInfo.value = false
                util.useAmountValidation.value = true
                util.useMobileValidation.value = false
                util.useDobValidation.value = false
                util.useEmailValidation.value = false
                billInfo.value = info
                isAmountReadyOnly.value = it.isAmountEditable == 0
                input.amountInputWrapper.setValue(it.info?.billAmount)

                successBanner("Bill Fetch Result", message)

            } else alertBanner("Bill Fetch Result", message)

        }
    }

    private suspend fun billPaymentResult() {
        billPaymentResponse.getLatest(
            progress = {transactionProgressDialog()}
        ) {
            val status: Int = it.status
            if (status == 1 || status == 3 || status == 24 || status == 34) {
                navigateTo(NavScreen.BillPaymentTxnScreen.passArgs(
                    response = it.apply {
                        this.numberTitle = operator.dealerName
                        this.number = input.numberInputWrapper.getValue()
                        this.serviceName = util.getOperatorTitle()
                        this.providerIcon = util.getIconFromOperatorType()
                    }
                ))
            } else alertDialog(it.message) { gotoMainDashboard() }
        }
    }

    val input = BillPaymentInput(
        useMobileValidation = util.useMobileValidation,
        useAmountValidation = util.useAmountValidation,
        useEmailValidation = util.useEmailValidation,
        useDobValidation = util.useDobValidation,
        inputValidator = { util.numberValidator(it) },
        amountValidator = { util.amountValidator(it) }
    )

    init {

    }

    val isAmountReadyOnly = mutableStateOf(true)
    val billInfo = mutableStateOf<BillFetchInfo?>(null)

    fun onButtonClick() {
        when (util.actionType) {
            BillPaymentAction.FETCH_BILL_INFO -> {
                fetchBillInfo()
            }
            BillPaymentAction.PROCEED_TO_PAYMENT -> {
                confirmDialogState.value = true
            }
        }
    }

    fun makePayment() {
        fun param() = hashMapOf(
            "context" to (billInfo.value?.context ?: ""),
            "number" to input.numberInputWrapper.getValue(),
            "provider" to operator.id.toString(),
            "amount" to input.amountInputWrapper.getValue(),
            "CustomerName" to (billInfo.value?.customerName ?: ""),
            "bill_due_date" to (billInfo.value?.dueDate ?: ""),
            "customerMobileNumber" to input.mobileInputWrapper.getValue(),
            "latitude" to location?.latitude.toString(),
            "longitude" to location?.longitude.toString(),
        )

        callApiForShareFlow(flow = billPaymentResponse) {
            if (selectedState.value == "BBPS Provider 1")
                transactionRepository.billPaymentRouteOne(param())
            else transactionRepository.billPaymentRouteTwo(param())
        }
    }

    private fun fetchBillInfo() {
        fun param() = listOf(
            "number" to input.numberInputWrapper.getValue(),
            "dob" to input.dobInputWrapper.getValue(),
            "customerMobileNumber" to input.mobileInputWrapper.getValue(),
            "provider" to operator.id.toString()
        ).toTypedArray()

        callApiForShareFlow(
            flow = billFetchResponse,
            call = { repository.fetchBillInfo(*param()) }
        )
    }

    fun showCard(): Boolean {
        return util.actionType == BillPaymentAction.PROCEED_TO_PAYMENT
    }

}

