package com.zohointerviewtest.zohopeople.di

import android.app.Application
import android.content.Context
import com.zohointerviewtest.zohopeople.data.remote.weather.WeatherApiHelper
import com.zohointerviewtest.zohopeople.data.remote.weather.WeatherApiHelperImpl
import com.zohointerviewtest.zohopeople.data.remote.weather.WeatherApiService
import com.zohointerviewtest.zohopeople.data.remote.zohopeople.PeopleApiHelper
import com.zohointerviewtest.zohopeople.data.remote.zohopeople.PeopleApiHelperImpl
import com.zohointerviewtest.zohopeople.data.remote.zohopeople.PeopleApiService
import com.zohointerviewtest.zohopeople.utils.Constants.PEOPLE_BASE_URL
import com.zohointerviewtest.zohopeople.utils.Constants.WEATHER_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
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
    @Named("weatherRetrofit")
    fun provideWeatherRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @Named("peopleRetrofit")
    fun providePeopleRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PEOPLE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherApiService(@Named("weatherRetrofit") retrofit: Retrofit): WeatherApiService =
        retrofit.create(WeatherApiService::class.java)

    @Singleton
    @Provides
    fun provideWeatherApiHelper(weatherApiHelperImpl: WeatherApiHelperImpl): WeatherApiHelper =
        weatherApiHelperImpl

    @Singleton
    @Provides
    fun providePeopleApiService(@Named("peopleRetrofit") retrofit: Retrofit): PeopleApiService =
        retrofit.create(PeopleApiService::class.java)

    @Singleton
    @Provides
    fun providePeopleApiHelper(peopleApiHelperImpl: PeopleApiHelperImpl): PeopleApiHelper =
        peopleApiHelperImpl
}