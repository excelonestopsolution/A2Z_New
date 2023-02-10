package com.di_md.a2z.data.repository

import com.di_md.a2z.data.service.AuthService
import com.di_md.a2z.util.ents.FieldMapData
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authService: AuthService) {

    suspend fun login(
        case: String,
        password: String,
        mobileNumber: String,
        deviceToken: String,
        hardwareSerialNumber: String,
        deviceName: String,
        imei: String,
        latitude: String,
        longitude: String,
    ) = authService.login(

        case = case,
        password = password,
        mobileNumber = mobileNumber,
        deviceToken = deviceToken,
        hardwareSerialNumber = hardwareSerialNumber,
        deviceName = deviceName,
        imei = imei,
        latitude = latitude,
        longitude = longitude,
    )

    suspend fun createLoginId(data: FieldMapData) =  authService.createLoginId(data)
    suspend fun verifyLoginId(data: FieldMapData) =  authService.verifyLoginId(data)
    suspend fun forgotLoginId(data: FieldMapData) =  authService.forgotLoginId(data)
    suspend fun forgotLoginIdVerify(data: FieldMapData) =  authService.forgotLoginIdVerify(data)

}