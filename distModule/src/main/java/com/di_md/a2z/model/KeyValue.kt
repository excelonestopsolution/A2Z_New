package com.di_md.a2z.model

import androidx.annotation.Keep
import com.di_md.a2z.util.AppConstants


@Keep
data class KeyValue(
        val key: String = AppConstants.EMPTY,
        val value: String = AppConstants.EMPTY
)