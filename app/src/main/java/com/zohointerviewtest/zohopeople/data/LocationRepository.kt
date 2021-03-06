package com.zohointerviewtest.zohopeople.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(context: Context) {
    private val currentLocation = MutableLiveData<Location>()
    private lateinit var locationCallback: LocationCallback
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Since the getLocation is called only after checking that location permission is granted
    // Using - @SuppressLint("MissingPermission")
    @SuppressLint("MissingPermission")
    fun getLocation(): MutableLiveData<Location> {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    currentLocation.value = location
                } else {
                    // If last known location is null, request for current location
                    val locationRequest = LocationRequest.create()
                    locationRequest.priority = PRIORITY_HIGH_ACCURACY
                    locationCallback = object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            for (resultLocation in result.locations) {
                                resultLocation?.let {
                                    currentLocation.value = it
                                }
                            }
                            // After receiving the current location, remove location update request
                            removeLocationRequest()
                        }
                    }
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            }
        return currentLocation
    }

    private fun removeLocationRequest() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

}