package com.a2z.app.ui.screen.indonepal.beneficiary_add

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.indonepal.INBranch
import com.a2z.app.data.model.indonepal.INBranchResponse
import com.a2z.app.data.model.indonepal.INStaticData
import com.a2z.app.data.repository.IndoNepalRepository
import com.a2z.app.ui.screen.indonepal.INUtil
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class INBeneficiaryAddViewModel @Inject constructor(
    private val repository: IndoNepalRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    var staticData: INStaticData = INUtil.staticData()
    private val customerId = savedStateHandle.get<String>("customerId")


    val validateAccountNumber = mutableStateOf(false)
    val input = InputForm(validateAccountNumber)

    val genderState = mutableStateOf("")
    val relationShipState = mutableStateOf("")
    val paymentModeState = mutableStateOf("Cash Payment")

    val bankState = mutableStateOf("")
    val branchState = mutableStateOf("")

    val bankDialogState = mutableStateOf(false)
    val branchDialog = mutableStateOf(false)

    val branchList = mutableStateListOf<INBranch>()


    private val _branchListResultFlow = resultShareFlow<INBranchResponse>()
    private val _registerResultFlow = resultShareFlow<AppResponse>()


    init {

        _branchListResultFlow.getLatest {
            branchList.clear()
            if (it.status == 1) {
                branchList.addAll(it.data!!)
            }
        }

        _registerResultFlow.getLatest {
            if(it.status ==1){
                successDialog(it.message){
                    navigateUpWithResult("isRegistered" to true)
                }
            }else alertDialog(it.message)
        }
    }

    fun onProceed() {

        if (!validateDropDownState()) return


        val param = if(paymentModeState.value == "Account Deposit") hashMapOf(
            "customerId" to customerId.toString(),
            "receiverName" to input.name.getValue(),
            "receiverMobileNumber" to input.mobile.getValue(),
            "receiverRelationship" to relationShipState.value,
            "receiverAddress" to input.address.getValue(),
            "paymentMode" to paymentModeState.value,
            "receiverGender" to genderState.value,
            "accountNumber" to input.accountNumber.getValue(),
        )
        else  hashMapOf(
            "customerId" to customerId.toString(),
            "receiverName" to input.name.getValue(),
            "receiverMobileNumber" to input.mobile.getValue(),
            "receiverRelationship" to relationShipState.value,
            "receiverAddress" to input.address.getValue(),
            "paymentMode" to paymentModeState.value,
            "receiverGender" to genderState.value,
        )
        callApiForShareFlow(_registerResultFlow) { repository.addBeneficiary(param) }

    }

    private fun validateDropDownState(): Boolean {
        if (genderState.value.isEmpty()) {
            alertBanner("Receiver Gender", "gender field can't be empty")
            return false
        }
        if (relationShipState.value.isEmpty()) {
            alertBanner("Receiver Relationship", "relationship field can't be empty")
            return false
        }
        if (paymentModeState.value == "Cash Payment") return true
        else {
            if (bankState.value.isEmpty()) {
                alertBanner("Receiver Bank", "bank field can't be empty")
                return false
            }
            if (branchState.value.isEmpty()) {
                alertBanner("Receiver Bank Branch", "branch field can't be empty")
                return false
            }
            return true
        }
    }

    fun fetchBranchList(bankName: String) {
        callApiForShareFlow(_branchListResultFlow) { repository.fetchBranchList(bankName) }
    }


    data class InputForm(
        val isValidateAccountNumber: MutableState<Boolean>,
        val name: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
        val mobile: InputWrapper = InputWrapper { AppValidator.mobile(it) },
        val address: InputWrapper = InputWrapper { AppValidator.minThreeChar(it) },
        val accountNumber: InputWrapper = InputWrapper(isValidateAccountNumber) {
            AppValidator.accountNumberValidation(it)
        },
    ) : BaseInput(
        name, mobile, address, accountNumber
    )
}