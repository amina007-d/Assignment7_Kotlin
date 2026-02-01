package com.example.weather_app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.domain.model.City
import com.example.weather_app.domain.repository.CityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: CityRepository
) : ViewModel() {

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun search(query: String) {
        if (query.isBlank()) {
            _cities.value = emptyList()
            _error.value = "Please enter a city name"
            return
        }

        if (query.length < 2) {
            _cities.value = emptyList()
            _error.value = null
            return
        }

        viewModelScope.launch {
            repository.searchCity(query)
                .onSuccess {
                    _cities.value = it
                    _error.value = null
                }
                .onFailure {
                    _cities.value = emptyList()
                    _error.value = it.message ?: "City not found"
                }
        }
    }


    fun clearResults() {
        _cities.value = emptyList()
        _error.value = null
    }
}

