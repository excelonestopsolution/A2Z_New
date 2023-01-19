package com.a2z.app.data.model.app

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class QRCodeResponse(
        val status : Int,
        val message : String,
        @SerializedName("str_url")
        val strUrl: String,
        @SerializedName("retailer_qr_upi_data")
        val retailerQRUpiData : String,
        @SerializedName("dynamic_vpn_info")
        val dynamicVpnInfo: String
) : Parcelable