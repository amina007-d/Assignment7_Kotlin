package com.example.weather_app.data.repository

import com.example.weather_app.data.dto.GeocodingApi
import com.example.weather_app.domain.model.City
import com.example.weather_app.domain.repository.CityRepository
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class CityRepositoryImpl(
    private val api: GeocodingApi
) : CityRepository {

    override suspend fun searchCity(query: String): Result<List<City>> {
        return try {
            val response = api.searchCity(name = query)

            val cities = response.results?.map {
                City(
                    name = it.name,
                    lat = it.latitude,
                    lon = it.longitude,
                    country = it.country
                )
            } ?: emptyList()

            if (cities.isEmpty()) {
                Result.failure(Throwable("City not found"))
            } else {
                Result.success(cities)
            }

        } catch (e: SocketTimeoutException) {
            Result.failure(Throwable("Network timeout. Please try again."))

        } catch (e: HttpException) {
            when (e.code()) {
                404 -> Result.failure(Throwable("City not found"))
                429 -> Result.failure(Throwable("API rate limit exceeded"))
                else -> Result.failure(Throwable("Server error"))
            }

        } catch (e: IOException) {
            Result.failure(Throwable("Network error"))

        } catch (e: Exception) {
            Result.failure(Throwable("Unknown error"))
        }
    }
}