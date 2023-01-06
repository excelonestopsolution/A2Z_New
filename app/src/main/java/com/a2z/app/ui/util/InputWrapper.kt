package com.a2z.app.ui.util

import androidx.compose.runtime.*
import com.a2z.app.ui.util.resource.FormErrorType
import com.a2z.app.util.FormFieldError

class InputWrapper constructor(
    val useValidation: MutableState<Boolean> = mutableStateOf(true),
    val validator: (String) -> Pair<Boolean, String>
) {
    val input: MutableState<String> = mutableStateOf("")
    val error: MutableState<FormFieldError> = mutableStateOf(FormErrorType.Initial)
    fun onChange(value: String) {
        this.input.value = value
        error.value = makeValid()
    }

    fun getValue() = input.value


    fun validate(): FormFieldError {
        if (!useValidation.value) return FormErrorType.Success
        error.value = makeValid()
        return error.value
    }

    fun setValue(value: String?) {

        input.value = value ?:""
        error.value = makeValid()
    }

    private fun makeValid(): FormErrorType<String> {
        val validatorResult = validator(input.value)
        return BaseValidator(validatorResult.first, validatorResult.second)
    }


    @Composable
    fun formError(): FormFieldError {
        val value by remember { error }
        return value
    }

    @Composable
    fun isError(): Boolean {
        val value by remember { error }
        return (value is FormErrorType.Failure)
    }

    @Composable
    fun isValid(): Boolean {
        val value by remember { error }
        return (value is FormErrorType.Success)
    }

    @Composable
    fun formValue(): String {
        val value by remember { input }
        return value
    }
}

open class BaseInput(private vararg val makeValid: InputWrapper) {

    val isValidObs get() = mutableStateOf(allInputValid())

    fun validate(): Boolean {
        makeValid.forEach { it.validate() }
        return allInputValid()
    }

    private fun allInputValid() = ValidateInput(makeValid.asList())
}
