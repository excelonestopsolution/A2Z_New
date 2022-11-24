package com.a2z.app.util.interceptor

import com.a2z.app.data.local.AppPreference
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val appPreference: AppPreference
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("user-id", appPreference.user?.id.toString())
                .addHeader("userId", appPreference.user?.id.toString())
                .addHeader("token", appPreference.user?.token.toString())
                .addHeader("Connection", "close")
                .addHeader("Accept-Encoding", "identity")
                .build()
        )
    }
}