package com.a2z.app.di

import android.content.Context
import com.a2z.app.data.local.AppPreference
import com.a2z.app.util.AppUtil
import com.a2z.app.util.AppUtilDI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Provides
    @Singleton
    fun provideAppUtilDI(appPreference: AppPreference): AppUtilDI = AppUtilDI(appPreference)

}