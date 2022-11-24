package com.a2z.app.data.model.provider

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Operator(
    @SerializedName("id") val id: String?,
    @SerializedName("provider_name") val operatorName: String?,
    @SerializedName("provider_image") val providerImage: String?,
    @SerializedName("dealer_name") val dealerName: String?,
    @SerializedName("is_amount_editable") val isAmountEditable: Int?,
    @SerializedName("pay_without_fetch") val payWithoutFetch: Int?,
    @SerializedName("is_numeric") val isNumeric: Int?,
    @SerializedName("is_alpha_numeric") val isAlphaNumeric: Int?,
    @SerializedName("min_length") val minLength: Int?,
    @SerializedName("max_length") val maxLength: Int?,
    @SerializedName("min_amount") val minAmount: Double?,
    @SerializedName("max_amount") val maxAmount: Double?,
    @SerializedName("message_amount") val messageAmount: String?,
    @SerializedName("extraparam") val extraParams: List<ProviderExtraParam?>,
    var serviceId: String = "",
    var baseUrl: String = ""
) : Parcelable {
    fun getSafeDealerName(): String {
        return if (dealerName == null) "Number"
        else if (dealerName.isEmpty() || dealerName == "null") "Number"
        else dealerName
    }
}


@Keep
@Parcelize
data class ProviderExtraParam(
    val fieldName: String?
) : Parcelable


@Keep
@Parcelize
data class OperatorResponse(
    val status: Int,
    val serviceId: String?,
    val baseUrl: String?,
    @SerializedName("provider") val providers: List<Operator>? = null
) : Parcelable
