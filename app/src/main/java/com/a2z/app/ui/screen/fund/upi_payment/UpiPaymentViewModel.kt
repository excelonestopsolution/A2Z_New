package com.a2z.app.ui.screen.fund.upi_payment

import androidx.compose.runtime.mutableStateOf
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.fund.UpiPaymentInitiateResponse
import com.a2z.app.data.repository.FundRepository
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class UpiPaymentViewModel @Inject constructor(
    private val repository: FundRepository,
    val appPreference: AppPreference
) : BaseViewModel() {

    val input = FormInput()
    private val _initiateResultFlow = resultShareFlow<UpiPaymentInitiateResponse>()
    val onInitiateSuccessResult = MutableSharedFlow<Boolean>()

    val qrcodeDialogState = mutableStateOf(false)

    var actionType = QRCodeActionType.SELF_PAYMENT

    var upiUri = mutableStateOf("")

    init {
        _initiateResultFlow.getLatest {
            if (it.status == 1) {

                val amount = input.amount.getValue()
                val mobile = appPreference.user?.mobile
                val shopName = appPreference.user?.shopName
                val refId = it
                upiUri.value =  "upi://pay?pa=excelone@icici&pn=Excel Stop&tr=$refId&am=$amount" +
                        "&cu=INR&mc=5411&tn=$mobile$shopName"
                onInitiateSuccessResult.emit(true)
            } else failureDialog(it.message)
        }
    }

    fun fetchData(type: QRCodeActionType) {
        actionType = type
        val param = hashMapOf("amount" to input.amount.getValue())
        callApiForShareFlow(_initiateResultFlow) {
            repository.initiateUpiPayment(param)
        }
    }

    data class FormInput(
        val amount: InputWrapper = InputWrapper {
            AppValidator.amountValidation(
                it,
                minAmount = 1.0
            )
        }
    ) : BaseInput(amount)

    enum class QRCodeActionType {
        SELF_PAYMENT, GENERATE_QR_CODE
    }
}