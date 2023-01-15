package com.a2z.app.data.repository

import com.a2z.app.data.model.AgreementInitialInfoResponse
import com.a2z.app.data.model.AgreementStartResponse
import com.a2z.app.data.model.AppResponse
import com.a2z.app.util.FieldMapData

interface AgreementRepository {


    suspend fun fetchInitialAgreement(): AgreementInitialInfoResponse

    suspend fun fetchInitialIrctcAgreement(): AgreementInitialInfoResponse

    suspend fun generateAgreementPdf(): AppResponse

    suspend fun generateIrctcPdf() : AppResponse

    suspend fun startAgreement(fieldMap: FieldMapData): AgreementStartResponse

    suspend fun startIrctcAgreement(fieldMap: FieldMapData): AgreementStartResponse

    suspend fun checkStatus(fieldMap: FieldMapData): AppResponse

    suspend fun checkStatusIrctc(fieldMap: FieldMapData): AppResponse

    suspend fun agreementDownload(fieldMap: FieldMapData): AppResponse

    suspend fun agreementIrctcDownload(fieldMap: FieldMapData): AppResponse

    suspend fun agreementDownloadReport(fieldMap: FieldMapData): AppResponse

    suspend fun agreementDownloadReportIrctc(fieldMap: FieldMapData): AppResponse

}