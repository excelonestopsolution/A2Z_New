package com.a2z.app.ui.screen.utility.bill

import androidx.compose.runtime.MutableState
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.InputWrapper

data class BillPaymentInput(

    private var useMobileValidation: MutableState<Boolean>,
    private var useAmountValidation: MutableState<Boolean>,
    private var useEmailValidation: MutableState<Boolean>,
    private var useDobValidation: MutableState<Boolean>,

    private var inputValidator: (String) -> Pair<Boolean, String>,
    private var amountValidator: (String) -> Pair<Boolean, String>,

    val numberInputWrapper: InputWrapper = InputWrapper { inputValidator(it) },
    val mobileInputWrapper: InputWrapper = InputWrapper(useMobileValidation) {
        AppValidator.mobile(it)
    },
    val amountInputWrapper: InputWrapper = InputWrapper(useAmountValidation)
    { amountValidator(it) },
    val emailInputWrapper: InputWrapper = InputWrapper(useEmailValidation) {
        AppValidator.email(it)
    },
    val dobInputWrapper: InputWrapper = InputWrapper(useDobValidation) {
        AppValidator.dobValidation(it)
    }

) : BaseInput(
    numberInputWrapper,
    mobileInputWrapper,
    amountInputWrapper,
    emailInputWrapper,
    dobInputWrapper
)