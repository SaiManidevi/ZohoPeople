package com.zohointerviewtest.zohopeople

import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.zohointerviewtest.zohopeople.data.LocationRepository
import com.zohointerviewtest.zohopeople.data.PeopleRemotePagingSource
import com.zohointerviewtest.zohopeople.data.PeopleRepository
import com.zohointerviewtest.zohopeople.data.WeatherRepository
import com.zohointerviewtest.zohopeople.data.local.weather.Weather
import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.PeopleResult
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

    /*fun getZohoPeople(): Flow<PagingData<PeopleResult>> {
        return peopleRepository.getZohoPeople()

    }
*/
    fun getZohoPeople(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<PeopleResult>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PeopleRemotePagingSource(repository = peopleRepository) }
        ).flow
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = PeopleRepository.DEFAULT_PAGE_SIZE,
            enablePlaceholders = false
        )
    }

    /**
     * This function gets the latest weather if [deviceInternetStatus] is TRUE or
     * gets the saved weather data from DB
     */
    private fun getLatestWeather(isInternetAvailable: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("TEST", "getLatestWeather called internet status: $isInternetAvailable")
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