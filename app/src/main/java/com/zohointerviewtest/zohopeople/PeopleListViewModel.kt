package com.zohointerviewtest.zohopeople

import android.location.Location
import androidx.lifecycle.*
import com.zohointerviewtest.zohopeople.data.LocationRepository
import com.zohointerviewtest.zohopeople.data.WeatherRepository
import com.zohointerviewtest.zohopeople.data.local.Weather
import com.zohointerviewtest.zohopeople.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleListViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _currentLocation: MutableLiveData<Location> = MutableLiveData()
    val currentLocation: LiveData<Location> get() = _currentLocation
    val currentWeather: LiveData<Weather> = currentLocation.switchMap {
        weatherRepository.currentWeather
            .onStart {
                emit(LOADING_WEATHER)
            }
            .onEmpty {
                emit(DEFAULT_WEATHER)
            }.asLiveData()
    }

    fun getLatestWeather(isInternetAvailable: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        val location = currentLocation.value
        weatherRepository.loadLatestWeather(
            isInternetAvailable,
            Utils.getCoordinateString(
                latitude = location?.latitude,
                longitude = location?.longitude
            )
        )
    }

    fun getLocation() {
        _currentLocation.value = locationRepository.getLocation().value
    }

    companion object {
        val DEFAULT_WEATHER: Weather = Weather("N/A", 0, 0, 0, "")
        val LOADING_WEATHER: Weather = Weather("loading..", 0, 0, 0, "")
    }
}