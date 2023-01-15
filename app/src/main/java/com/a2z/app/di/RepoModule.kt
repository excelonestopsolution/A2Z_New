package com.a2z.app.di

import com.a2z.app.data.repository.*
import com.a2z.app.data.repository_impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepoModule {
    @Binds
    fun provideMainRepositoryImpl(repository: AuthRepositoryImpl): AuthRepository
    @Binds
    fun provideUtilityRepositoryImpl(repository: UtilityRepositoryIml): UtilityRepository

    @Binds
    fun provideAppRepositoryImpl(repository: AppRepositoryImpl): AppRepository

    @Binds
    fun provideFundRepositoryImpl(repository: FundRepositoryImpl): FundRepository
    @Binds
    fun provideTransactionRepositoryImpl(repository: TransactionRepositoryImpl): TransactionRepository

    @Binds
    fun provideReportRepositoryImpl(repository: ReportRepositoryImpl): ReportRepository

    @Binds
    fun provideAepsRepositoryImpl(repository: AepsRepositoryImpl): AepsRepository

    @Binds
    fun provideMatmRepositoryImpl(repository: MatmRepositoryImpl): MatmRepository

    @Binds
    fun provideDMTRepositoryImpl(repository: DMTRepositoryImpl): DMTRepository

    @Binds
    fun provideDMT3RepositoryImpl(repository: DMT3RepositoryImpl): DMT3Repository

    @Binds
    fun provideUpiRepositoryImpl(repository: UpiRepositoryIml): UpiRepository


    @Binds
    fun provideKycRepositoryImpl(repository: KycRepositoryImpl): KycRepository
    @Binds
    fun provideAgreementRepositoryImpl(repository: AgreementRepositoryImpl): AgreementRepository
}