package com.paperflymerchantapp.di

import android.content.Context
import com.paperflymerchantapp.data.db.AppDatabase
import com.rasel.moviedb.data.preference.AppPrefsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.invoke(context)
    }


    @Provides
    fun providePreferenceProvider(@ApplicationContext context: Context): AppPrefsManager {
        return AppPrefsManager(context)
    }
}
