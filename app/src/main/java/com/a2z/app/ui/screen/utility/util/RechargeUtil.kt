package com.a2z.app.ui.screen.utility.util

import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.provider.Operator
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.util.AppUtil

class RechargeUtil(
    private val appPreference: AppPreference?=null,
    private val operator: Operator,
    operatorType: OperatorType,
) : UtilityUtil(operatorType){

    private val isPrepaid : Boolean
    get() = operatorType == OperatorType.PREPAID


    fun getNumberDownText(): String {
        return if (isPrepaid) {
            "Enter 10 digits mobile number"
        } else when (operator.id) {
            "12" -> "Number start with 0 and is 11 digits long"
            "13" -> "Number start with 1 and is 10 digits long"
            "17" -> "Number start with 3 and is 10 digits long"
            else -> "Enter valid DTH Number."
        }
    }

    fun getInputMinAmount(): Double {
        return if (isPrepaid)
            10.0
        else when (operator.id) {
            "12" -> 100.0
            "13" -> 50.0
            "17" -> 100.0
            else -> 100.0
        }
    }

    fun getInputMaxLength(): Int {
        return if (isPrepaid) 10
        else when (operator.id) {
            "12" -> 11
            "13" -> 10
            "17" -> 10
            else -> 14
        }
    }

    private fun getInputMinLength(): Int{
        return if (isPrepaid) 10
        else when (operator.id) {
            "12" -> 11
            "13" -> 10
            "17" -> 10
            else -> -1
        }
    }

    private fun inputStartWith(): List<Int>? {
        if (isPrepaid)
            return listOf(9, 8, 7, 6)
        return when (operator.id) {
            "12" -> listOf(0)
            "13" -> listOf(1)
            "17" -> listOf(3)
            else -> null
        }
    }


    fun rechargeInputValidator(value: String) =
        AppValidator.rechargeInputValidation(value, inputStartWith(), getInputMinLength())

    fun rechargeAmountValidator(value: String): Pair<Boolean, String> {

      return   AppValidator.rechargeAmountValidation(
            value, appPreference!!.user!!.userBalance,
            minAmount = getInputMinAmount()
        )
    }
}