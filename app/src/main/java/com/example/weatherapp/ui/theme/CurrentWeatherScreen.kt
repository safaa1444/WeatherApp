package com.example.weatherapp.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.WeatherResponse

@Composable
fun CurrentWeatherScreen(weather: WeatherResponse) {
    val todayForecast = weather.forecast.forecastday.firstOrNull()?.day

    val precipChance = todayForecast?.dailyChanceOfRain
        ?: todayForecast?.dailyChanceOfSnow
        ?: 0
    val precipAmount = weather.current.precipMm ?: 0.0
    val windDirStr = weather.current.windDir ?: ""
    val windDisplay = if (windDirStr.isNotBlank()) {
        "$windDirStr ${weather.current.windKph.toInt()} km/h"
    } else {
        "${weather.current.windKph.toInt()} km/h"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = weather.location.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = listOf(weather.location.region, weather.location.country)
                .filter { it.isNotBlank() }
                .joinToString(", "),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Today's Weather")

                Spacer(modifier = Modifier.height(12.dp))

                val iconUrl = if (weather.current.condition.icon.startsWith("//")) {
                    "https:" + weather.current.condition.icon
                } else {
                    weather.current.condition.icon
                }

                AsyncImage(
                    model = iconUrl,
                    contentDescription = weather.current.condition.text,
                    modifier = Modifier.size(64.dp)
                )

                Text(
                    text = "${weather.current.tempC.toInt()}°C",
                    fontSize = 48.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = weather.current.condition.text,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (todayForecast != null) {
                    Text("High: ${todayForecast.maxTempC.toInt()}°C  •  Low: ${todayForecast.minTempC.toInt()}°C")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Weather Details",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeatherDetailCard(
                title = "Feels Like",
                value = "${weather.current.feelsLikeC.toInt()}°C",
                modifier = Modifier.weight(1f)
            )

            WeatherDetailCard(
                title = "Humidity",
                value = "${weather.current.humidity}%",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeatherDetailCard(
                title = "Wind Speed & Dir",
                value = windDisplay,
                modifier = Modifier.weight(1f)
            )

            WeatherDetailCard(
                title = "Precipitation",
                value = "$precipChance% ($precipAmount mm)",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun WeatherDetailCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 14.sp
            )
        }
    }
}
