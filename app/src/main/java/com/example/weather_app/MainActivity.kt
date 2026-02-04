package com.example.weather_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_app.data.firebase.FirebaseFavoritesRepository
import com.example.weather_app.data.local.SettingsStore
import com.example.weather_app.data.local.WeatherCache
import com.example.weather_app.data.dto.RetrofitClient
import com.example.weather_app.data.repository.CityRepositoryImpl
import com.example.weather_app.data.repository.WeatherRepositoryImpl
import com.example.weather_app.presentation.*
import com.example.weather_app.presentation.ui.*
import com.example.weather_app.ui.theme.WeatherAppTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInAnonymously()
        }

        setContent {
            App()
        }
    }

    @Composable
    private fun App() {

        val weatherRepository = remember {
            WeatherRepositoryImpl(
                api = RetrofitClient.api,
                cache = WeatherCache(this)
            )
        }

        val cityRepository = remember {
            CityRepositoryImpl(
                api = RetrofitClient.geocodingApi
            )
        }

        val settingsStore = remember {
            SettingsStore(this)
        }

        val favoritesRepository = remember {
            FirebaseFavoritesRepository()
        }

        val userId = auth.currentUser?.uid ?: "anonymous"

        val weatherViewModel: WeatherViewModel = viewModel {
            WeatherViewModel(weatherRepository)
        }

        val searchViewModel: SearchViewModel = viewModel {
            SearchViewModel(cityRepository)
        }

        val settingsViewModel: SettingsViewModel = viewModel {
            SettingsViewModel(settingsStore)
        }

        val favoritesViewModel: FavoritesViewModel = viewModel {
            FavoritesViewModel(favoritesRepository, userId)
        }

        // screen switch state
        var showFavorites by remember { mutableStateOf(false) }

        WeatherAppTheme {
            Surface {
                if (showFavorites) {
                    FavoritesScreen(
                        viewModel = favoritesViewModel,
                        weatherRepository = weatherRepository,
                        onBack = { showFavorites = false }
                    )
                } else {
                    MainScreen(
                        weatherViewModel = weatherViewModel,
                        searchViewModel = searchViewModel,
                        settingsViewModel = settingsViewModel,
                        favoritesViewModel = favoritesViewModel,
                        onOpenFavorites = { showFavorites = true }
                    )
                }

            }
        }
    }
}
