package com.a2z.app.util.interceptor

import com.a2z.app.util.Exceptions
import okhttp3.Interceptor
import okhttp3.Response

class ExceptionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        when (response.code) {
            200, 201 -> return response
            401, 403 -> throw Exceptions.UnAuthorizedException("Token expired or may you have logged " + "in another device, code : ${response.code}")
            404, 400, 405 -> throw Exceptions.InternalServerError("Api error ! please try to contact with admin with code : ${response.code}")
            500, 501, 502, 503 -> throw Exceptions.InternalServerError("Something went wrong! please try to contact with admin. code : ${500}")
            else -> throw Exceptions.InternalServerError("Api Error( ${response.message})-${response.code}")
        }
    }

}

