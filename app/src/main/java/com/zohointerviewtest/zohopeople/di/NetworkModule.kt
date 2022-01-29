package com.zohointerviewtest.zohopeople.di

import android.app.Application
import android.content.Context
import com.zohointerviewtest.zohopeople.data.remote.WeatherApiHelper
import com.zohointerviewtest.zohopeople.data.remote.WeatherApiHelperImpl
import com.zohointerviewtest.zohopeople.data.remote.WeatherApiService
import com.zohointerviewtest.zohopeople.utils.Constants.WEATHER_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherApiService(retrofit: Retrofit) =
        retrofit.create(WeatherApiService::class.java)

    @Singleton
    @Provides
    fun provideWeatherApiHelper(weatherApiHelperImpl: WeatherApiHelperImpl): WeatherApiHelper =
        weatherApiHelperImpl
}