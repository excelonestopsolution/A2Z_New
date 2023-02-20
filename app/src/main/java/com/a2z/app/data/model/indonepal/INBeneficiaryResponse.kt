package com.a2z.app.data.model.indonepal

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class INBeneficiaryResponse(
    val status : Int,
    val message : String,
    val data : List<INBeneficiary>?
) : Parcelable

@Keep
@Parcelize
data class INBeneficiary(
    val id : String?,
    val receiver_id : String?,
    val sender_id : String?,
    val name : String?,
    val mobile : String?,
    val gender : String?,
    val address : String?,
    val account_number : String?,
    val bank_branch_id : String?,
    val bank_branch_name : String?,
    val bank_name : String?,
    val relationship : String?,
    val paymentMode : String?,
    val user_id : String?,
) : Parcelable