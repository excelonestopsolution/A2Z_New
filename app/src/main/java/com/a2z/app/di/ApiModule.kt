package com.a2z.app.di

import com.a2z.app.data.network.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providerTransactionService(@Named(DIConstant.RETROFIT_TRANSACTION) client: Retrofit):
            TransactionService = client.create(TransactionService::class.java)

    @Provides
    @Singleton
    fun provideAuthService(@Named(DIConstant.RETROFIT_NORMAL) client: Retrofit): AuthService =
        client.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideUtilityService(@Named(DIConstant.RETROFIT_NORMAL) client: Retrofit): UtilityService =
        client.create(UtilityService::class.java)

    @Provides
    @Singleton
    fun provideAppService(@Named(DIConstant.RETROFIT_NORMAL) client: Retrofit): AppService =
        client.create(AppService::class.java)

    @Provides
    @Singleton
    fun providerFundService(@Named(DIConstant.RETROFIT_NORMAL) client: Retrofit): FundService =
        client.create(FundService::class.java)

    @Provides
    @Singleton
    fun providerReportService(@Named(DIConstant.RETROFIT_NORMAL) client: Retrofit): ReportService =
        client.create(ReportService::class.java)
    @Provides
    @Singleton
    fun providerAepsService(@Named(DIConstant.RETROFIT_NORMAL) client: Retrofit): AepsService =
        client.create(AepsService::class.java)
    @Provides
    @Singleton
    fun providerMatmService(@Named(DIConstant.RETROFIT_NORMAL) client: Retrofit): MatmService =
        client.create(MatmService::class.java)

    @Provides
    @Singleton
    fun providerDMTService(@Named(DIConstant.RETROFIT_NORMAL) client: Retrofit): DMTService =
        client.create(DMTService::class.java)

}
