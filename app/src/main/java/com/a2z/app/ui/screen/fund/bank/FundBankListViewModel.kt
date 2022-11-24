package com.a2z.app.ui.screen.fund.bank

import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.model.fund.FundMethod
import com.a2z.app.data.model.fund.FundRequestBankListResponse
import com.a2z.app.data.repository.FundRepository
import com.a2z.app.ui.screen.fund.method.FundMethodType
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForFlow
import com.a2z.app.ui.util.extension.safeParcelable
import com.a2z.app.ui.util.resource.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FundBankListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: FundRepository
) : BaseViewModel() {
    var fundMethod  : FundMethod = savedStateHandle.safeParcelable("fundMethod")!!

    private val _bankListFlow =
        MutableStateFlow<ResultType<FundRequestBankListResponse>>(ResultType.Loading())
    val bankListFlow: StateFlow<ResultType<FundRequestBankListResponse>> = _bankListFlow

    init {
        fetchBankList()
    }

    private fun fetchBankList() {
        val requestType = when (fundMethod.type) {
            FundMethodType.BANK_TRANSFER -> "BT"
            FundMethodType.CASH_IN_CDM -> "CASH_CDM"
            FundMethodType.CASH_DEPOSIT -> "CASH_DEPOSIT"
            FundMethodType.CASH_COLLECT -> "CASH_COLLECT"
            else -> ""
        }
        callApiForFlow(_bankListFlow) { repository.fetchFundBankList(requestType) }
    }


    private val noteCashCollect = listOf(
        "* Balance is updated daily (10AM - 9PM) within 1 hour upon realisation of funds in our bank account.",
        "* 100% penalty would be debited for the value of soiled or fake currency deposited to our account.",
        "* Please Note: Excel One Stop Solution Pvt ltd will not be liable if payment is made with wrong details or to the wrong account, please double check all the details before making a cash deposit.",

        )
    private val noteBankTransfer = listOf(
        "* No Charges to load money using this mode.",
        "* Payee Name: Excel One Stop Solution Pvt ltd",
        "* If you are transferring funds from ICICI Bank to our ICICI Bank account then go to Transfers > Pay to Virtual Account option.",
        "* Balance is updated daily within 30 min upon realisation of funds in our bank account.",
        "* Note: Changing your registered mobile number with Excel One Stop Solution Pvt ltd will change your deposit bank account number.",

        )
    private val noteCdm = listOf(
        "* 100% penalty would be debited for the value of soiled or fake currency deposited to our account.",
        "* Please Note: Excel One Stop Solution Pvt ltd will not be liable if payment is made with wrong details or to the wrong account, please double check all the details before making a cash deposit."
    )
    private val noteCashDeposit = listOf(
        "* Balance is updated daily (10AM - 9PM) within 1 hour upon realisation of funds in our bank account.",
        "* 100% penalty would be debited for the value of soiled or fake currency deposited to our account.",
        "* Please Note: Excel One Stop Solution Pvt ltd will not be liable if payment is made with wrong details or to the wrong account, please double check all the details before making a cash deposit."
    )

    val notes = when (fundMethod.type) {
        FundMethodType.BANK_TRANSFER -> noteBankTransfer
        FundMethodType.CASH_IN_CDM ->noteCdm
        FundMethodType.CASH_DEPOSIT -> noteCashDeposit
        FundMethodType.CASH_COLLECT -> noteCashCollect
        else ->null
    }


}