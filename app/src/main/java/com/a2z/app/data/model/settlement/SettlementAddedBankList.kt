package com.a2z.app.data.model.settlement

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Keep
data class SettlementAddedBankListResponse(
    val status: Int,
    val message: String,
    val data: ArrayList<SettlementAddedBank>?
) : Parcelable

@Parcelize
@Keep
data class SettlementAddedBank(
    @SerializedName("id") val id: Int?,
    @SerializedName("account_number") val accountNumber: String?,
    @SerializedName("bank_name") val bankName: String?,
    @SerializedName("branch_name") val branchName: String?,
    @SerializedName("ifsc") val ifscCode: String?,
    @SerializedName("remark") val remark: String?,
    @SerializedName("is_bank_verified") val isBankVerified: Int?,
    @SerializedName("document_status") val documentStatus: Int?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("status_id") val statusId: Int?,
    @SerializedName("balance") val balance: String?,
    @SerializedName("aeps_bloacked_amount") val aepsBlockAmount: String?,
    @SerializedName("aeps_charge") val aepsCharge: String?

) : Parcelable