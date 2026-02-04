package com.example.weather_app.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.weather_app.presentation.SearchViewModel
import com.example.weather_app.presentation.SettingsViewModel
import com.example.weather_app.presentation.WeatherViewModel
import com.example.weather_app.presentation.FavoritesViewModel
@Composable
fun MainScreen(
    weatherViewModel: WeatherViewModel,
    searchViewModel: SearchViewModel,
    settingsViewModel: SettingsViewModel,
    favoritesViewModel: FavoritesViewModel,
    onOpenFavorites: () -> Unit   // ✅ NEW
) {
    val cities by searchViewModel.cities.collectAsState()
    val unit by settingsViewModel.unit.collectAsState()
    var showSettings by remember { mutableStateOf(false) }
    val error by searchViewModel.error.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {


        WeatherScreen(
            viewModel = weatherViewModel,
            unit = unit,
            favoritesViewModel = favoritesViewModel
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .zIndex(3f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    modifier = Modifier.weight(1f),
                    onSearch = searchViewModel::search
                )


                IconButton(onClick = onOpenFavorites) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorites",
                        tint = Color.White
                    )
                }


                IconButton(onClick = { showSettings = true }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }


            error?.let {
                Text(
                    text = it,
                    color = Color.White,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }

            if (cities.isNotEmpty()) {
                CityList(
                    cities = cities,
                    onCityClick = { city ->
                        weatherViewModel.loadWeather(
                            city = city.name,
                            lat = city.lat,
                            lon = city.lon
                        )
                        searchViewModel.clearResults()
                    }
                )
            }
        }


        if (showSettings) {
            SettingsOverlay(
                viewModel = settingsViewModel,
                onClose = { showSettings = false }
            )
        }
    }
}
