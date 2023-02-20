package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.indonepal.*
import com.a2z.app.data.model.settlement.SettlementAddedBankListResponse
import com.a2z.app.data.model.settlement.SettlementBankListResponse
import com.a2z.app.util.FieldMapData

interface IndoNepalRepository {
    suspend fun mobileVerification(data : FieldMapData): INMobileVerificationResponse
    suspend fun fetchBeneficiary(data : FieldMapData): INBeneficiaryResponse
    suspend fun serviceCharge(data : FieldMapData): INServiceChargeResponse
    suspend fun txnOtp(data : FieldMapData): INTxnOtpResponse

}