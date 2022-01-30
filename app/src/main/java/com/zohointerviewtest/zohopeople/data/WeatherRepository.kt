package com.zohointerviewtest.zohopeople.data

import android.util.Log
import com.zohointerviewtest.zohopeople.data.local.weather.Weather
import com.zohointerviewtest.zohopeople.data.local.weather.WeatherDao
import com.zohointerviewtest.zohopeople.data.remote.weather.WeatherApiHelper
import com.zohointerviewtest.zohopeople.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApiHelper: WeatherApiHelper,
    private val weatherDao: WeatherDao
) {
    // Database is the single source of truth - any latest updates are pushed to DB & then emitted
    val currentWeather: Flow<Weather> = weatherDao.getWeather()

    /**
     * This method tried to get the latest data from the Network server
     * If succeeded, first push fresh data to DB and then access it.
     * If failed, returns a [NetworkResult.Error]
     */
    suspend fun loadLatestWeather(
        internetAvailable: Boolean,
        coordinates: String
    ): NetworkResult<Flow<Weather>> {
        if (internetAvailable) {
            try {
                getWeatherFromRemoteServer(coordinates)
            } catch (e: Exception) {
                return NetworkResult.Error(e)
            }
        }
        return getWeatherFromDB()
    }

    private suspend fun getWeatherFromRemoteServer(coordinates: String) {
        Log.d("TEST", "getWeatherFromRemoteServer: called")
        val latestWeatherResponse = weatherApiHelper.getCurrentWeather(
            Constants.KEY,
            coordinateString = coordinates,
            addAirQuality = "YES"
        )
        if (latestWeatherResponse.isSuccessful) {
            val latestWeather = latestWeatherResponse.body()
            latestWeather?.let {
                val currentWeather = Weather(
                    city = it.location.name,
                    celsius = it.current.temp_c.toInt(),
                    fahrenheit = it.current.temp_f.toInt(),
                    aqi_index = it.current.air_quality.pm10.toInt(),
                    icon_url = it.current.condition.icon
                )
                // If weather response from api is successful, then save it to DB
                weatherDao.saveWeather(currentWeather)
                Log.d("TEST", "Weather from remote server successful")
            }
        }
    }

    private fun getWeatherFromDB(): NetworkResult<Flow<Weather>> {
        return try {
            NetworkResult.Success(weatherDao.getWeather())
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}