package com.a2z.app.data.model.report

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class CommissionSchemeListResponse(
        val status: Int,
        val message: String,
        val result: List<CommissionScheme>? = null
) : Parcelable

@Keep
@Parcelize
data class CommissionScheme(
        val type: String,
        val title: String,
) : Parcelable

@Parcelize
@Keep
data class CommissionSchemeDetailResponse(
        val status : Int,
        val message: String,
        val type: String? = null,
        val title: String? = null,
        val result : List<CommissionSchemeDetail>? = null
) : Parcelable

@Parcelize
@Keep
data class CommissionSchemeDetail(
        @SerializedName("id")val id : String?,
        @SerializedName("service") val service : String?,
        @SerializedName("operatorName") val operator : String?,
        @SerializedName("category") val category : String?,
        @SerializedName("min_amt") val minAmount : String?,
        @SerializedName("max_amt")val maxAmount : String?,
        @SerializedName("type")val type : String?,
        @SerializedName("agent_charge_type")val agentChargeType : String?,
        @SerializedName("agent_charge")val agentCharge : String?,
        @SerializedName("agent_comm_type")val agentCommissionType : String?,
        @SerializedName("agent_comm")val agentCommission : String?,
) : Parcelable