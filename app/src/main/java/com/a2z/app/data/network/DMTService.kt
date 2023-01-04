package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.dmt.*
import com.a2z.app.data.model.matm.MaPosAmountLimitResponse
import com.a2z.app.util.FieldMapData
import retrofit2.Response
import retrofit2.http.*

interface DMTService {

    @GET("a2z/plus/wallet/sender/search")
    suspend fun searchMobileNumberWalletOne(@QueryMap data: FieldMapData):
            MoneySenderResponse

    @GET("a2z/plus/wallet-two/sender/search")
    suspend fun searchMobileNumberWalletTwo(@QueryMap data: FieldMapData):
            MoneySenderResponse

    @GET("a2z/plus/wallet-three/sender/search")
    suspend fun searchMobileNumberWalletThree(@QueryMap data: FieldMapData):
            MoneySenderResponse

    @GET("a2z/plus/wallet/account-search")
    suspend fun searchAccountNumber(@QueryMap data: FieldMapData):
            SenderAccountDetailResponse

    @POST("a2z/plus/wallet/sender/registration")
    @FormUrlEncoded
    suspend fun registerSender(@FieldMap data: FieldMapData):
            SenderRegistrationResponse

    @GET("a2z/plus/wallet/sender/verification/resendotp")
    suspend fun resendSenderRegistrationOtp(@QueryMap data: FieldMapData):
            SenderRegistrationResponse

    @POST("a2z/plus/wallet/sender/verification")
    @FormUrlEncoded
    suspend fun verifySender(@FieldMap data: FieldMapData):
            AppResponse

    @POST("a2z/plus/wallet/sender/updateAndVerification")
    @FormUrlEncoded
    suspend fun verifyAndUpdateSender(@FieldMap data: FieldMapData):
            AppResponse


    @GET("a2z/plus/wallet/bene-list")
    suspend fun beneficiaryList(@QueryMap data: FieldMapData):
            BeneficiaryListResponse

    @GET("a2z/plus/wallet/bank-list")
    suspend fun bankList(@QueryMap data: FieldMapData):
            BankListResponse

    @POST("a2z/plus/wallet/add-beneficiary")
    @FormUrlEncoded
    suspend fun addBeneficiary(@FieldMap data: FieldMapData):
            AppResponse

    @POST("a2z/plus/wallet/account/validation")
    @FormUrlEncoded
    suspend fun accountValidation(@FieldMap data: FieldMapData):
            AccountVerify

    @POST("a2z/plus/wallet/account/re-validation")
    @FormUrlEncoded
    suspend fun accountReValidation(@FieldMap data: FieldMapData):
            AccountVerify


    @POST("a2z/plus/wallet/bene-delete")
    @FormUrlEncoded
    suspend fun deleteBeneficiary(@FieldMap data: FieldMapData): AppResponse

    @POST("a2z/plus/wallet/bene-delete/confirm")
    @FormUrlEncoded
    suspend fun deleteBeneficiaryConfirm(@FieldMap data: FieldMapData): AppResponse

    @GET("a2z/plus/wallet/charge-commission")
    suspend fun commissionCharge(@QueryMap data: FieldMapData): DmtCommissionResponse

    @GET("a2z/plus/wallet/check-bank-down")
    suspend fun bankDownCheck(@QueryMap data: FieldMapData): BankDownCheckResponse

    @GET("a2z/plus/wallet/bank-down")
    suspend fun bankDown(): BankDownResponse

    @POST("a2z/plus/wallet/transaction")
    @FormUrlEncoded
    suspend fun transfer(@FieldMap data: FieldMapData): AppResponse

}
