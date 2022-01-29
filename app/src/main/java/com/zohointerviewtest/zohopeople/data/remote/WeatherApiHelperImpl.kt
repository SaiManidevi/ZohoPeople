package com.zohointerviewtest.zohopeople.data.remote

import com.zohointerviewtest.zohopeople.models.weatherapi.WeatherResult
import retrofit2.Response
import javax.inject.Inject

class WeatherApiHelperImpl @Inject constructor(private val apiService: WeatherApiService) :
    WeatherApiHelper {
    override suspend fun getCurrentWeather(
        key: String,
        coordinateString: String,
        addAirQuality: String
    ): Response<WeatherResult> {
        return apiService.getCurrentWeather(key, coordinateString, addAirQuality)
    }
}