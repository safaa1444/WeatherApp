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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun ForecastScreen(weather: WeatherResponse) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "7-Day Forecast",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = listOf(weather.location.name, weather.location.region)
                .filter { it.isNotBlank() }
                .joinToString(", "),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(weather.forecast.forecastday) { dayItem ->
                val iconUrl = if (dayItem.day.condition.icon.startsWith("//")) {
                    "https:" + dayItem.day.condition.icon
                } else {
                    dayItem.day.condition.icon
                }

                val precipChance = dayItem.day.dailyChanceOfRain
                    ?: dayItem.day.dailyChanceOfSnow
                    ?: 0
                val precipAmount = dayItem.day.totalPrecipMm ?: 0.0

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = dayItem.date,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = dayItem.day.condition.text,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            AsyncImage(
                                model = iconUrl,
                                contentDescription = dayItem.day.condition.text,
                                modifier = Modifier.size(44.dp)
                            )

                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                            Text(
                                text = "${dayItem.day.maxTempC.toInt()}° / ${dayItem.day.minTempC.toInt()}°C",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Max Wind: ${dayItem.day.maxWindKph?.toInt() ?: 0} km/h",
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Precip: $precipChance% ($precipAmount mm)",
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Humidity: ${dayItem.day.avgHumidity?.toInt() ?: 0}%",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
