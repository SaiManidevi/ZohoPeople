package com.zohointerviewtest.zohopeople.utils

import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.math.RoundingMode
import java.text.DecimalFormat

class LocationUtils {
    private val currentLocality = MutableLiveData<String>()

    fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.#####")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toDouble()
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
}