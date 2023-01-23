package com.a2z.app.ui.screen.matm

import androidx.compose.runtime.MutableState
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.InputWrapper

data class MatmInput(

    val useAmountValidation : MutableState<Boolean>,
    val mobileInputWrapper: InputWrapper = InputWrapper { AppValidator.mobile(it) },
    val amountInputWrapper: InputWrapper = InputWrapper (useAmountValidation){
        AppValidator.amountValidation(
            it,
            minAmount = 100.0,
            maxAmount = 10000.0,
            useMultipleOfTen = true
        )
    }

) : BaseInput(
    mobileInputWrapper,
    amountInputWrapper
)