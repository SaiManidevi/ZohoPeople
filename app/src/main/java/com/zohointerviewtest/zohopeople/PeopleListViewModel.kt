package com.zohointerviewtest.zohopeople

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zohointerviewtest.zohopeople.data.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PeopleListViewModel @Inject constructor(repository: LocationRepository) : ViewModel() {
    val currentLocation: LiveData<Location> = repository.getLocation()

    /*val currentLocality: LiveData<String> = currentLocation.switchMap { location ->
        LocationUtils().getCityFromCoordinates(
            context,
            latitude = location.latitude,
            longitude = location.longitude
        )
    }*/

}