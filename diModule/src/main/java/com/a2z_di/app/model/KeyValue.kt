package com.a2z_di.app.model

import com.a2z_di.app.util.AppConstants
import androidx.annotation.Keep

@Keep
data class KeyValue(
        val key: String = AppConstants.EMPTY,
        val value: String = AppConstants.EMPTY
)