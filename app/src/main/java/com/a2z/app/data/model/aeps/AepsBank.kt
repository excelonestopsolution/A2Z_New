package com.a2z.app.data.model.aeps

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class AepsBankListResponse(
    val status : Int,
    val message : String,
    @SerializedName("data")
    val banks : List<AepsBank> ?,
    val default_aeps_service : String
) : Parcelable

@Keep
@Parcelize
data class AepsBank(

    @SerializedName("bankName")
    val bankName : String?,
    @SerializedName("iinno")
    val bankInnNumber : String,
) : Parcelable