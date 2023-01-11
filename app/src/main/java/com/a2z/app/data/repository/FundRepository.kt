package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.fund.FundRequestBankListResponse
import com.a2z.app.data.model.r2r.R2RSearchRetailerResponse
import com.a2z.app.util.FieldMapData
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface FundRepository {
    suspend fun fetchFundBankList(type : String): FundRequestBankListResponse

    suspend fun submitRequest(
        slipFile : MultipartBody.Part?,
        amount: RequestBody,
        requestTo: RequestBody,
        paymentDate: RequestBody,
        paymentMode: RequestBody,
        refNumber: RequestBody,
        onlineMode: RequestBody,
        bankId: RequestBody?,
        bankName: RequestBody?,
        remark: RequestBody,
    ) : AppResponse

    //r2r

    suspend fun searchR2RRetailer(
        searchType : String,
        searchInput : String,
    ) : R2RSearchRetailerResponse

}