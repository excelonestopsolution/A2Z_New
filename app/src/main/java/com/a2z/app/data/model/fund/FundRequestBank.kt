package com.a2z.app.data.model.fund

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class FundRequestBankListResponse(
    val status : Int,
    val message : String,
    val banks : List<FundRequestBank>
): Parcelable


@Keep
@Parcelize
data class FundRequestBank(
    var id: String? = "",
    var mobile: String? = "",
    var bank_transfer_place_holder: String? = "",
    var cash_deposit_place_holder: String? = "",
    var cash_cdm_place_holder: String? = "",
    var account_number: String? = "",
    var ifsc: String? = "",
    @SerializedName("branch_name")
    var branchName: String? = "",
    @SerializedName("bank_name")
    var bankName: String? = "",
    @SerializedName("message_one")
    var messageOne: String? = "",
    @SerializedName("message_two")
    var messageTwo: String? = "",
    var status: String? = "",
    @SerializedName("charge")
    var charges: String? = "",
    var balance: String? = "",
    var aeps_bloack_amount: String? = "",
    var aeps_charge: String? = "",
    var beneName: String? = "",
    var document_status: Int? = 0,
    var isHide: Boolean= false,
):Parcelable
