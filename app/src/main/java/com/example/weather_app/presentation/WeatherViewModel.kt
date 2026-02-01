package com.example.weather_app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.repository.WeatherResult
import com.example.weather_app.domain.repository.WeatherRepository
import com.example.weather_app.presentation.state.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState
    init {
        loadWeather(
            city = "Astana",
            lat = 51.1694,
            lon = 71.4491
        )
    }
    fun loadWeather(city: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState(isLoading = true)

            when (val result = repository.getWeather(city, lat, lon)) {

                is WeatherResult.Success -> {
                    _uiState.value = WeatherUiState(
                        weather = result.weather,
                        isOffline = result.isFromCache,
                        isLoading = false
                    )
                }

                is WeatherResult.Error -> {
                    _uiState.value = WeatherUiState(
                        error = "No internet connection",
                        isOffline = true,
                        isLoading = false
                    )
                }
            }
        }
    }

}


