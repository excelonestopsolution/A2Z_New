package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.*
import com.a2z.app.util.FieldMapData

interface DMTRepository {
    suspend fun searchMobileNumberWalletOne(data: FieldMapData): MoneySenderResponse
    suspend fun searchMobileNumberWalletTwo(data: FieldMapData): MoneySenderResponse
    suspend fun searchMobileNumberWalletThree(data: FieldMapData): MoneySenderResponse
    suspend fun searchAccountNumber(data: FieldMapData): SenderAccountDetailResponse
    suspend fun registerSender(data: FieldMapData): SenderRegistrationResponse
    suspend fun resendSenderRegistrationOtp(data: FieldMapData): SenderRegistrationResponse
    suspend fun verifySender(data: FieldMapData): AppResponse
    suspend fun verifyAndUpdateSender(data: FieldMapData): AppResponse
    suspend fun beneficiaryList(data: FieldMapData): BeneficiaryListResponse
    suspend fun bankList(data: FieldMapData): BankListResponse
    suspend fun addBeneficiary(data: FieldMapData): AppResponse
    suspend fun accountValidation(data: FieldMapData): AccountVerify
    suspend fun accountReValidation(data: FieldMapData): AccountVerify
    suspend fun deleteBeneficiary(data: FieldMapData): AppResponse
    suspend fun deleteBeneficiaryConfirm(data: FieldMapData): AppResponse
    suspend fun commissionCharge(data: FieldMapData): DmtCommissionResponse
    suspend fun bankDownCheck(data: FieldMapData): BankDownCheckResponse
    suspend fun bankDown(): BankDownResponse

}