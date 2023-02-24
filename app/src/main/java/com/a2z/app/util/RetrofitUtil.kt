package com.a2z.app.util

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object RetrofitUtil {

    fun fileToMultipart(file: File?, param: String): MultipartBody.Part? {
        fun getRequestBody(file: File?) =
            file?.asRequestBody("multipart/form-data".toMediaTypeOrNull())


        fun getMultipartFormData(file: File?, fileField: String): MultipartBody.Part? {
            val requestBody = getRequestBody(file) ?: return null
            return MultipartBody.Part.createFormData(fileField, file?.name, requestBody)
        }

        return if (param.isNotEmpty())
            getMultipartFormData(file, param)
        else null

    }
}