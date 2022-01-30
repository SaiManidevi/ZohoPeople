package com.zohointerviewtest.zohopeople.data.local.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class Weather(
    @PrimaryKey
    val city: String,
    val celsius: Int,
    val fahrenheit: Int,
    val aqi_index: Int,
    val icon_url: String
)
