package com.zohointerviewtest.zohopeople.detailscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zohointerviewtest.zohopeople.data.WeatherRepository
import com.zohointerviewtest.zohopeople.data.local.weather.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    val currentWeather: LiveData<Weather> get() = _currentWeather
    private val _currentWeather: MutableLiveData<Weather> = MutableLiveData()

    fun getLatestWeather(coordinates: String) = viewModelScope.launch(Dispatchers.IO) {
        val weather = weatherRepository.getWeatherForGivenCoordinates(coordinates)
        _currentWeather.postValue(weather)
    }
}