package com.a2z.app.data.model.indonepal

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class INCommonOtpResponse(
    val status: Int,
    val message: String,
    val data: INTxnOtp?
) : Parcelable


@Keep
@Parcelize
data class INTxnOtp(
    val message: String?,
    val txnProcessId: String?,
    val ProcessId: String?,
) : Parcelable
