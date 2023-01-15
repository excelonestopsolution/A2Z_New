package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.AgreementInitialInfoResponse
import com.a2z.app.data.model.AgreementStartResponse
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.network.AgreementService
import com.a2z.app.data.repository.AgreementRepository
import com.a2z.app.util.FieldMapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AgreementRepositoryImpl @Inject constructor(private val service: AgreementService) :
    AgreementRepository {
    override suspend fun fetchInitialAgreement(): AgreementInitialInfoResponse {
        return service.fetchInitialAgreement()
    }

    override suspend fun fetchInitialIrctcAgreement(): AgreementInitialInfoResponse {
       return service.fetchInitialIrctcAgreement()
    }

    override suspend fun generateAgreementPdf(): AppResponse {
        return service.generateAgreementPdf()
    }

    override suspend fun generateIrctcPdf(): AppResponse {
        return service.generateIrctcPdf()
    }

    override suspend fun startAgreement(fieldMap: FieldMapData): AgreementStartResponse {
        return service.startAgreement(fieldMap)
    }

    override suspend fun startIrctcAgreement(fieldMap: FieldMapData): AgreementStartResponse {
        return service.startIrctcAgreement(fieldMap)
    }

    override suspend fun checkStatus(fieldMap: FieldMapData): AppResponse {
        return service.checkStatus(fieldMap)
    }

    override suspend fun checkStatusIrctc(fieldMap: FieldMapData): AppResponse {
       return service.checkStatusIrctc(fieldMap)
    }

    override suspend fun agreementDownload(fieldMap: FieldMapData): AppResponse {
        return service.agreementDownload(fieldMap)
    }

    override suspend fun agreementIrctcDownload(fieldMap: FieldMapData): AppResponse {
        return service.agreementIrctcDownload(fieldMap)
    }

    override suspend fun agreementDownloadReport(fieldMap: FieldMapData): AppResponse {
        return service.agreementDownloadReport(fieldMap)
    }

    override suspend fun agreementDownloadReportIrctc(fieldMap: FieldMapData): AppResponse {
        return service.agreementDownloadReportIrctc(fieldMap)
    }
}