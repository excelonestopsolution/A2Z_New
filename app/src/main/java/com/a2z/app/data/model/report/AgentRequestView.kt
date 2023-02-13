package com.a2z.app.data.model.report

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import org.json.JSONObject

@Parcelize
@Keep
data class AgentRequestViewResponse(
    val status: Int,
    val message: String?,
    @SerializedName("result") val report: List<AgentRequestView>?,
    var mRemarks: List<Pair<String, String>>?
) : Parcelable


@Parcelize
@Keep
data class AgentRequestView(
    val created_at: String?,
    val id: String?,
    val user_id: String?,
    val user_name: String?,
    val firm_name: String?,
    val role: String?,
    val mobile: String?,
    val mode: String?,
    val branch_code: String?,
    val online_payment_mode: String?,
    val deposit_date: String?,
    val bank_name: String?,
    val remark: String?,
    val slip: String?,
    val ref_id: String?,
    val amount: String?,
    val status: String?,
    val status_id: Int?,

    ) : Parcelable