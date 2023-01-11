package com.a2z.app.ui.screen.settlement.bank_list

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.settlement.SettlementAddedBank
import com.a2z.app.data.model.settlement.SettlementAddedBankListResponse
import com.a2z.app.data.repository.AepsRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettlementBankViewModel @Inject constructor(
    private val repository: AepsRepository
) : BaseViewModel() {

    val bankListResultFlow = resultStateFlow<SettlementAddedBankListResponse>()

    init {
        fetchBankList()
    }


    fun fetchBankList() {
        callApiForStateFlow(
            flow = bankListResultFlow,
            call = { repository.fetchSettlementAddedBankList() }
        )
    }

    fun onSend(it: SettlementAddedBank) {
        navigateTo(NavScreen.SettlementTransferScreen.passArgs(it))

    }

    fun onUploadDocument(it: SettlementAddedBank) {


    }

    fun onAddNewBank() {
        navigateTo(NavScreen.SettlementBankAddScreen.route)
    }
}