package com.a2z.app.data.model.matm

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatmInitiate(
    val appKey : String,
    val username : String,
    val password : String,
    val transactionId : String,
) : Parcelable