package com.a2z.app.ui.screen.fund.request

import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.InputWrapper

data class FundRequestInput(
    val dateInput: InputWrapper = InputWrapper { AppValidator.dobValidation(it) },
    val amountInput : InputWrapper = InputWrapper{AppValidator.amountValidation(it, minAmount = 1.0)},
    val bankRefInput : InputWrapper = InputWrapper{AppValidator.empty(it)},
    val paymentModeInput : InputWrapper = InputWrapper{AppValidator.empty(it)},
    val slipUploadInput : InputWrapper = InputWrapper{AppValidator.empty(it)},
) : BaseInput(
    dateInput,
    amountInput,
    bankRefInput,
    paymentModeInput,
    slipUploadInput
)