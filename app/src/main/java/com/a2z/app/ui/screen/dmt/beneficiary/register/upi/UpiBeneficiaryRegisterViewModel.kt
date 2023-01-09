package com.a2z.app.ui.screen.dmt.beneficiary.register.upi

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.a2z.app.data.model.dmt.UpiBank
import com.a2z.app.data.model.dmt.UpiExtension
import com.a2z.app.data.model.dmt.VpaBankExtensionResponse
import com.a2z.app.data.repository.UpiRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpiBeneficiaryRegisterViewModel
@Inject constructor(
    private val upiRepository: UpiRepository
) : BaseViewModel() {

    val vpaListResultFlow = resultStateFlow<VpaBankExtensionResponse>()

    val selectedBankState = mutableStateOf<UpiBank?>(null)

    val upiExtension = mutableStateListOf<UpiExtension>()
    init {
        fetchVpaAccounts()
    }

    private fun fetchVpaAccounts() {
        callApiForStateFlow(
            flow = vpaListResultFlow,
            call = { upiRepository.vpaList() },
            beforeEmit = {
                if(it is ResultType.Success){
                    it.data.upiBank?.let {
                       selectedBankState.value =  it.find { it.name == "Bank Upi" }
                        upiExtension.clear()
                        upiExtension.addAll(selectedBankState.value?.upiextensions!!)
                    }
                }
            }
        )
    }

    fun onSelectBank(it: UpiBank) {
        selectedBankState.value = it
        upiExtension.clear()
        upiExtension.addAll(selectedBankState.value?.upiextensions!!)

    }

    fun onSelectExtension(it: UpiExtension) {


    }

}