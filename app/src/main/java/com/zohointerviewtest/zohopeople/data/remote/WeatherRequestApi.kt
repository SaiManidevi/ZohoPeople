package com.zohointerviewtest.zohopeople.data.remote

interface WeatherRequestApi {
    // BASE:  http://api.weatherapi.com/v1
    // https://api.weatherapi.com/v1/current.json?key=1e8e00e7b0c84d5bbfc25643222601&q=13.11711,80.23768&aqi=yes
    suspend fun getCurrentWeather()
}