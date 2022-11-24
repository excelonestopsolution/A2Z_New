package com.a2z.app.ui.screen.utility.util

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.provider.Operator
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.util.extension.notNullOrEmpty

class BillPaymentUtil(
    private val operator: Operator,
    private val appPreference: AppPreference,
    operatorType: OperatorType
) : UtilityUtil(operatorType) {

    var useEmailValidation = mutableStateOf(false)
    var useDobValidation = mutableStateOf(false)
    var useAmountValidation = mutableStateOf(false)
    var useMobileValidation = mutableStateOf(true)
    init {
        extraFieldValidation()
    }

    val inputMaxLength: Int
        get() = if (operator.maxLength == 0) 18
        else operator.maxLength ?: 18

    val fetchBillInfo = mutableStateOf(operator.payWithoutFetch == 0)

    private val isAlphaNumeric: Boolean
        get() = operator.isAlphaNumeric == 1 ||
                (operator.isAlphaNumeric == 0
                        && operator.isNumeric == 0)

    val buttonText: String
        get() = if (fetchBillInfo.value) "Fetch Bill Info" else "Proceed To Payment"

    val actionType: BillPaymentAction
        get() =
            if (fetchBillInfo.value) BillPaymentAction.FETCH_BILL_INFO
            else BillPaymentAction.PROCEED_TO_PAYMENT

    fun numberValidator(
        value: String,
    ): Pair<Boolean, String> {
        fun validate() = Pair(true, "")
        fun characterText() =
            if (isAlphaNumeric) "character ${operator.getSafeDealerName()}" else "digits ${operator.getSafeDealerName()}"
        return if (operator.minLength == 0 && operator.maxLength!! > 0) {
            if (operator.maxLength == value.length) validate()
            else Pair(false, "Enter ${operator.maxLength} ${characterText()}")
        } else if (operator.minLength!! > 0 && operator.maxLength == 0) {
            if (value.length >= operator.minLength) validate()
            else Pair(false, "Enter minimum ${operator.minLength} ${characterText()}")
        } else if (operator.minLength > 0 && operator.maxLength!! > 0) {
            if (value.length >= operator.minLength && value.length <= operator.maxLength) validate()
            else Pair(false, "Enter ${operator.maxLength} ${characterText()}")
        } else if (operator.maxLength == operator.minLength && operator.maxLength == 0) {
            if (value.length > 4) validate()
            else Pair(false, "Enter minimum 5 ${characterText()}")
        } else validate()
    }

    fun amountValidator(value: String): Pair<Boolean, String> {

        var minAmount = operator.minAmount!!
        var maxAmount = operator.maxAmount!!

        if (minAmount == 0.0) minAmount = 1.0
        if (maxAmount == 0.0) maxAmount = 10000.0
        return AppValidator.rechargeAmountValidation(
            inputAmount = value,
            userBalance = appPreference.user?.userBalance ?: "0",
            minAmount = minAmount,
            maxAmount = maxAmount
        )
    }

    fun getNumberFieldLabel() = operator.getSafeDealerName()

    fun getNumberFieldKeyboardOption() =
        if (isAlphaNumeric)
            Pair(KeyboardType.Text, KeyboardCapitalization.Characters)
        else Pair(KeyboardType.Number, KeyboardCapitalization.None)


    fun getNumberFieldDownText(): String {
        return if (operator.minLength == 0 && operator.maxLength == 0)
            "Enter ${operator.getSafeDealerName()}"
        else if (operator.minLength == operator.maxLength)
            "Enter ${operator.maxLength} digits ${operator.getSafeDealerName()}"
        else if (operator.minLength != 0 && operator.maxLength != 0)
            "Enter ${operator.minLength} to ${operator.maxLength} digits ${operator.getSafeDealerName()}"
        else "Enter Number"
    }

    fun getAmountFieldDownText(): String {
        val minAmount = operator.minAmount!!
        val maxAmount = operator.maxAmount!!
        return if (minAmount == 0.0) {
            "Enter min amount rs 1"
        } else if (minAmount > 0.0 && maxAmount == 0.0) {
            "Enter min amount rs $minAmount"
        } else if (minAmount > 0.0 && maxAmount > 0.0) {
            "Enter amount rs $minAmount - rs $maxAmount"
        } else "Enter min amount rs 1"
    }


    val fetchBillProgressTitle = if(operatorType == OperatorType.FAS_TAG)
        "Fetching Vehicle Info" else "Fetching Bill Info"



    private fun extraFieldValidation() {
        val email: String? = operator.extraParams.find { it?.fieldName == "email" }?.fieldName
        val dob: String? = operator.extraParams.find { it?.fieldName == "dob" }?.fieldName

        if (email.notNullOrEmpty()) useEmailValidation.value = true
        if (dob.notNullOrEmpty()) useDobValidation.value = true

        if (operatorType == OperatorType.POSTPAID) useMobileValidation.value = false
    }
}