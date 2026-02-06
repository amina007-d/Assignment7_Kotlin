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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherAppTheme {
                Surface {
                    App()
                }
            }
        }
    }

    @Composable
    private fun App() {

        var userId by remember { mutableStateOf<String?>(null) }
        var authError by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            val auth = FirebaseAuth.getInstance()

            if (auth.currentUser != null) {
                userId = auth.currentUser!!.uid
            } else {
                auth.signInAnonymously()
                    .addOnSuccessListener {
                        userId = it.user?.uid
                    }
                    .addOnFailureListener { e ->
                        authError = e.message
                    }
            }
        }

        // ❌ Ошибка auth
        if (authError != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Auth error: $authError")
            }
            return
        }

        // ⏳ Пока auth не готов
        if (userId == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return
        }

        // ✅ НИЖЕ — ВСЁ РАБОТАЕТ
        val weatherRepository = remember {
            WeatherRepositoryImpl(
                api = RetrofitClient.api,
                cache = WeatherCache(this)
            )
        }

        val cityRepository = remember {
            CityRepositoryImpl(api = RetrofitClient.geocodingApi)
        }

        val settingsStore = remember { SettingsStore(this) }
        val favoritesRepository = remember { FirebaseFavoritesRepository() }

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
            FavoritesViewModel(
                repo = favoritesRepository,
                userId = userId!!
            )
        }

        var showFavorites by remember { mutableStateOf(false) }

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

