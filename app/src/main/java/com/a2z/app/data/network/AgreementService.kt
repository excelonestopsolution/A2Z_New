package com.a2z.app.data.network

import com.a2z.app.data.model.AgreementInitialInfoResponse
import com.a2z.app.data.model.AgreementStartResponse
import com.a2z.app.data.model.AppResponse
import com.a2z.app.util.FieldMapData
import retrofit2.http.*


interface AgreementService {


    @GET("agreement")
    suspend fun fetchInitialAgreement(): AgreementInitialInfoResponse

    @GET("agreement/irctc")
    suspend fun fetchInitialIrctcAgreement(): AgreementInitialInfoResponse

    @GET("agreement/e-sign")
    suspend fun generateAgreementPdf(): AppResponse
    @GET("agreement/irctc/generate-pdf")
    suspend fun generateIrctcPdf() : AppResponse

    @POST("agreement/start")
    @FormUrlEncoded
    suspend fun startAgreement(
        @FieldMap fieldMap: FieldMapData
    ): AgreementStartResponse


    @POST("agreement/irctc/start")
    @FormUrlEncoded
    suspend fun startIrctcAgreement(
        @FieldMap fieldMap: FieldMapData
    ): AgreementStartResponse

    @POST("agreement/e-sign/checkstatus")
    @FormUrlEncoded
    suspend fun checkStatus(
        @FieldMap fieldMap: FieldMapData
    ): AppResponse
    @POST("agreement/irctc/checkstatus")
    @FormUrlEncoded
    suspend fun checkStatusIrctc(
        @FieldMap fieldMap: FieldMapData
    ): AppResponse


    @POST("agreement/e-sign/download")
    @FormUrlEncoded
    suspend fun agreementDownload(
        @FieldMap fieldMap: FieldMapData
    ): AppResponse
    @POST("agreement/irctc/getsigneddocument")
    @FormUrlEncoded
    suspend fun agreementIrctcDownload(
        @FieldMap fieldMap: FieldMapData
    ): AppResponse


    @POST("agreement/get/report")
    @FormUrlEncoded
    suspend fun agreementDownloadReport(
        @FieldMap fieldMap: FieldMapData
    ): AppResponse
    @POST("agreement/irctc/get/report")
    @FormUrlEncoded
    suspend fun agreementDownloadReportIrctc(
        @FieldMap fieldMap: FieldMapData
    ): AppResponse


}