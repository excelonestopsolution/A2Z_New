package com.a2z.app.ui.screen.settlement.add_bank

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.settlement.SettlementBank
import com.a2z.app.data.model.settlement.SettlementBankListResponse
import com.a2z.app.data.model.settlement.SettlementBankTextInfo
import com.a2z.app.data.repository.AepsRepository
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class SettlementAddBankViewModel @Inject constructor(
    private val repository: AepsRepository
) : BaseViewModel() {


    lateinit var bankList: ArrayList<SettlementBank>
    val input = SettlementAddBankInput()
    val bankListResultFlow = resultStateFlow<SettlementBankListResponse>()
    val spinnerDialogState = mutableStateOf(false)
    val selectedBank = mutableStateOf<SettlementBank?>(null)
    private val _bankAddResultFlow = resultShareFlow<AppResponse>()

    init {
        fetchBankList()

        viewModelScope.launch {
            _bankAddResultFlow.getLatest {
                if(it.status == 1)  successDialog(it.message){
                    navigateUpWithResult("bank_added" to true)
                }
                else failureDialog(it.message)
            }
        }
    }

    private fun fetchBankList() {

        callApiForStateFlow(
            flow = bankListResultFlow,
            call = { repository.fetchSettlementBank() }
        )
    }

    fun addBank() {
        val param = hashMapOf(
            "account_number" to input.accountNumber.getValue(),
            "confirm_account_number" to input.confirmAccount.getValue(),
            "ifsc" to input.ifscCode.getValue(),
            "bank_id" to selectedBank.value!!.id.toString(),
        )
        callApiForShareFlow(
            flow = _bankAddResultFlow,
            call = { repository.addSettlementBank(param) }
        )
    }

    fun parseToHTMLTextString(it: SettlementBankTextInfo): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(" * ")
        stringBuilder.append(it.lineOne.first)
        stringBuilder.append(" ${it.lineOne.username}")
        stringBuilder.append(" ${it.lineOne.join} ")
        stringBuilder.append(it.lineOne.shopName)
        stringBuilder.append("\n * ${it.lineTwo}")
        val mText =
            " * ${it.lineOne.first} <b>${it.lineOne.username}</b> ${it.lineOne.join} <b>${it.lineOne.shopName}</b> <br>* ${it.lineTwo}"

        return mText
    }

    fun onBankSelect(bankName: String) {
        selectedBank.value = bankList.find { it.bankName == bankName }
        val ifsc = selectedBank.value?.ifscCode
        if (ifsc != null && ifsc.trim().isNotEmpty()) {
            input.ifscCode.setValue(ifsc)
        }
    }

}

data class SettlementAddBankInput(
    val ifscCode: InputWrapper = InputWrapper { AppValidator.ifscCode(it) },
    val accountNumber: InputWrapper = InputWrapper { AppValidator.accountNumberValidation(it) },
    val confirmAccount: InputWrapper = InputWrapper {
        AppValidator.confirmAccountNumber(
            it, accountNumber.getValue()
        )
    },
) : BaseInput(ifscCode, accountNumber, confirmAccount)