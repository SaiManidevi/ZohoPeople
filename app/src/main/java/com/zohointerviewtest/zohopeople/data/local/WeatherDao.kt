package com.zohointerviewtest.zohopeople.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather LIMIT 1")
    fun getWeather(): Flow<Weather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWeather(currentWeather: Weather)

    @Query("DELETE FROM weather")
    suspend fun clearOldWeatherData()
}