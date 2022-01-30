package com.zohointerviewtest.zohopeople.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zohointerviewtest.zohopeople.data.local.weather.Weather
import com.zohointerviewtest.zohopeople.data.local.weather.WeatherDao
import com.zohointerviewtest.zohopeople.data.local.zohopeople.*
import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.PeopleResult

@Database(
    entities = [Weather::class, PeopleResult::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun personDao(): PersonDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getAppDbInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: createDbInstance(context).also { INSTANCE = it }
            }
        }

        private fun createDbInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "zohopeople_db"
            ).build()
        }
    }
}