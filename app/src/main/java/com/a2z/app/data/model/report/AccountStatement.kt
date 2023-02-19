package com.a2z.app.data.model.report

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class AccountStatementResponse(
    val status : Int,
    val message : String?,
    val reports : List<AccountStatement>?
) : Parcelable


@Keep
@Parcelize
data class AccountStatement(
     val id : String?,
     val created_at : String?,
     val user_name : String?,
     val mobile_number : String?,
     val product : String?,
     val bank_name : String?,
     val name : String?,
     val number : String?,
     val txn_id : String?,
     val description : String?,
     val opening_bal : String?,
     val amount : String?,
     val credit_charge : String?,
     val debit_charge : String?,
     val total_bal : String?,
     val remark : String?,
     val statusId : String?,
     val status : String?,
     val mode : String?,
) : Parcelable