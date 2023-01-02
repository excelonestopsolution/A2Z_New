package com.a2z.app.ui.screen.aeps

import androidx.compose.runtime.mutableStateOf
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.ui.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AepsViewModel @Inject constructor(
    private val repository: AppRepository
): BaseViewModel() {

    val transactionType = mutableStateOf(AepsTransactionType.CASH_WITHDRAWAL)

    var spinnerDialogState = mutableStateOf(false)
    var selectedBank = mutableStateOf("")

    fun aepsBankList() = arrayListOf("HDFC Bank")

    fun onBankChange(value : String){
        selectedBank.value = value
    }
}

enum class AepsTransactionType{
    CASH_WITHDRAWAL,BALANCE_ENQUIRY, MINI_STATEMENT,AADHAAR_PAY
}