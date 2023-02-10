package com.a2z.app.dist.data.repository

import com.a2z.app.dist.data.service.FundRequestService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class FundRequestRepository @Inject constructor(private val fundRequestService: FundRequestService) {

    suspend fun fetchBalanceInfo(
            userId: String,
            token: String,
            type: String) = fundRequestService.fetchBankList(
            userId,
            token,
            type)


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
    ) = fundRequestService.submitRequest(
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


}