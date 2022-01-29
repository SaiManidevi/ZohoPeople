package com.zohointerviewtest.zohopeople.di

import android.content.Context
import androidx.room.Room
import com.zohointerviewtest.zohopeople.data.local.WeatherDatabase
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
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase{
        return WeatherDatabase.getWeatherDbInstance(context)
    }

    @Singleton
    @Provides
    fun provideWeatherDao(weatherDatabase: WeatherDatabase) = weatherDatabase.weatherDao()
}