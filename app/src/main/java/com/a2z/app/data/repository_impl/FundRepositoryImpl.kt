package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.fund.FundRequestBankListResponse
import com.a2z.app.data.model.fund.PaymentReturnDetailResponse
import com.a2z.app.data.model.r2r.R2RSearchRetailerResponse
import com.a2z.app.data.network.FundService
import com.a2z.app.data.repository.FundRepository
import com.a2z.app.util.FieldMapData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FundRepositoryImpl @Inject constructor(
    private val service: FundService
) : FundRepository {

    override suspend fun fetchFundBankList(type: String): FundRequestBankListResponse {
        return service.fetchFundBankList(type)
    }

    override suspend fun submitRequest(
        slipFile: MultipartBody.Part?,
        amount: RequestBody,
        requestTo: RequestBody,
        paymentDate: RequestBody,
        paymentMode: RequestBody,
        refNumber: RequestBody,
        onlineMode: RequestBody,
        bankId: RequestBody?,
        bankName: RequestBody?,
        remark: RequestBody
    ) = service.submitRequest(
        slipFile = slipFile,
        amount = amount,
        requestTo = requestTo,
        paymentDate = paymentDate,
        paymentMode = paymentMode,
        refNumber = refNumber,
        onlineMode = onlineMode,
        bankId = bankId,
        bankName = bankName,
        remark = remark,
    )

    override suspend fun searchR2RRetailer(
        searchType: String,
        searchInput: String
    ): R2RSearchRetailerResponse {
        return service.searchR2RRetailer(searchType, searchInput)
    }

    override suspend fun fetchParentPaymentReturnDetail(): PaymentReturnDetailResponse {
        return service.fetchParentPaymentReturnDetail()
    }

}