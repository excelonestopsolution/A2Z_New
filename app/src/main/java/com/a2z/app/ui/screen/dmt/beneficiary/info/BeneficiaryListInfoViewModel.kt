package com.a2z.app.ui.screen.dmt.beneficiary.info

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.model.dmt.Beneficiary
import com.a2z.app.data.model.dmt.BeneficiaryListResponse
import com.a2z.app.data.model.dmt.MoneySender
import com.a2z.app.data.repository.DMTRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.ui.util.extension.safeParcelable
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class BeneficiaryListInfoViewModel @Inject constructor(
    private val repository: DMTRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    lateinit var mainBeneficiaryList: ArrayList<Beneficiary>
    val  showBeneficiaryList = mutableStateListOf<Beneficiary>()

    val moneySender: MoneySender = savedStateHandle.safeParcelable("moneySender")!!

    private val _beneficiaryFlow = resultStateFlow<BeneficiaryListResponse>()
    val beneficiaryFlow = _beneficiaryFlow.asStateFlow()

    init {
        fetchBeneficiary()
    }

    private fun fetchBeneficiary(){
        val param = hashMapOf(
            "mobile_number" to moneySender.mobileNumber.orEmpty(),
            "mobile" to moneySender.mobileNumber.orEmpty()
        )
        callApiForStateFlow(
            flow = _beneficiaryFlow,
            call = {repository.beneficiaryList(param)}
        )
    }

}