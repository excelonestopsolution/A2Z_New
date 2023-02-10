package com.di_md.a2z.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.di_md.a2z.AppPreference
import com.di_md.a2z.database.DBHelper
import com.di_md.a2z.util.VolleyClient
import com.di_md.a2z.util.apis.ApiInterceptor
import com.di_md.a2z.util.apis.NetworkConnection
import com.di_md.a2z.util.ui.ContextInjectUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providerOldAppPreference(@ApplicationContext context : Context): AppPreference =
         AppPreference.getInstance(context)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context : Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)


    @Provides
    @Singleton
    fun provideNetworkConnection(@ApplicationContext context : Context) : NetworkConnection {
        return NetworkConnection(context)
    }

    @Provides
    @Singleton
    fun provideApiInterceptor(appPreference: AppPreference, networkConnection: NetworkConnection) : ApiInterceptor {
        return ApiInterceptor(appPreference,networkConnection)
    }


    @Provides
    @Singleton
    fun provideLocalDB(@ApplicationContext context: Context) : DBHelper {
        return DBHelper(context)
    }


    @Provides
    @Singleton
    fun provideVolleyClient(@ApplicationContext context : Context,appPreference: AppPreference) : VolleyClient {
        return VolleyClient(context,appPreference)
    }

    @Provides
    @Singleton
    fun provideContextInjectUtil(@ApplicationContext context: Context): ContextInjectUtil{
        return ContextInjectUtil(context);
    }
}