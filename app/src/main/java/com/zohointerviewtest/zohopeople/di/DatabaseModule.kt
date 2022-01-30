package com.zohointerviewtest.zohopeople.di

import android.content.Context
import com.zohointerviewtest.zohopeople.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getAppDbInstance(context)
    }

    @Singleton
    @Provides
    fun provideWeatherDao(appDatabase: AppDatabase) = appDatabase.weatherDao()

    @Singleton
    @Provides
    fun providePersonDao(appDatabase: AppDatabase) = appDatabase.personDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(appDatabase: AppDatabase) = appDatabase.remoteKeysDao()
}