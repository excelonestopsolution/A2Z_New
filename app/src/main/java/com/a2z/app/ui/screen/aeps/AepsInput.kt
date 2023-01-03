package com.a2z.app.ui.screen.aeps

import androidx.compose.runtime.MutableState
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.InputWrapper

data class AepsInput(

    val useAmountValidation : MutableState<Boolean>,
    val aadhaarInputWrapper: InputWrapper = InputWrapper { AppValidator.aadhaarValidation(it) },
    val mobileInputWrapper: InputWrapper = InputWrapper { AppValidator.mobileValidation(it) },
    val amountInputWrapper: InputWrapper = InputWrapper (useAmountValidation){
        AppValidator.amountValidation(
            it,
            minAmount = 100.0,
            maxAmount = 10000.0,

        )
    }

) : BaseInput(
    aadhaarInputWrapper,
    mobileInputWrapper,
    amountInputWrapper
)