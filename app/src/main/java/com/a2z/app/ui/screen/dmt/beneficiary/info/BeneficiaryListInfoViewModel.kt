package com.a2z.app.ui.screen.dmt.beneficiary.info

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.*
import com.a2z.app.data.repository.DMTRepository
import com.a2z.app.ui.screen.dmt.util.DMTType
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.ui.util.extension.safeParcelable
import com.a2z.app.ui.util.extension.safeSerializable
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.AppUtil
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BeneficiaryListInfoViewModel @Inject constructor(
    private val repository: DMTRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val confirmDeleteDialogState = mutableStateOf(false)
    val confirmVerifyDialogState = mutableStateOf(false)
    val otpVerifyDialogState = mutableStateOf(false)
    val beneficiaryState = mutableStateOf<Beneficiary?>(null)

    lateinit var mainBeneficiaryList: ArrayList<Beneficiary>
    val showBeneficiaryList = mutableStateListOf<Beneficiary>()

    val moneySender: MoneySender = savedStateHandle.safeParcelable("moneySender")!!
    val dmtType: DMTType = savedStateHandle.safeSerializable("dmtType")!!
    private val senderAccountDetail: SenderAccountDetail =
        savedStateHandle.safeParcelable("accountDetail")!!

    private val _beneficiaryFlow = resultStateFlow<BeneficiaryListResponse>()
    val beneficiaryFlow = _beneficiaryFlow.asStateFlow()

    private val deleteFlow = resultShareFlow<AppResponse>()
    private val verificationFlow = resultShareFlow<AccountVerify>()
    private val deleteBeneficiaryFlow = resultShareFlow<AppResponse>()
    private val deleteBeneficiaryOtpFlow = resultShareFlow<AppResponse>()
    val swipeState = mutableStateOf(false)

    init {

        fetchBeneficiary()

        viewModelScope.launch {
            deleteBeneficiaryFlow.getLatest {
                if (it.status == 37) otpVerifyDialogState.value = true
                else alertDialog(it.message)
            }
        }

        viewModelScope.launch {
            deleteBeneficiaryOtpFlow.getLatest {
                if (it.status == 38) successDialog(it.message) {
                    fetchBeneficiary()
                }
                else alertDialog(it.message)
            }

        }

        viewModelScope.launch {
            verificationFlow.getLatest {
                if (it.status == 1) successDialog(it.message)
                else alertDialog(it.message)
            }


        }
    }

    fun fetchBeneficiary() {
        if (this::mainBeneficiaryList.isInitialized)
            mainBeneficiaryList.clear()
        showBeneficiaryList.clear()
        beneficiaryState.value = null
        val param = hashMapOf(
            "mobile_number" to moneySender.mobileNumber.orEmpty(),
            "mobile" to moneySender.mobileNumber.orEmpty()
        )
        callApiForStateFlow(
            flow = _beneficiaryFlow,
            call = { repository.beneficiaryList(param) },
            beforeEmit = {
                if (it is ResultType.Success)
                    if (it.data.status == 22) {
                        mainBeneficiaryList = it.data.data!!
                        showBeneficiaryList.addAll(it.data.data!!)

                        if (showBeneficiaryList.isNotEmpty()) {
                            val beneficiary = showBeneficiaryList.find { item->
                                item.bankName == senderAccountDetail.bankName
                                        && item.name == senderAccountDetail.name
                            }
                            if(beneficiary!=null){
                                val index = showBeneficiaryList.indexOf(beneficiary)
                                Collections.swap(showBeneficiaryList,0,index)
                                swipeState.value = true
                            }

                        }


                    }
            }
        )
    }


    fun filterListCondition(it: Beneficiary, query: String): Boolean {
        return it.name!!.toLowerCase().contains(query.toLowerCase())
                || it.bankName!!.toLowerCase().contains(query.toLowerCase())
                || it.accountNumber!!.contains(query.toLowerCase())
    }


    fun onVerify(it: Beneficiary) {
        beneficiaryState.value = it
        confirmVerifyDialogState.value = true

    }

    fun onVerificationConfirm() {
        val param = hashMapOf(
            "bene_id" to beneficiaryState.value?.a2zBeneId.orEmpty(),
        )
        callApiForShareFlow(
            flow = verificationFlow,
            call = { repository.accountReValidation(param) }
        )

    }

    fun onDelete(it: Beneficiary) {
        beneficiaryState.value = it
        confirmDeleteDialogState.value = true
    }


    fun deleteBeneficiaryOtp(otp: String) {

        val param = hashMapOf(
            "bene_id" to beneficiaryState.value?.a2zBeneId.orEmpty(),
            "otp" to otp
        )

        callApiForShareFlow(
            flow = deleteBeneficiaryOtpFlow,
            call = { repository.deleteBeneficiaryConfirm(param) }
        )


    }

    fun deleteBeneficiary() {
        val param = hashMapOf(
            "bene_id" to beneficiaryState.value?.a2zBeneId.orEmpty()
        )
        callApiForShareFlow(
            flow = deleteBeneficiaryFlow,
            call = { repository.deleteBeneficiary(param) }
        )
    }

}
