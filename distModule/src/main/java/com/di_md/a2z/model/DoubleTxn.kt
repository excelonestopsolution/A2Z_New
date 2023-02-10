package com.di_md.a2z.model

import androidx.annotation.Keep

@Keep
data class DoubleTxn(
        val isException: Boolean = false,
        val number: String = "0",
        val amount: String = "0",
        val type : String = "",
        val time: Long = 0L

)
