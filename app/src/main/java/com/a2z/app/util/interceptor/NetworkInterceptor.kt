package com.a2z.app.util.interceptor

import com.a2z.app.util.Exceptions
import com.a2z.app.util.NetworkUtil
import kotlinx.coroutines.delay
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import kotlin.concurrent.thread

class NetworkInterceptor @Inject constructor(
    private val networkUtil: NetworkUtil
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkUtil.isInternetAvailable()) {

            Thread.sleep(1000)
            throw Exceptions.NoInternetException("No Internet connection available")
        }
        return chain.proceed(chain.request())
    }

}