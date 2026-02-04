package com.example.weather_app.data.repository

import com.example.weather_app.data.dto.OpenMeteoApi
import com.example.weather_app.domain.model.HourlyWeather
import com.example.weather_app.domain.model.Weather
import com.example.weather_app.domain.repository.WeatherRepository
import com.example.weather_app.data.local.WeatherCache
import java.net.SocketTimeoutException
class WeatherRepositoryImpl(
    private val api: OpenMeteoApi,
    private val cache: WeatherCache
) : WeatherRepository {

    override suspend fun getWeather(
        city: String,
        lat: Double,
        lon: Double
    ): WeatherResult {
        return try {
            val response = api.getWeather(lat, lon)

            val hourly = response.hourly.time.take(24).mapIndexed { index, time ->
                HourlyWeather(
                    time = time,
                    temperature = response.hourly.temperature_2m[index]
                )
            }

            val weather = Weather(
                city = city,
                lat = lat,
                lon = lon,
                temperature = response.current_weather.temperature,
                feelsLike = response.current_weather.temperature,
                humidity = response.hourly.relativehumidity_2m.first(),
                windSpeed = response.current_weather.windspeed,
                conditionCode = response.current_weather.weathercode,
                lastUpdate = response.current_weather.time,
                hourly = hourly
            )

            cache.save(weather)
            WeatherResult.Success(weather, isFromCache = false)

        } catch (e: Exception) {
            val cached = cache.load()
            if (cached != null) {
                WeatherResult.Success(cached, isFromCache = true)
            } else {
                WeatherResult.Error(e)
            }
        } catch (e: SocketTimeoutException) {
            WeatherResult.Error(Throwable("Network timeout. Please try again."))
        }

    }

}


