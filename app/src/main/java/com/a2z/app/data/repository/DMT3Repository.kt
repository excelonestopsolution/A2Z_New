package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.MoneySenderResponse
import com.a2z.app.data.model.dmt.SenderRegistrationResponse
import com.a2z.app.util.FieldMapData

interface DMT3Repository {

    suspend fun searchMobileNumberDmtThree(data: FieldMapData): MoneySenderResponse

    suspend fun registerSender(data: FieldMapData): SenderRegistrationResponse


    suspend fun verifySender(data: FieldMapData): AppResponse

    suspend fun resendSenderRegistrationOtp(data: FieldMapData): SenderRegistrationResponse

}