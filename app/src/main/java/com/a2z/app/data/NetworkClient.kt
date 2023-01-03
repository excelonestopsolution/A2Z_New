package com.a2z.app.data

import com.a2z.app.BuildConfig
import com.a2z.app.util.AppConstant
import com.a2z.app.util.interceptor.*
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NetworkClient @Inject constructor(
    private val headerInterceptor: HeaderInterceptor,
    private val networkInterceptor: NetworkInterceptor,
    private val paramInterceptor: ParamInterceptor,
    private val responseInterceptor: ResponseInterceptor,
) {

    fun normalClient() = networkClient(true)

    fun transactionClient() = networkClient(false)

    private fun networkClient(normalClient: Boolean): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstant.BASE_URL)
            .addConverterFactory(gsonConverterFactory())
            .client(okHttpClient(normalClient))
            .build()
    }

    private fun okHttpClient(normalClient: Boolean): OkHttpClient {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okkHttpClient = OkHttpClient.Builder()
        okkHttpClient
            .addInterceptor(headerInterceptor)
            .addInterceptor(paramInterceptor)
            .addInterceptor(networkInterceptor)
            .addInterceptor(ExceptionInterceptor())
            .addInterceptor(responseInterceptor)
            .connectTimeout(0, TimeUnit.MINUTES)
            .readTimeout(0, TimeUnit.MINUTES)
            .writeTimeout(0, TimeUnit.MINUTES)
            .retryOnConnectionFailure(normalClient)
        if (BuildConfig.DEBUG) okkHttpClient.addInterceptor(logger)

        return okkHttpClient.build()
    }

    private fun gsonConverterFactory() = GsonConverterFactory.create(
        GsonBuilder().serializeNulls().create()
    )


}