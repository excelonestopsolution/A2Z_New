package com.a2z.app.ui.screen.r2r

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.r2r.R2RSearchRetailer
import com.a2z.app.data.model.r2r.R2RSearchRetailerResponse
import com.a2z.app.data.repository.FundRepository
import com.a2z.app.data.repository.TransactionRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class R2RTransferViewModel @Inject constructor(
    private val repository: FundRepository,
    private val transactionRepository: TransactionRepository,
) : BaseViewModel() {

    val searchType = mutableStateOf(R2RTransferSearchType.MOBILE)

    val numberValidationLength = mutableStateOf(10)
    val amountValidation = mutableStateOf(false)

    val input = R2RTransferInput(numberValidationLength, amountValidation)

    private val _searchRetailerResultFlow = resultShareFlow<R2RSearchRetailerResponse>()
    private val _transferResultFlow = resultShareFlow<AppResponse>()
    val r2rRetailer = mutableStateOf<R2RSearchRetailer?>(null)

    val confirmDialogState = mutableStateOf(false)

    val inputLabel: String
        get() = if (searchType.value == R2RTransferSearchType.MOBILE)
            "Mobile Number" else "Retailer ID"

    init {

        viewModelScope.launch {
            _searchRetailerResultFlow.getLatest {
                if (it.status == 1 && it.hasData) {
                    amountValidation.value = true
                    successBanner("Retailer Found", it.message)
                    r2rRetailer.value = it.data.first()

                } else {
                    r2rRetailer.value = null
                    failureDialog(it.message)
                }
            }
        }

        viewModelScope.launch {
            _transferResultFlow.getLatest(
                progress = { transactionProgressDialog() },
                failure = {
                    failureDialog(it.message.toString()) {
                        navigateTo(NavScreen.DashboardScreen.route, true)
                    }
                },
                success = {
                    successDialog(it.message) {
                        navigateTo(NavScreen.DashboardScreen.route, true)
                    }
                }
            )
        }
    }


    fun onSearchTypeChange(it: R2RTransferSearchType) {


        if (r2rRetailer.value != null) {
            amountValidation.value = false
            r2rRetailer.value = null
        }

        this.searchType.value = it
        numberValidationLength.value =
            if (searchType.value == R2RTransferSearchType.MOBILE) 10 else 8
        if (input.number.getValue().isNotEmpty()) input.number.setValue("")
    }

    fun onSearchClick() {

        if (r2rRetailer.value != null) {
            r2rRetailer.value = null
            amountValidation.value = false
        }

        val searchType = when (searchType.value) {
            R2RTransferSearchType.MOBILE -> "NUMBER"
            R2RTransferSearchType.ID -> "ID"
        }

        callApiForShareFlow(
            flow = _searchRetailerResultFlow,
            call = {
                repository.searchR2RRetailer(
                    searchType = searchType,
                    searchInput = input.number.getValue(),
                )
            }
        )
    }

    fun transfer() {

        var remark = input.remark.getValue()
        if(remark.trim().isEmpty()) remark = "not avaialble"
        val param = hashMapOf(
            "amount" to input.amount.getValue(),
            "dt_scheme" to "0.0",
            "wallet" to "0",
            "remark" to remark,
            "memberId" to r2rRetailer.value!!.memberId.toString()
        )

        callApiForShareFlow(
            flow = _transferResultFlow,
            call = {
                transactionRepository.r2rTransfer(param)
            }
        )
    }

}

data class R2RTransferInput(
    val numberValidationLength: MutableState<Int>,
    val amountValidation: MutableState<Boolean>,
    val number: InputWrapper = InputWrapper {
        if (numberValidationLength.value == 10)
            AppValidator.mobile(it)
        else AppValidator.empty(it)
    },
    val amount: InputWrapper = InputWrapper(amountValidation) {
        AppValidator.amountValidation(it, minAmount = 10.0)
    },

    val remark: InputWrapper = InputWrapper { AppValidator.empty(it) }
) : BaseInput(number, amount)

enum class R2RTransferSearchType {
    MOBILE, ID
}