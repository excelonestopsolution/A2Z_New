package com.a2z.app.di

import android.content.Context
import com.a2z.app.data.NetworkClient
import com.a2z.app.data.local.AppPreference
import com.a2z.app.service.location.LocationService
import com.a2z.app.util.NetworkUtil
import com.a2z.app.util.interceptor.HeaderInterceptor
import com.a2z.app.util.interceptor.NetworkInterceptor
import com.a2z.app.util.interceptor.ParamInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppPreference(@ApplicationContext context: Context): AppPreference {
        return AppPreference(context)
    }



    @Provides
    @Singleton
    fun provideNetworkConnection(@ApplicationContext context: Context): NetworkUtil {
        return NetworkUtil(context)
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(
        appPreference: AppPreference,
    ): HeaderInterceptor {
        return HeaderInterceptor(appPreference)
    }


    @Provides
    @Singleton
    fun provideNetworkInterceptor(
        networkUtil: NetworkUtil
    ): NetworkInterceptor {
        return NetworkInterceptor(networkUtil)
    }


    @Provides
    @Singleton
    fun provideParamInterceptor(
        appPreference: AppPreference
    ): ParamInterceptor {
        return ParamInterceptor(appPreference)
    }


    @Provides
    @Singleton
    fun provideNetworkClient(
        headerInterceptor: HeaderInterceptor,
        networkInterceptor: NetworkInterceptor,
        paramInterceptor: ParamInterceptor,
    ): NetworkClient {
        return NetworkClient(
            headerInterceptor = headerInterceptor,
            networkInterceptor = networkInterceptor,
            paramInterceptor = paramInterceptor
        )
    }


    @Singleton
    @Provides
    @Named(DIConstant.RETROFIT_NORMAL)
    fun provideNormalRetrofitClient(networkClient: NetworkClient): Retrofit {
        return networkClient.normalClient()
    }


    @Singleton
    @Provides
    @Named(DIConstant.RETROFIT_TRANSACTION)
    fun provideTransactionRetrofitClient(networkClient: NetworkClient): Retrofit {
        return networkClient.transactionClient()
    }

    @Singleton
    @Provides
    fun provideLocationService(@ApplicationContext context: Context): LocationService {
        return LocationService(context)
    }


}