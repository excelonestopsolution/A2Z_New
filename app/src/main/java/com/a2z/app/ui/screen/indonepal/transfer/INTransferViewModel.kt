package com.a2z.app.ui.screen.indonepal.transfer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.indonepal.*
import com.a2z.app.data.repository.IndoNepalRepository
import com.a2z.app.data.repository.TransactionRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.screen.indonepal.INUtil
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.safeParcelable
import com.a2z.app.util.ApiUtil
import com.a2z.app.util.extension.nullOrEmptyToDouble
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class INTransferViewModel @Inject constructor(
    private val repository: IndoNepalRepository,
    private val transactionRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {


    val mpinDialogState = mutableStateOf(false)
    val beneficiary = savedStateHandle.safeParcelable<INBeneficiary>("beneficiary")!!
    val sender = savedStateHandle.safeParcelable<INSender>("sender")!!

    val staticData = INUtil.staticData()

    val isChargeFetched = mutableStateOf(false)
    val input = InputForm(isChargeFetched)
    val selectedReasonOfTransfer = mutableStateOf("")
    private val _serviceChargeResultFlow = resultShareFlow<INServiceChargeResponse>()
    private val _transactionResultFlow = resultShareFlow<INTransferResponse>()
    private val _verifyTransactionResultFlow = resultShareFlow<AppResponse>()

    var serviceCharge: INServiceCharge? = null

    private val _txnOtpResultFlow = resultShareFlow<INCommonOtpResponse>()
    private var txnProcessId = ""


    private var response: INTransferData? = null

    init {

        _serviceChargeResultFlow.getLatest {
            if (it.status == 1) {
                successBanner("Service Charge", it.message)
                serviceCharge = it.data
                isChargeFetched.value = true
            } else alertDialog(it.message)
        }

        _txnOtpResultFlow.getLatest {
            if (it.status == 1) {
                txnProcessId = it.data?.txnProcessId ?: ""
                successDialog(it.message)
            } else alertDialog(it.message)
        }

        _transactionResultFlow.getLatest(
            progress = { transactionProgressDialog() },
            failure = {
                pendingDialog("Transaction response interrupted with blank response. Please check report.") {
                    navigateTo(NavScreen.DashboardScreen.route)
                }
            },
            success = {
                if (it.status == 1) {
                    if (it.data!!.status == 3 || it.data.status == 34) {
                        response = it.data
                        verifyTransaction()
                    } else alertDialog(it.message)
                } else alertDialog(it.message)
            }
        )

        _verifyTransactionResultFlow.getLatest(
            progress = {
                transactionProgressDialog()
            },
            failure = {
                navigateUpWithResult()
                navigateTo(NavScreen.IndoNepalTxnScreen.passArgs(response!!.apply {
                    this.isTransaction = true
                }))
            },
            success = {
                if (it.status == 1) {
                    response?.status = 1
                    response?.message = it.message
                }
                navigateUpWithResult()
                navigateTo(NavScreen.IndoNepalTxnScreen.passArgs(response!!.apply {
                    this.isTransaction = true
                }))
                dismissDialog()
            }
        )
    }


    fun onTransfer() {
        if (txnProcessId.isEmpty()) {
            failureBanner("OTP Error", "Otp didn't received successfully!")
            return
        }
        mpinDialogState.value = true
    }

    fun transfer(mpin: String) {



        val param = hashMapOf(
            "customerId" to sender.customerId.toString(),
            "senderName" to sender.name.toString(),
            "gender" to sender.gender.toString(),
            "dob" to sender.dob.toString(),
            "senderAddress" to sender.address.toString(),
            "senderDistic" to sender.district.toString(),
            "senderMobileNumber" to sender.mobile.toString(),
            "employer" to sender.employer.toString(),
            "senderCountry" to "India",
            "nationality" to sender.nationality.toString(),
            "senderTypeId" to sender.idType.toString(),
            "senderIdNumber" to sender.idNumber.toString(),
            "txnReceiverID" to beneficiary.receiver_id.toString(),
            "txnReceiverName" to beneficiary.name.toString(),
            "txnReceiverGender" to beneficiary.gender.toString(),
            "txnReceiverGender" to beneficiary.gender.toString(),
            "txnReceiverAddress" to beneficiary.address.toString(),
            "txnReceiverMobile" to beneficiary.mobile.toString(),
            "txnReceiverCity" to sender.city.toString(),
            "txnPaymentMode" to beneficiary.paymentMode.toString(),
            "sendAmount" to input.amount.getValue(),
            "collectionAmount" to serviceCharge!!.collectionAmount.toString(),
            "payAmount" to serviceCharge!!.payoutAmount.toString(),
            "serviceCharge" to serviceCharge!!.serviceCharge.toString(),
            "exchangeRate" to serviceCharge!!.exchangeRate.toString(),
            "accountNumber" to beneficiary.account_number.toString(),
            "txnBranchID" to beneficiary.bank_branch_id.toString(),
            "txnBankName" to beneficiary.bank_name.toString(),
            "txnBranchName" to beneficiary.bank_branch_name.toString(),
            "txnReceiverRelationship" to beneficiary.relationship.toString(),
            "remittanceReason" to selectedReasonOfTransfer.value.toString(),
            "incomeSource" to sender.incomeSource.toString(),
            "senderState" to sender.state.toString(),
            "txnPin" to mpin,
            "txnOtp" to input.otp.getValue(),
            "txnProcessId" to txnProcessId,
        )

        callApiForShareFlow(_transactionResultFlow) { transactionRepository.indoNepalTransfer(param) }

    }

    private fun verifyTransaction() {



        val param = hashMapOf(
            "indoTxnPin" to response?.bank_ref.toString(),
            "recordId" to response?.report_id.toString()
        )
        callApiForShareFlow(_verifyTransactionResultFlow) {
            repository.verifyTransaction(param)
        }
    }

    fun fetchCharge() {

        callApiForShareFlow(_serviceChargeResultFlow) {
            repository.serviceCharge(
                hashMapOf(
                    "paymentMode" to beneficiary.paymentMode.toString(),
                    "sendAmount" to input.amount.getValue(),
                    "branchId" to beneficiary.bank_branch_id.toString()
                )
            )
        }

    }

    fun onAmountChange(it: String) {
        input.amount.onChange(it)
        if (serviceCharge != null) {
            isChargeFetched.value =
                serviceCharge!!.transferAmount.nullOrEmptyToDouble() == it.nullOrEmptyToDouble()
        }

    }

    fun sendOtp() {
        callApiForShareFlow(_txnOtpResultFlow) {
            repository.txnOtp(
                hashMapOf(
                    "senderMobileNumber" to sender.mobile.toString(),
                    "type" to "SendTransaction",
                    "txnPaymentMode" to beneficiary.paymentMode.toString(),
                    "customerId" to sender.customerId.toString(),
                    "ReceiverId" to beneficiary.receiver_id.toString(),
                    "sendAmount" to input.amount.getValue(),
                )
            )
        }
    }


    data class InputForm(
        val otpValidate: MutableState<Boolean>,
        val amount: InputWrapper = InputWrapper {
            AppValidator.amountValidation(
                it,
                minAmount = 200.0,
                maxAmount = 49800.0
            )
        },
        val otp: InputWrapper = InputWrapper(otpValidate) { AppValidator.otp(it) }
    ) : BaseInput(amount, otp)
}