package com.example.weather_app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.firebase.FirebaseFavoritesRepository
import com.example.weather_app.data.repository.WeatherResult
import com.example.weather_app.domain.model.FavoriteCity
import com.example.weather_app.domain.model.Weather
import com.example.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repo: FirebaseFavoritesRepository,
    private val userId: String
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<FavoriteCity>>(emptyList())
    val favorites: StateFlow<List<FavoriteCity>> = _favorites

    private val _favoriteWeather = MutableStateFlow<Map<String, Weather>>(emptyMap())
    val favoriteWeather: StateFlow<Map<String, Weather>> = _favoriteWeather

    init {
        repo.observeFavorites(userId) {
            _favorites.value = it
        }
    }

    fun addFavorite(title: String, note: String, lat: Double, lon: Double) {
        if (note.isBlank()) return

        val city = FavoriteCity(
            id = System.currentTimeMillis().toString(),
            title = title,
            note = note,
            lat = lat,
            lon = lon,
            createdAt = System.currentTimeMillis()
        )

        repo.addFavorite(userId, city)
    }

    fun deleteFavorite(id: String) {
        repo.deleteFavorite(userId, id)
    }

    fun updateNote(id: String, note: String) {
        if (note.isBlank()) return
        repo.updateNote(userId, id, note)
    }

    fun loadWeatherForFavorites(weatherRepository: WeatherRepository) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Weather>()

            favorites.value.forEach { city ->
                try {
                    val result = weatherRepository.getWeather(
                        city.title,
                        city.lat,
                        city.lon
                    )

                    when (result) {
                        is WeatherResult.Success -> {
                            map[city.id] = result.weather
                        }
                        is WeatherResult.Error -> {
                            // ignore error for this city
                        }
                    }

                } catch (_: Exception) {
                    // ignore error
                }
            }

            _favoriteWeather.value = map
        }
    }

}
