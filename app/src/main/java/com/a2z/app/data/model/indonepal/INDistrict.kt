package com.a2z.app.data.model.indonepal

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class INDistrictResponse(
    val status : Int,
    val message : String,
    val data : List<INDistrict>
) : Parcelable


@Keep
@Parcelize
data class INDistrict(
    val distic : String
): Parcelable