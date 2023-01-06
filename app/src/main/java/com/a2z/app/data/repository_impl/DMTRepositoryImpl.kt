package com.a2z.app.data.repository_impl

import com.a2z.app.data.network.DMTService
import com.a2z.app.data.repository.DMTRepository
import com.a2z.app.util.FieldMapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DMTRepositoryImpl @Inject constructor(private val service: DMTService) :
    DMTRepository {

    override suspend fun searchMobileNumberWalletOne(data: FieldMapData) =
        service.searchMobileNumberWalletOne(data)

    override suspend fun searchMobileNumberWalletTwo(data: FieldMapData) =
        service.searchMobileNumberWalletTwo(data)

    override suspend fun searchMobileNumberWalletThree(data: FieldMapData) =
        service.searchMobileNumberWalletThree(data)

    override suspend fun searchAccountNumber(data: FieldMapData) =
        service.searchAccountNumber(data)

    override suspend fun registerSender(data: FieldMapData) = service.registerSender(data)
    override suspend fun resendSenderRegistrationOtp(data: FieldMapData) =
        service.resendSenderRegistrationOtp(data)

    override suspend fun verifySender(data: FieldMapData) = service.verifySender(data)
    override suspend fun verifyAndUpdateSender(data: FieldMapData) =
        service.verifyAndUpdateSender(data)

    override suspend fun beneficiaryList(data: FieldMapData) = service.beneficiaryList(data)
    override suspend fun bankList(data: FieldMapData) = service.bankList(data)
    override suspend fun addBeneficiary(data: FieldMapData) = service.addBeneficiary(data)
    override suspend fun accountValidation(data: FieldMapData) = service.accountValidation(data)
    override suspend fun accountReValidation(data: FieldMapData) = service.accountReValidation(data)
    override suspend fun deleteBeneficiary(data: FieldMapData) = service.deleteBeneficiary(data)
    override suspend fun deleteBeneficiaryConfirm(data: FieldMapData) =
        service.deleteBeneficiaryConfirm(data)

    override suspend fun commissionCharge(data: FieldMapData) = service.commissionCharge(data)
    override suspend fun bankDownCheck(data: FieldMapData) = service.bankDownCheck(data)
    override suspend fun transfer(data: FieldMapData) = service.transfer(data)
    override suspend fun bankDown() = service.bankDown()


}