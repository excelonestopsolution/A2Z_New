package com.a2z.app.util.interceptor

import com.a2z.app.data.local.AppPreference
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class ParamInterceptor @Inject constructor(private val appPreference: AppPreference) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url =
            request.url.newBuilder()
                .addQueryParameter("user-id", appPreference.user?.id.toString())
                .addQueryParameter("userId", appPreference.user?.id.toString())
                .addQueryParameter("token", appPreference.user?.token.toString())
                .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }

}