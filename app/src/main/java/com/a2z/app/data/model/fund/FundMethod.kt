package com.a2z.app.data.model.fund

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import com.a2z.app.ui.screen.fund.method.FundMethodType
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class FundMethod(
    val name: String,
    @DrawableRes val drawable: Int,
    val type: FundMethodType
) : Parcelable