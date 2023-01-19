package com.a2z.app.data.model.matm

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class MatmInitiate(
    val appKey : String,
    val username : String,
    val password : String,
    val transactionId : String,
) : Parcelable