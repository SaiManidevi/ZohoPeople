package com.zohointerviewtest.zohopeople.utils

import android.content.Context
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.math.RoundingMode
import java.text.DecimalFormat

object Utils {
    private val currentLocality = MutableLiveData<String>()

    fun getCoordinateString(latitude: Double?, longitude: Double?): String {
        return if (latitude != null && longitude != null)
            "%.5f".format(latitude).plus(",").plus("%.5f".format(longitude))
        else ""
    }

    fun getCityFromCoordinates(
        context: Context,
        latitude: Double,
        longitude: Double
    ): LiveData<String> {
        val geocoder = Geocoder(context)
        val currentLocation = geocoder.getFromLocation(longitude, latitude, 1)
        currentLocality.value = currentLocation.first().locality
        return currentLocality
    }

    fun getAirQualityMeasure(pm10: Int): String {
        return when (pm10) {
            in 0..50 -> "Good"
            in 51..100 -> "Satisfactory"
            in 101..250 -> "Moderately Polluted"
            in 251..350 -> "Poor"
            in 351..430 -> "Very Poor"
            else -> "Severe"
        }
    }

    /**
     * Since icon url from WeatherApi is provided without https, Eg: - //cdn.weatherapi.com/weather/64x64/night/143.png
     * Format the url and add the https to avoid issues with Glide
     */
    fun getIconUrl(iconUrl: String): String = "https:".plus(iconUrl)

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                }
            }
            return false
        } else {
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }
}