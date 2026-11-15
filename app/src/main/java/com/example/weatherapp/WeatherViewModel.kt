
package com.example.weatherapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = WeatherRepository()

    var weather by mutableStateOf<WeatherResponse?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadWeather(location: String) {

        viewModelScope.launch {

            isLoading = true
            errorMessage = null

            try {

                weather = repository.getWeather(location)

            } catch (e: Exception) {

                errorMessage = e.message ?: "Unable to load weather."

            } finally {

                isLoading = false
            }
        }
    }
}
