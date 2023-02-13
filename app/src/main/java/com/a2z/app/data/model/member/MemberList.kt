package com.a2z.app.data.model.member

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Keep
data class MemberListResponse(
    val status : Int,
    val message : String?,
    val page : String?,
    val members : List<Member>?
) : Parcelable

@Keep
@Parcelize
data class Member(
    val id : String?,
    @SerializedName("name", alternate = ["userDetails"])val name : String?,
    val balance : String?,
    val status : String?,
    val shopName : String?,
    val email : String?,
    val mobile : String?,
    val statusId : String?,
    val parentDetails : String?,
    val prefix : String?,
) : Parcelable