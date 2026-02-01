package com.example.weather_app.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.weather_app.domain.model.City

@Composable
fun CityList(
    cities: List<City>,
    onCityClick: (City) -> Unit
) {
    Column {
        cities.forEach { city ->
            Text(
                text = "${city.name}, ${city.country}",
                modifier = androidx.compose.ui.Modifier
                    .clickable { onCityClick(city) }
            )
        }
    }
}
