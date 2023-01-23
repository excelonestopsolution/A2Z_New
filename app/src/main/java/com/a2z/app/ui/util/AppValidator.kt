package com.a2z.app.ui.util

import android.text.TextUtils
import android.util.Patterns
import com.a2z.app.ui.util.resource.FormErrorType
import com.a2z.app.util.extension.toDoubleAmount
import java.util.regex.Matcher
import java.util.regex.Pattern


object BaseValidator {
    operator fun invoke(
        condition: Boolean,
        message: String
    ): FormErrorType<String> {
        return if (condition) FormErrorType.Success
        else FormErrorType.Failure(message)
    }
}

object ValidateInput {
    operator fun invoke(inputWrapper: List<InputWrapper>): Boolean {
        return inputWrapper.all {
            it.error.value == FormErrorType.Success || !it.useValidation.value
        }
    }
}


object AppValidator {

    fun userId(value: String) = Pair(
        value.length in 6..10,
        "Enter 6 - 10 characters user id"
    )


    fun minThreeChar(value: String) = Pair(
        value.length >= 3,
        "Enter min 3 characters"
    )

    fun upiId(value: String) = Pair(
        value.length >= 3 && value.contains("@"),
        "Enter valid upi id"
    )

    fun password(value: String) = Pair(
        validatePassword(value),
        "Enter valid password" +
                ""
    )

    fun confirmPassword(value: String, newPassword: String) = Pair(
        value == newPassword,
        "Confirm password doesn't matched!"
    )

    fun empty(value: String) = Pair(
        value.isNotEmpty(), "Field is required*"
    )

    fun mpinValidation(value: String, length: Int = 6) = Pair(
        value.length == length,
        "Enter $length digits M-PIN"
    )

    fun confirmMpin(value: String, newMpin: String, length: Int = 6) = Pair(
        value.length == length && value == newMpin,
        "Confirm M-PIN doesn't matched!"
    )

    fun pinCodeValidation(value: String) = Pair(
        value.length == 6,
        "Enter 6 digits Pin Code"
    )

    fun pan(value: String) = Pair(
        value.length == 10,
        "Enter valid 10 chars pan number"
    )

    fun ifscCode(value: String) = Pair(
        value.length == 11,
        "Enter 11 digits Ifsc Code"
    )

    fun amountValidation(
        inputAmount: String,
        minAmount: Double = 100.0,
        maxAmount: Double = 1000000.0,
        useMultipleOfTen : Boolean = false,
    ): Pair<Boolean, String> {
        val amount = inputAmount.toDoubleAmount()
        val message = "Enter Rs. $minAmount - Rs. $maxAmount"
        val condition = amount in minAmount..maxAmount

        if(condition && useMultipleOfTen){
            if(amount % 10.0 != 0.0){
                return Pair(false,"Enter amount multiple of 10")
            }
        }
        return Pair(condition, message)


    }


    fun rechargeAmountValidation(
        inputAmount: String,
        userBalance: String,
        minAmount: Double = 10.0,
        maxAmount: Double = 10000.0
    ): Pair<Boolean, String> {
        val amount = inputAmount.toDoubleAmount()
        val balance = userBalance.toDoubleAmount()

        var condition = balance >= amount
        return if (condition) {
            val message = "Enter Rs. $minAmount - Rs. $maxAmount"
            condition = amount in minAmount..maxAmount
            Pair(condition, message)
        } else {
            val message = "Insufficient balance ₹ $balance"
            Pair(false, message)
        }

    }

    fun rechargeInputValidation(
        value: String,
        numbersStartWith: List<Int>?,
        minLength: Int
    ): Pair<Boolean, String> {

        var isTrue = false
        numbersStartWith?.forEach {
            if (value.startsWith(it.toString()))
                isTrue = true

        } ?: run {
            isTrue = true
        }

        if (!isTrue)
            return Pair(
                false,
                "Number should start with ${
                    numbersStartWith.toString()
                        .replace("[", "")
                        .replace("]", "")
                }"
            )


        if (value.length != minLength)
            if (minLength != -1) return Pair(false, "Enter $minLength digit number")


        if (value.length < 6) {
            return Pair(false, "Enter min 6 digit number")
        }

        return Pair(true, "")


    }

    fun otp(value: String, length: Int = 6) = Pair(
        value.length == length,
        "Enter $length digits OTP"
    )


    fun mobile(value: String) = Pair(
        value.length == 10,
        "Enter 10 digits mobile number"
    )

    fun accountNumberValidation(value: String) = Pair(
        value.length in 6..20,
        "Enter 6 to 20 digits account"
    )

    fun confirmAccountNumber(value: String, accountNumber: String) = Pair(
        value == accountNumber,
        "Confirm account doesn't matched!"
    )


    fun aadhaarValidation(value: String) = Pair(
        value.length == 12,
        "Enter 12 digits aadhaar number"
    )


    fun email(value: String) = Pair(
        validateEmail(value),
        "Enter valid email id"
    )

    fun dobValidation(value: String) = Pair(
        value.length == 8, "Enter Date 01/01/1970 form"
    )

    private fun validatePassword(password: String?): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        val pattern: Pattern = Pattern.compile(passwordPattern)
        val matcher: Matcher = pattern.matcher(password.orEmpty())
        return matcher.matches()
    }

    private fun validateEmail(value: String) =
        !TextUtils.isEmpty(value) && Patterns.EMAIL_ADDRESS.matcher(value).matches()


}