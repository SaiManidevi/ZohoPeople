package com.zohointerviewtest.zohopeople.data.remote

import com.zohointerviewtest.zohopeople.models.weatherapi.WeatherResult
import retrofit2.Response

interface WeatherApiHelper {
    suspend fun getCurrentWeather(
        key: String,
        coordinateString: String,
        addAirQuality: String
    ): Response<WeatherResult>
}