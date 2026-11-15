package com.example.weatherapp

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val location: Location,
    val current: CurrentWeather,
    val forecast: Forecast
)

data class Location(
    val name: String,
    val region: String,
    val country: String
)

data class CurrentWeather(
    @SerializedName("temp_c") val tempC: Double,
    val condition: Condition,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("wind_dir") val windDir: String? = null,
    val humidity: Int,
    @SerializedName("feelslike_c") val feelsLikeC: Double,
    @SerializedName("precip_mm") val precipMm: Double? = null
)

data class Condition(
    val text: String,
    val icon: String
)

data class Forecast(
    @SerializedName("forecastday") val forecastday: List<ForecastDayItem>
)

data class ForecastDayItem(
    val date: String,
    val day: Day
)

data class Day(
    @SerializedName("maxtemp_c") val maxTempC: Double,
    @SerializedName("mintemp_c") val minTempC: Double,
    @SerializedName("maxwind_kph") val maxWindKph: Double? = null,
    @SerializedName("totalprecip_mm") val totalPrecipMm: Double? = null,
    @SerializedName("avghumidity") val avgHumidity: Double? = null,
    @SerializedName("daily_chance_of_rain") val dailyChanceOfRain: Int? = null,
    @SerializedName("daily_chance_of_snow") val dailyChanceOfSnow: Int? = null,
    val condition: Condition
)
