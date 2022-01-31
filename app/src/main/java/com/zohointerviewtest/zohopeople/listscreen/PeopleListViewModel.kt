package com.zohointerviewtest.zohopeople.listscreen

import android.location.Location
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.zohointerviewtest.zohopeople.data.LocationRepository
import com.zohointerviewtest.zohopeople.data.PeopleRepository
import com.zohointerviewtest.zohopeople.data.WeatherRepository
import com.zohointerviewtest.zohopeople.data.local.weather.Weather
import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.PeopleResult
import com.zohointerviewtest.zohopeople.utils.Event
import com.zohointerviewtest.zohopeople.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleListViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
    private val peopleRepository: PeopleRepository
) : ViewModel() {
    private var _currentLocation: MutableLiveData<Location> = locationRepository.getLocation()
    val currentLocation: LiveData<Location> get() = _currentLocation
    private val deviceInternetStatus: MutableLiveData<Boolean> = MutableLiveData(
        DEFAULT_INTERNET_STATUS
    )
    val currentWeather: LiveData<Weather> = currentLocation.switchMap {
        getLatestWeather(
            isInternetAvailable = deviceInternetStatus.value ?: DEFAULT_INTERNET_STATUS
        )
        weatherRepository.currentWeather
            .onStart {
                emit(LOADING_WEATHER)
            }
            .onEmpty {
                emit(DEFAULT_WEATHER)
            }.asLiveData()
    }

    fun getZohoPeople(): Flow<PagingData<PeopleResult>> {
        return peopleRepository.getZohoPeople()
    }

    @ExperimentalPagingApi
    fun getZohoPeopleFromDb(): Flow<PagingData<PeopleResult>> {
        return peopleRepository.getZohoPeopleDb()
    }

    // State that observes/listens to Person item click
    private val _personClickEvent = MutableLiveData<Event<PeopleResult>>()
    val personClickEvent: LiveData<Event<PeopleResult>> get() = _personClickEvent

    fun onZohoPersonClick(peopleResult: PeopleResult) {
        _personClickEvent.value = Event(peopleResult)
    }

    /**
     * This function gets the latest weather if [deviceInternetStatus] is TRUE or
     * gets the saved weather data from DB
     */
    private fun getLatestWeather(isInternetAvailable: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            val location = currentLocation.value
            weatherRepository.loadLatestWeather(
                isInternetAvailable,
                Utils.getCoordinateString(
                    latitude = location?.latitude,
                    longitude = location?.longitude
                )
            )
        }

    /**
     * Helper function that updates the [_currentLocation] with the latest location updates
     * Sets the [deviceInternetStatus] as per the current internet status of the user's device
     * Calls the [getLatestWeather] function to update the [currentWeather] value
     */
    fun getLocation(isInternetAvailable: Boolean) {
        _currentLocation = locationRepository.getLocation()
        deviceInternetStatus.value = isInternetAvailable
        getLatestWeather(isInternetAvailable)
    }

    companion object {
        val DEFAULT_WEATHER: Weather = Weather("N/A", 0, 0, 0, "")
        val LOADING_WEATHER: Weather = Weather("loading..", 0, 0, 0, "")
        const val DEFAULT_INTERNET_STATUS = false
    }
}