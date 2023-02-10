package com.a2z.app.model

import com.a2z.app.util.AppConstants
import androidx.annotation.Keep

@Keep
data class KeyValue(
        val key: String = AppConstants.EMPTY,
        val value: String = AppConstants.EMPTY
)