
package com.example.weatherapp

class WeatherRepository {

    suspend fun getWeather(location: String): WeatherResponse {

        return WeatherApi.service.getWeather(
            apiKey = WeatherApi.API_KEY,
            location = location,
            days = 7
        )
    }
}
