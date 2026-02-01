package com.example.weather_app

import androidx.activity.compose.setContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_app.data.local.SettingsStore
import com.example.weather_app.data.local.WeatherCache
import com.example.weather_app.data.dto.RetrofitClient
import com.example.weather_app.data.repository.CityRepositoryImpl
import com.example.weather_app.data.repository.WeatherRepositoryImpl
import com.example.weather_app.presentation.SearchViewModel
import com.example.weather_app.presentation.SettingsViewModel
import com.example.weather_app.presentation.WeatherViewModel
import com.example.weather_app.presentation.ui.MainScreen
import com.example.weather_app.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }

    @Composable
    private fun App() {

        // --- repositories & stores ---
        val weatherRepository = WeatherRepositoryImpl(
            api = RetrofitClient.api,
            cache = WeatherCache(this)
        )

        val cityRepository = CityRepositoryImpl(
            api = RetrofitClient.geocodingApi
        )

        val settingsStore = SettingsStore(this)

        // --- viewmodels ---
        val weatherViewModel: WeatherViewModel = viewModel {
            WeatherViewModel(weatherRepository)
        }

        val searchViewModel: SearchViewModel = viewModel {
            SearchViewModel(cityRepository)
        }

        val settingsViewModel: SettingsViewModel = viewModel {
            SettingsViewModel(settingsStore)
        }

        WeatherAppTheme {
            Surface {
                MainScreen(
                    weatherViewModel = weatherViewModel,
                    searchViewModel = searchViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }

    }
}
