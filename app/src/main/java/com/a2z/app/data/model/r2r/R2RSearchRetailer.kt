package com.a2z.app.data.model.r2r

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class R2RSearchRetailerResponse(
    val status : Int,
    val message : String,
    val hasData : Boolean,
    val data : ArrayList<R2RSearchRetailer>
) : Parcelable

@Parcelize
@Keep
data class R2RSearchRetailer(
    val id : Int,
    val name : String,
    val mobile : String,
    val memberId : Int,
    val shopName : String,
) : Parcelable