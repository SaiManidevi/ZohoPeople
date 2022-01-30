package com.zohointerviewtest.zohopeople.data.remote.weather

import com.zohointerviewtest.zohopeople.models.weatherapi.WeatherResult
import com.zohointerviewtest.zohopeople.utils.Constants.PATH_CURRENT_WEATHER_JSON
import com.zohointerviewtest.zohopeople.utils.Constants.QUERY_PARAM_WEATHER
import com.zohointerviewtest.zohopeople.utils.Constants.QUERY_PARAM_WEATHER_AQI
import com.zohointerviewtest.zohopeople.utils.Constants.QUERY_PARAM_WEATHER_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET(PATH_CURRENT_WEATHER_JSON)
    suspend fun getCurrentWeather(
        @Query(QUERY_PARAM_WEATHER_KEY) keyString: String,
        @Query(QUERY_PARAM_WEATHER) coordinateString: String,
        @Query(QUERY_PARAM_WEATHER_AQI) addAirQuality: String
    ): Response<WeatherResult>
}