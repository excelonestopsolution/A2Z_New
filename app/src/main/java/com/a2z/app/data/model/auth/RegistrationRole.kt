package com.a2z.app.data.model.auth

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RegistrationRoleResponse(
    val status: Int,
    val message: String,
    val roles: List<RegistrationRole>? = null

) : Parcelable

@Keep
@Parcelize
data class RegistrationRole(
    val roleId: Int,
    val title: String,
    val selected: Boolean = false
) : Parcelable


@Keep
@Parcelize
data class RegistrationRoleUser(
    var id: Int,
    val userDetails: String,
    val mobile: String,
    var relationId: Int? = null
) : Parcelable
