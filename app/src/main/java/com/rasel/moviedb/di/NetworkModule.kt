package com.paperflymerchantapp.di

import android.content.Context
import com.rasel.moviedb.data.network.MyApi
import com.rasel.moviedb.data.network.NetworkConnectionInterceptor
import com.rasel.moviedb.data.preference.AppPrefsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkConnectionInterceptor(@ApplicationContext context: Context, prefsManager: AppPrefsManager): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context, prefsManager)
    }


    @Singleton
    @Provides
    fun provideMyApi(networkConnectionInterceptor: NetworkConnectionInterceptor): MyApi {
        return MyApi(networkConnectionInterceptor)
    }
}
