package com.a2z.app.util.extension

import com.a2z.app.util.AppConstant
import com.a2z.app.util.DateUtil

fun String.toDoubleAmount(): Double {
    return try {
        this.toDouble()
    } catch (e: java.lang.Exception) {
        0.0
    }
}

fun String?.isValidateDate(): Boolean {
    if (this == null) return false
    return DateUtil.isValidDate(this)
}

fun String.removeDateSeparator(): String {
    return this.replace(AppConstant.DATE_SEPARATOR, "")
}

fun String.insertDateSeparator(character : String = AppConstant.DATE_SEPARATOR): String {
    val charArray = this.toCharArray()
    val build = StringBuilder()
    charArray.forEachIndexed { index, value ->
        if (index == 2 || index == 4)
            build.append("$character$value")
        else build.append(value.toString())
    }
    return build.toString()
}

fun String?.notNullOrEmpty(): Boolean {
    if (this == null) return false
    return this.isNotEmpty()
}

fun String?.nullOrEmptyToDouble(): Double {
    if(this == null || this == "" || this == "null" ) return 0.0
    if(this.trim().isEmpty()) return 0.0
    return try {
        this.toDouble()
    }catch (e : java.lang.Exception) {
        0.0
    }
}

fun String?.notAvailable() : String{
    return if(this == null || this.isEmpty()) "Not Available";
    else this
}
fun String.prefixRS() : String{
    return AppConstant.RUPEE_SYMBOL + " "+this
}

fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")
