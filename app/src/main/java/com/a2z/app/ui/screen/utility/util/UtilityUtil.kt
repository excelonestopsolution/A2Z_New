package com.a2z.app.ui.screen.utility.util

import com.a2z.app.R

open class UtilityUtil(val operatorType: OperatorType) {


    fun getOperatorTitle(suffix: String? = null) = when (operatorType) {
        OperatorType.PREPAID -> "Prepaid"
        OperatorType.POSTPAID -> "Postpaid"
        OperatorType.DTH -> "DTH"
        OperatorType.FAS_TAG -> "Fastag"
        OperatorType.ELECTRICITY -> "Electricity"
        OperatorType.WATER -> "Water"
        OperatorType.GAS -> "Gas"
        OperatorType.INSURANCE -> "Insurance"
        OperatorType.LOAN_REPAYMENT -> "Load"
        OperatorType.BROADBAND -> "BroadBand"
    } + if (suffix == null) "" else " $suffix"

    fun getIconFromOperatorType() = when (operatorType) {
        OperatorType.PREPAID -> R.drawable.ic_launcher_mobile
        OperatorType.POSTPAID -> R.drawable.ic_launcher_mobile
        OperatorType.DTH -> R.drawable.ic_launcher_dth
        OperatorType.FAS_TAG -> R.drawable.ic_launcher_fastag
        OperatorType.ELECTRICITY -> R.drawable.ic_launcher_electricity
        OperatorType.WATER -> R.drawable.ic_launcher_water
        OperatorType.GAS -> R.drawable.ic_launcher_gas
        OperatorType.INSURANCE -> R.drawable.ic_launcher_insurence
        OperatorType.LOAN_REPAYMENT -> R.drawable.ic_launcher_insurence
        OperatorType.BROADBAND -> R.drawable.ic_launcher_broadband
    }
}