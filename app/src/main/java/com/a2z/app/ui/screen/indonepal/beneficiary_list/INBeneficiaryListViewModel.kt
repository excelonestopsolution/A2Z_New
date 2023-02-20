package com.a2z.app.ui.screen.indonepal.beneficiary_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.indonepal.INBeneficiary
import com.a2z.app.data.model.indonepal.INBeneficiaryResponse
import com.a2z.app.data.model.indonepal.INSender
import com.a2z.app.data.repository.IndoNepalRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.ui.util.extension.safeParcelable
import com.a2z.app.util.ApiUtil
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class INBeneficiaryListViewModel @Inject constructor(
    private val repository: IndoNepalRepository,
    private val apiUtil : ApiUtil,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val sender = savedStateHandle.safeParcelable<INSender>("sender")!!

    val listResultFlow = resultStateFlow<INBeneficiaryResponse>()

    init {
        fetchBeneficiary()
    }

    private fun fetchBeneficiary(){

        /*apiUtil.fakeApiForStateFlow(
            flow = listResultFlow,
            scope = viewModelScope,
            fileName = "in_beneficiaries"
        )*/

        callApiForStateFlow(listResultFlow) { repository.fetchBeneficiary(
            hashMapOf("senderMobileNumber" to sender.mobile.toString())
        ) }
    }

}