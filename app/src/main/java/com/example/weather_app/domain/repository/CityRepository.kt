package com.example.weather_app.domain.repository

import com.example.weather_app.domain.model.City

interface CityRepository {
    suspend fun searchCity(query: String): Result<List<City>>
}
